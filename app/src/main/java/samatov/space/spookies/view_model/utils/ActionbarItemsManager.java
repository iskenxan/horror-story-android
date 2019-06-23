package samatov.space.spookies.view_model.utils;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.actionitembadge.library.ActionItemBadge;

import java.util.Map;

import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.beans.notification.NotificationsFeed;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.activities.BaseToolbarActivity;
import samatov.space.spookies.view_model.activities.FeedActivity;
import samatov.space.spookies.view_model.activities.ViewProfileActivity;
import samatov.space.spookies.view_model.activities.my_profile.OnSearchSuggestionClick;
import samatov.space.spookies.view_model.activities.my_profile.SearchUserHandler;
import samatov.space.spookies.view_model.fragments.notification.NotificationFragment;

public class ActionbarItemsManager {

    private BaseToolbarActivity mActivity;
    private Menu mMenu;


    public ActionbarItemsManager(BaseToolbarActivity activity, Menu menu) {
        mMenu = menu;
        mActivity = activity;
        setupSearchAction();
        setupNotificationAction();
    }


    private void setupSearchAction() {
        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) mMenu.findItem(R.id.action_search).getActionView();
        SearchUserHandler searchHandler = new SearchUserHandler(searchView,
                searchManager, mActivity, onSearchResultListener(), onSearchItemClickListener());
    }


    public void updateNotifications() {
        setupNotificationAction();
    }
    

    private void setupNotificationAction() {
        mActivity.getNotificationFeed(false, false, (result, exception) -> {
            NotificationsFeed feed = (NotificationsFeed) result;
            int feedCount = feed != null ? feed.getUnseen() : 0;

            if (feedCount <= 0)
                return;

            swapBadges();
        });
    }


    public void onActionBarItemClicked(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notifications || id == R.id.action_notifications_badge) {
            MenuItem itemWithoutBadge = mMenu.findItem(R.id.action_notifications);
            MenuItem itemWithBadge = mMenu.findItem(R.id.action_notifications_badge);
            itemWithoutBadge.setVisible(true);
            itemWithBadge.setVisible(false);

            startNotificationFragment();
        }
    }


    private void startNotificationFragment() {
        if (mActivity instanceof FeedActivity)
            ((FeedActivity) mActivity).hideViewPagerTabs();

        mActivity.stackFragment(NotificationFragment.Companion.newInstance(),
                mActivity.mPlaceholder, "notification_fragment");
    }


    private void swapBadges() {
        MenuItem itemWithoutBadge = mMenu.findItem(R.id.action_notifications);
        MenuItem itemWithBadge = mMenu.findItem(R.id.action_notifications_badge);
        itemWithBadge.setVisible(true);
        itemWithoutBadge.setVisible(false);
        Drawable iconDrawable = ContextCompat.getDrawable(mActivity, R.drawable.notification_icon);
        ActionItemBadge.update(mActivity, mMenu.findItem(R.id.action_notifications_badge),
                iconDrawable, ActionItemBadge.BadgeStyles.RED, "new");
    }


    private ApiRequestListener onSearchResultListener() {
        return (result, exception) -> {
            if (exception != null) {
                mActivity.displayErrorDialog();
                return;
            }
        };
    }


    private OnSearchSuggestionClick onSearchItemClickListener() {
        return (username) -> {
            Map<String, User> users = MyPreferenceManager.getMapOfObjects(mActivity,
                    MyPreferenceManager.USER_SEARCH_RESULT, User.class);
            User user = users.get(username);
            startViewUserProfileActivity(user);
        };
    }


    private void startViewUserProfileActivity(User user) {
        MyPreferenceManager.addToViewedUsersStack(mActivity, user);
        ActivityFactory.startActivity(mActivity,
                ViewProfileActivity.class, true, false);
    }

}
