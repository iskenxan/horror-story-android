package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Feed;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.fragments.FeedFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class FeedActivity extends BaseActivity {



    @BindView(R.id.feedToolbar) Toolbar mToolbar;
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
        setupMainActionbar(mToolbar, "");
        getFeed();
    }


    //TODO: figure out a way to store likes and comments for the activity
    //TODO: setup viewholder for the feed objects

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
            MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.FEED_POPULAR, mFeed.getTimeline());
            replaceFragment(FeedFragment.newInstance(), R.id.feedActivityMainPlaceholder);
        });
    }


    public SmartTabLayout getViewPagerTabs() {
        return mViewPagerTabs;
    }
}
