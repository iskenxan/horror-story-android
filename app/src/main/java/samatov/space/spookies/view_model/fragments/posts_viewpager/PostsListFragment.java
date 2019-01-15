package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.fragments.BaseFragment;
import samatov.space.spookies.view_model.dialogs.favorite.FavoriteDialogHandler;


public class PostsListFragment extends BaseFragment {


    public PostsListFragment() { }


    public static PostsListFragment newInstance(Map<String, JsonObject> posts, POST_TYPE type) {
        PostsListFragment fragment = new PostsListFragment();

        Gson gson = new GsonBuilder().serializeNulls().create();
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
            mPosts = new GsonBuilder().serializeNulls().create().fromJson(postsStr, JsonObject.class);
        if (postType != null)
            mPostsType = POST_TYPE.valueOf(postType);
    }


    private void setupViews() {
        List<JsonObject> list = getFormattedPostList(mPosts);
        if (list.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
        } else
            setupRecyclerView(list);
    }


    private void setupRecyclerView(List<JsonObject> list) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostsListAdapter(list, getItemClickedListener(), true, mPostsType);
        mRecyclerView.setAdapter(mAdapter);
    }


    private PostListItemClicked getItemClickedListener() {
        return (postId, clickedType) -> {
            if (clickedType == ClickItemType.READ_POST)
                onReadPostClicked(postId);
            else if (clickedType == ClickItemType.FAVORITE)
                onViewLikesClicked(postId);
            else if (clickedType == ClickItemType.COMMENT)
                onViewCommentsClicked(postId);
        };
    }


    private void onReadPostClicked(String postId) {
        MyPreferenceManager.saveObjectAsJson(getContext(), MyPreferenceManager.CURRENT_POST_TYPE, mPostsType);
        MyPreferenceManager.saveString(getContext(), MyPreferenceManager.CURRENT_POST_ID, postId);
        mActivity.startEditPostActivity();
    }


    private void onViewLikesClicked(String postId) {
        Post post = getPost(postId);
        FavoriteDialogHandler dialogHandler = new FavoriteDialogHandler(mActivity, post.getFavorite());
        dialogHandler.showDialog();
    }


    private void onViewCommentsClicked(String postId) {
       Post post = getPost(postId);
       mActivity.startViewCommentFragment(post);
    }


    private Post getPost(String postId) {
        JsonObject postObj = mPosts.getAsJsonObject(postId);
        Post post = new GsonBuilder().serializeNulls().create().fromJson(postObj, Post.class);
        post.setId(postId);

        return post;
    }


    @Override
    public void onResume() {
        super.onResume();
        extractPostsArgument();
        setupViews();
    }
}
