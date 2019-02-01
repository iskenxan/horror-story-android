package samatov.space.spookies.view_model.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nightonke.boommenu.BoomMenuButton;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Comment;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.beans.notification.NotificationsFeed;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.fragments.post.comment.CommentFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.BmbMenuFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public abstract class BaseActivity extends AppCompatActivity {

    protected  SweetAlertDialog mDialog;
    public int mPlaceholder;


    public void fetchMyPublished(String postId, ApiRequestListener listener) {
        Observable<Post> observable = Post.getPublished(postId, this);
        displayLoadingDialog();
        listenToObservable(observable, (result, exception) -> {
            mDialog.dismiss();
            if (exception != null) {
                displayErrorDialog();
                return;
            }

            listener.onRequestComplete(result, null);
        });
    }



    public void getNotificationFeed(boolean showLoading, boolean showError, ApiRequestListener listener) {
        if (showLoading)
            displayLoadingDialog();

        Observable<NotificationsFeed> observable = NotificationsFeed.getNotificationFeed(this);
        listenToObservable(observable, (result, exception) -> {
            if (mDialog != null)
                mDialog.dismiss();
            if (exception != null && showError) {
                displayErrorDialog();
                return;
            }
            listener.onRequestComplete(result, null);
        });
    }


    public void getUserAndStartViewProfileActivity(String username, boolean finishCurrent) {
        BaseActivity activity = this;
        displayLoadingDialog();
        Observable<User> observable = User.getOtherUserInfo(username, this);
        listenToObservable(observable, (result, exception) -> {
            mDialog.dismiss();
            if (exception != null) {
                displayErrorDialog();
                return;
            }

            User viewedUser = (User) result;
            MyPreferenceManager.addToViewedUsersStack(activity, viewedUser);
            ActivityFactory
                    .startActivity(activity, ViewProfileActivity.class, true, finishCurrent);
        });
    }


    public void fetchPostAndStartReadCommentFragment(String postId, String username, int placeholder) {
        displayLoadingDialog();
        Observable<Post> observable = Post.getOtherUserPost(postId, username, this);
        listenToObservable(observable, (result, exception) -> {
            mDialog.dismiss();
            if (exception != null) {
                displayErrorDialog();
                return;
            }
            Post post = (Post) result;
            startReadCommentFragment(post, username, placeholder);
        });
    }


    private void startReadCommentFragment(Post post, String username, int placeholder) {
        MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, post);
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_AUTHOR, username);
        stackFragment(CommentFragment.newInstance(this), placeholder, "current_post");
    }


    public void addComment(String authorUsername, String postId, Comment comment, ApiRequestListener listener) {
        displayLoadingDialog();
        Observable<Comment> observable = Post.addComment(authorUsername, postId, comment, this);
        listenToObservable(observable, (result, exception) -> {
            mDialog.dismiss();
            onAddCommentResult(result, exception);
            listener.onRequestComplete(result, exception);
        });
    }


    private void onAddCommentResult(Object result, Throwable exception) {
        if (exception != null) {
            displayErrorDialog();
        } else {
            Post currentPost = MyPreferenceManager.getObject(this,
                    MyPreferenceManager.CURRENT_POST, Post.class);
            currentPost.getComments().add((Comment) result);
            MyPreferenceManager
                    .saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, currentPost);
        }
    }


    protected void stackFragment(Fragment fragment, int placeholder, String tag) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(placeholder, fragment, tag)
                .addToBackStack(tag).commit();
    }


    protected void replaceFragment(Fragment fragment, int placeholder) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(placeholder, fragment).commit();
    }


    protected void setupMainActionbar(Toolbar toolbar, String title) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View actionBar = inflater.inflate(R.layout.main_action_bar, null);

        TextView titleTextView = actionBar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText(title);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setCustomView(actionBar);
        bar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);
        BoomMenuButton bmb = actionBar.findViewById(R.id.toolbarBmb);
        BmbMenuFactory.setupBmb(this, bmb);
    }


    public void displayLoadingDialog() {
        mDialog = DialogFactory.getLoadingDialog(this, "Loading...");
        mDialog.show();
    }


    public void displayErrorDialog() {
        String errorText = "Couldn't complete your request. Please try again later.";
        mDialog = DialogFactory.getErrorDialog(this, errorText, null);
        mDialog.show();
    }


    public void listenToObservable(Observable observable, ApiRequestListener listener) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(requestObserver(listener));
    }


    public void listenToCompletable(Completable completable, ApiRequestListener listener) {
        completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        listener.onRequestComplete(null, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onRequestComplete(null, e);
                    }
                });
    }


    protected Observer<Object> requestObserver(ApiRequestListener listener) {
        return new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object auth) {
                listener.onRequestComplete(auth, null);
            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestComplete(null, e);
            }

            @Override
            public void onComplete() {

            }
        };
    }


    protected boolean handleBackPressed() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }
}
