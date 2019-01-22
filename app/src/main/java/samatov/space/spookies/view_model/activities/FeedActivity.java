package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Feed;
import samatov.space.spookies.model.api.beans.FeedItem;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.fragments.FeedFragment;
import samatov.space.spookies.view_model.fragments.post.comment.CommentFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class FeedActivity extends BaseToolbarActivity {



    @BindView(R.id.feedToolbar) Toolbar mToolbar;
    @BindView(R.id.feedActivityAppBarLayout)AppBarLayout mAppBarLayout;
    @BindView(R.id.feedViewPagerTabs)SmartTabLayout mViewPagerTabs;

    SweetAlertDialog mDialog;
    FeedActivity mActivity;
    Feed mFeed;


    //TODO: finish setting up activity. Test the whole like and comment workflow, seems buggy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        mActivity = this;
        super.mToolbar = mToolbar;
        setupMainActionbar(mToolbar, "");
        getFeed();
    }



    private void getFeed() {
        mDialog = DialogFactory.getLoadingDialog(this, "Loading your feed...");
        mDialog.show();
        Observable<Feed> feedObservable = Feed.getMyFeed(this);
        listenToObservable(feedObservable, (result, exception) -> {
            mDialog.dismiss();

            if (exception != null) {
                String text = "Error loading your feed. Please try again later";
                mDialog = DialogFactory.getErrorDialog(mActivity, text, (dialog) -> {
                    dialog.dismiss();
                    ActivityFactory.startActivity(mActivity,
                            MyProfileActivity.class, true, true);
                });
                return;
            }

            mFeed = (Feed) result;
            MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.FEED_TIMELINE, mFeed.getTimeline());
            MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.FEED_POPULAR, mFeed.getPopular());
            replaceFragment(FeedFragment.newInstance(), R.id.feedActivityMainPlaceholder);
        });
    }


    public void startReadPostActivity(FeedItem item) {
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_ID, item.getId());
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_AUTHOR, item.getAuthor());
        ActivityFactory
                .startActivity(this, ReadPostActivity.class, true, false);
    }


    public void startReadCommentsFragment(FeedItem item) {
        mAppBarLayout.setVisibility(View.GONE);
        MyPreferenceManager
                .saveString(this, MyPreferenceManager.CURRENT_POST_AUTHOR, item.getAuthor());
        MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, item);
        stackFragment(CommentFragment.newInstance(this, true),
                R.id.feedActivityMainPlaceholder, "read_comment_fragment");
    }


    public SmartTabLayout getViewPagerTabs() {
        return mViewPagerTabs;
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.feedActivityMainPlaceholder);

        if (currentFragment instanceof CommentFragment)
            mAppBarLayout.setVisibility(View.VISIBLE);

        handleBackPressed();
    }
}
