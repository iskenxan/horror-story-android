package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.model.utils.Formatter;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.fragments.edit_post.EditPostFragment;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class EditPostActivity extends BaseActivity {

    SweetAlertDialog mDialog;
    AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        mActivity = this;
        checkCurrentPostAndStartFragment();
    }


    private void checkCurrentPostAndStartFragment() {
        String currentPostId = MyPreferenceManager.getString(this, MyPreferenceManager.CURRENT_POST_ID);
        if (Validator.isNullOrEmpty(currentPostId)) {
            replaceFragment(EditPostFragment.newInstance(), R.id.editPostActivityPlaceHolder);
            return;
        }

        mDialog = DialogFactory.getLoadingDialog(this, "Loading...");
        mDialog.show();
        Observable observable = getPostRequestObservable(currentPostId);
        listenToObservable(observable, (result, exception) -> {
            mDialog.dismiss();
            if (exception == null) {
                MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, result);
                replaceFragment(EditPostFragment.newInstance(), R.id.editPostActivityPlaceHolder);
            }
            else
                showGetPostErrorDialog();
        });
    }


    private Observable getPostRequestObservable(String currentPostId) {
        POST_TYPE type = MyPreferenceManager
                .getObject(this, MyPreferenceManager.CURRENT_POST_TYPE, POST_TYPE.class);
        Observable observable = Post.getDraft(currentPostId, this);
        if (type == POST_TYPE.PUBLISHED)
            observable = Post.getPublished(currentPostId, this);

        return observable;
    }


    private void showGetPostErrorDialog() {
        String text = "Error retrieving your post. Please try again in a few moments.";
        mDialog = DialogFactory.getErrorDialog(this, text, (dialog) -> {
            dialog.dismiss();
            mActivity.supportFinishAfterTransition();
        });
        mDialog.show();
    }


    @Override
    public void onBackPressed() {
        POST_TYPE type = MyPreferenceManager
                .getObject(this, MyPreferenceManager.CURRENT_POST_TYPE, POST_TYPE.class);
        if (type == POST_TYPE.DRAFT)
            showExitConfirmDialog();
        else
            supportFinishAfterTransition();
    }


    public void showExitConfirmDialog() {
        String message = "Do you want to stop editing and delete unsaved changes?";
        mDialog = DialogFactory.getAlertDialog(this, "Leaving?", message, true,
                dialog -> {
                    dialog.dismiss();
                    mActivity.supportFinishAfterTransition();
                }
        );
        mDialog.show();
    }


    public void unpublishPost(Post published) {
        mDialog = DialogFactory.getAlertDialog(this, "Unpublish?",
                "Are you sure you want to unpublish this post and move it to drafts?", true, d -> {
                    d.dismiss();
                    sendUnpublishRequest(published);
                });
        mDialog.show();
    }


    private void sendUnpublishRequest(Post published) {
        listenToObservable(Post.unpublishPost(published.getId(), this), (result, exception) -> {
            if (exception == null)
                movePostToDrafts(result);
            String error = "There was an error unpublishing your post. Please try again in a few moments.";
            String success = "Your post was unpublished.";
            mDialog.dismiss();
            displayApiResult(exception, error, success);
        });
    }


    private void movePostToDrafts(Object result) {
        Post draft = (Post) result;
        User user = MyPreferenceManager.getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        JsonObject postRef = Formatter.constructRefFromPost(draft);
        user.getDraftRefs().put(draft.getId(), postRef);
        user.getPublishedRefs().remove(draft.getId());
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_USER, user);
    }


    public void deleteDraft(Post draft) {
        if (Validator.isNullOrEmpty(draft.getId())) {
            mActivity.supportFinishAfterTransition();
            return;
        }

        mDialog = DialogFactory.getAlertDialog(this, "Delete draft?",
                "You won't be able to undo this action", true, d -> {
           d.dismiss();
           sendDeleteRequest(draft.getId());
        });
        mDialog.show();
    }


    private void sendDeleteRequest(String draftId) {
        mDialog = DialogFactory.getLoadingDialog(this, "Deleting your post");
        mDialog.show();
        listenToObservable(Post.deleteDraft(draftId, this), (result, exception) -> {
            String error = "There was an error deleting your post. Please try again in a few moments.";
            String success = "Your post was deleted.";
            if (exception == null)
                deleteDraftFromMyPref(result);
            mDialog.dismiss();
            displayApiResult(exception, error, success);
        });
    }


    private void deleteDraftFromMyPref(Object result) {
        String draftId = (String) result;
        User user = MyPreferenceManager.getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        user.getDraftRefs().remove(draftId);
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_USER, user);
    }


    public void saveDraft(Post draft) {
        mDialog = DialogFactory.getLoadingDialog(this, "Saving your post");
        mDialog.show();
        Observable<Post> observable = Post.saveDraft(draft, this);
        if (!Validator.isNullOrEmpty(draft.getId()))
            observable = Post.updateDraft(draft, this);

        listenToObservable(observable, (result, exception) -> {
            if (exception == null)
                addDraftToMyPref(result);
            mDialog.dismiss();
            String error = "There was an error saving your post. Please try again in a few moments.";
            String success = "Your post was saved.";
            displayApiResult(exception, error, success);
        });
    }


    private void addDraftToMyPref(Object result) {
        Post savedDraft = (Post) result;
        User user = MyPreferenceManager.getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        JsonObject draftRef = Formatter.constructRefFromPost(savedDraft);
        user.getDraftRefs().put(savedDraft.getId(), draftRef);
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_USER, user);
    }


    public void publishPost(Post post) {
        mDialog = DialogFactory.getLoadingDialog(this, "Publishing your post");
        mDialog.show();
        listenToObservable(Post.publishPost(post, this), (result, exception) -> {
            if (exception == null)
                moveDraftToPublished(result, post.getId());
            mDialog.dismiss();
            String errorText = "There was an error publishing your post. Please try again in a few moments.";
            String successText = "Your post was published";
            displayApiResult(exception, errorText, successText);
        });
    }


    private void moveDraftToPublished(Object result, String oldDraftId) {
        Post publishedPost = (Post) result;
        User user = MyPreferenceManager.getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        JsonObject postRef = Formatter.constructRefFromPost(publishedPost);
        user.getPublishedRefs().put(publishedPost.getId(), postRef);
        user.getDraftRefs().remove(oldDraftId);
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_USER, user);
    }


    private void displayApiResult(Throwable exception, String errorText, String successText) {
        if (exception != null)
            showSaveErrorDialog(errorText);
        else
            showSaveSuccessDialog(successText);
    }


    private void showSaveErrorDialog(String text) {
        mDialog = DialogFactory.getErrorDialog(this, text, d -> d.dismiss());
        mDialog.show();
    }


    private void showSaveSuccessDialog(String text) {

        mDialog = DialogFactory.getSuccessDialog(this, text, dialog -> {
            dialog.dismiss();
            mActivity.supportFinishAfterTransition();
        });
        mDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyPreferenceManager.cleanAllListeners(this);
    }
}
