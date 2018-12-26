package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.activities.MyProfileActivity;


public class PostsListFragment extends Fragment {


    public PostsListFragment() { }


    public static PostsListFragment newInstance(Map<String, JsonObject> posts, POST_TYPE type) {
        PostsListFragment fragment = new PostsListFragment();

        Gson gson = new Gson();
        String jsonStr = gson.toJson(posts);
        Bundle bundle = new Bundle();
        bundle.putString("posts", jsonStr);
        bundle.putString("post_type", type.toString());
        fragment.setArguments(bundle);

        return fragment;
    }


    @BindView(R.id.postListFragmentRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.postListEmptyListContainer) LinearLayout mEmptyListView;


    JsonObject mPosts;
    private RecyclerView.LayoutManager mLayoutManager;
    private PostsListAdapter mAdapter;
    private MyProfileActivity mActivity;
    POST_TYPE mPostsType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        ButterKnife.bind(this, view);

        mActivity = (MyProfileActivity) getActivity();

        return view;
    }


    private void extractPostsArgument() {
        String postsStr = getArguments().getString("posts");
        String postType = getArguments().getString("post_type");
        if (postsStr != null)
            mPosts = new Gson().fromJson(postsStr, JsonObject.class);
        if (postType != null)
            mPostsType = POST_TYPE.valueOf(postType);
    }


    private void setupViews() {
        List<JsonObject> list = getFormattedPostList();
        if (list.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
        } else
            setupRecyclerView(list);
    }


    private void setupRecyclerView(List<JsonObject> list) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostsListAdapter(list, postId -> {
            MyPreferenceManager.saveObjectAsJson(getContext(), EditPostActivity.CURRENT_POST_TYPE, mPostsType);
            MyPreferenceManager.saveString(getContext(), EditPostActivity.CURRENT_POST_ID, postId);
            mActivity.startEditPostActivity();
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    private List<JsonObject> getFormattedPostList() {
        List<JsonObject> list = new ArrayList<>();

        for (String key: mPosts.keySet()) {
            JsonObject item = getFormattedListItem(key);
            list.add(item);
        }
        sortPostsByTimestamp(list);

        return list;
    }


    private JsonObject getFormattedListItem(String key) {
        JsonObject value;
        value = mPosts.get(key).getAsJsonObject();
        value.addProperty("id", key);

        return value;
    }


    private void sortPostsByTimestamp(List<JsonObject> list) {
        Collections.sort(list, (a, b) ->
                (int) (b.getAsJsonPrimitive("created").getAsLong()
                - (a.getAsJsonPrimitive("created").getAsLong())));
    }


    @Override
    public void onResume() {
        super.onResume();
        extractPostsArgument();
        setupViews();
    }
}
