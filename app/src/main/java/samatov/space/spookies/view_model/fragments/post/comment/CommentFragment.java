package samatov.space.spookies.view_model.fragments.post.comment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.MessageInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Comment;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.view_model.activities.BaseActivity;
import samatov.space.spookies.view_model.activities.ReadPostActivity;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.utils.DialogFactory;


public class CommentFragment extends Fragment implements MessageInput.InputListener {

    public CommentFragment() {
        // Required empty public constructor
    }


    public static CommentFragment newInstance(AppCompatActivity activity, boolean fetchPost) {
        CommentFragment fragment = new CommentFragment();
        fragment.setEnterTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_right));
        fragment.setExitTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_right));

        Bundle args = new Bundle();
        args.putBoolean("fetch_post", fetchPost);
        fragment.setArguments(args);

        return fragment;
    }


    @BindView(R.id.commentFragmentCommentCountTextView)TextView mCommentCountTextView;
    @BindView(R.id.commentFragmentMessageInput)MessageInput mMessageInput;
    @BindView(R.id.commentFragmentRecyclerView)RecyclerView mRecyclerView;

    Post mPost;
    BaseActivity mActivity;
    CommentListAdapter mAdapter;
    SweetAlertDialog mDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);
        mActivity = (BaseActivity) getActivity();
        getPost();

        return view;
    }


    private void getPost() {
        mPost = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_POST, Post.class);
        Boolean fetchPost = getArguments().getBoolean("fetch_post");
        if (fetchPost) {
            fetchPost();
            return;
        }

        setupViews();
    }


    //current post doesn't have the actual comments so we have to fetch the full post from the server
    private void fetchPost() {
        mDialog = DialogFactory.getLoadingDialog(mActivity, "Loading comments...");
        mDialog.show();
        String postAuthor = MyPreferenceManager.getString(getContext(), MyPreferenceManager.CURRENT_POST_AUTHOR);
        Observable<Post> observable = Post.getOtherUserPost(mPost.getId(), postAuthor, getContext());
        mActivity.listenToObservable(observable, (result, exception) -> {
            mDialog.dismiss();
            if (exception != null) {
                showFetchError();
                return;
            }
            mPost = (Post) result;
            setupViews();
        });
    }


    private void showFetchError() {
        String errorText = "Error retrieving the post. Please try again later";
        SweetAlertDialog dialog = DialogFactory.getErrorDialog(mActivity, errorText, (d) -> {
            d.dismiss();
            mActivity.onBackPressed();
        });
        dialog.show();
    }


    private void setupViews() {
        mCommentCountTextView.setText(mPost.getComments().size() + " comments");
        setupRecyclerView();
        mMessageInput.setInputListener(this);
    }


    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CommentListAdapter(mPost.getComments());
        mRecyclerView.setAdapter(mAdapter);
    }


    // on new comment submit
    @Override
    public boolean onSubmit(CharSequence input) {
        String text = input + "";

        User user = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_USER, User.class);
        String postAuthor = MyPreferenceManager
                .getString(getContext(), MyPreferenceManager.CURRENT_POST_AUTHOR);

        Comment comment = new Comment();
        comment.setProfileImageUrl(user.getProfileUrl());
        comment.setText(text);
        comment.setUsername(user.getUsername());

        mActivity.addComment(postAuthor, mPost.getId(), comment, (result, exception) -> {
            if (exception != null)
                return;

            Comment returnedComment = (Comment) result;
            mPost.getComments().add(returnedComment);
            mAdapter.notifyDataSetChanged();
            mCommentCountTextView.setText(mPost.getComments().size() + " comments");
            mRecyclerView.scrollToPosition(mPost.getComments().size() - 1);
        });

        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity instanceof ReadPostActivity) {
            ReadPostActivity activity = (ReadPostActivity) mActivity;
            activity.showToolbar();
        } else if (mActivity instanceof MyProfileActivity) {
            MyProfileActivity activity = (MyProfileActivity) mActivity;
            activity.showToolbar();
        }
    }
}
