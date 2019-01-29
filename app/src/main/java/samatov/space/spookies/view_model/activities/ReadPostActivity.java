package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.view_model.fragments.post.comment.CommentFragment;
import samatov.space.spookies.view_model.fragments.post.read_post.ReadPostFragment;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class ReadPostActivity extends BaseToolbarActivity {

    Post mPost;
    SweetAlertDialog mDialog;
    BaseActivity mActivity;

    @BindView(R.id.readPostToolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
        ButterKnife.bind(this);
        super.mToolbar = mToolbar;
        mActivity = this;
        getPost();
    }


    private void getPost() {
        mDialog = DialogFactory.getLoadingDialog(this, "Loading...");
        mDialog.show();
        String postId = MyPreferenceManager.getString(this, MyPreferenceManager.CURRENT_POST_ID);
        String authorUsername = MyPreferenceManager.getString(this, MyPreferenceManager.CURRENT_POST_AUTHOR);

        Observable<Post> observable = Post.getOtherUserPost(postId, authorUsername, this);
        listenToObservable(observable, (result, exception) -> {
           mDialog.dismiss();
           if (exception != null) {
               showErrorDialog();
               return;
           }
           mPost = (Post) result;
           onPostLoadSuccess();
        });
    }


    private void showErrorDialog() {
        String errorText = "Error loading the post. Please try again later.";
        mDialog = DialogFactory.getErrorDialog(this, errorText, (dialog) -> {
           dialog.dismiss();
           mActivity.finishAfterTransition();
        });
        mDialog.show();
    }


    private void onPostLoadSuccess() {
        setToolbarTitle(mPost.getOtherCharacterName());
        setupToolbar();
        MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, mPost);
        replaceFragment(ReadPostFragment.newInstance(), R.id.readPostMainPlaceholder);
    }


    public void startCommentFragment() {
        mToolbar.setVisibility(View.GONE);
        CommentFragment fragment = CommentFragment.newInstance(this);
        stackFragment(fragment, R.id.readPostMainPlaceholder, "comment_fragment");
    }


    private void setToolbarTitle(String title) {
        TextView titleTextView = mToolbar.findViewById(R.id.readPostToolbarTitleTextView);
        titleTextView.setText(title);
    }
}
