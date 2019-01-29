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
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.fragments.FeedFragment;
import samatov.space.spookies.view_model.fragments.feed_viewpager.FeedType;
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
        fetchFeed(() -> {
            MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.FEED_TIMELINE, mFeed.getTimeline());
            MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.FEED_POPULAR, mFeed.getPopular());
            replaceFragment(FeedFragment.newInstance(), R.id.feedActivityMainPlaceholder);
        });
    }


    public void fetchTimelineFeed( FeedType type, ApiRequestListener listener) {
        fetchFeed(() -> {
            if (type == FeedType.TIMELINE)
                listener.onRequestComplete(mFeed.getTimeline(), null);
            else
                listener.onRequestComplete(mFeed.getPopular(), null);
        });
    }


    private void fetchFeed(Runnable onFetchSuccess) {
        Observable<Feed> feedObservable = Feed.getMyFeed(this);
        listenToObservable(feedObservable, (result, exception) -> {
            if (mDialog != null)
                mDialog.dismiss();

            if (exception != null) {
                String text = "Error loading your feed. Please try again later";
                mDialog = DialogFactory.getErrorDialog(mActivity, text, (dialog) -> {
                    dialog.dismiss();
                    ActivityFactory.startActivity(mActivity,
                            MyProfileActivity.class, true, true);
                });
                mDialog.show();
                return;
            }

            mFeed = (Feed) result;
            onFetchSuccess.run();
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
        fetchPostAndStartReadCommentFragment(item.getId(), item.getAuthor(), R.id.feedActivityMainPlaceholder);
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
