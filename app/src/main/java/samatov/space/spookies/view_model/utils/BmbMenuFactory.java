package samatov.space.spookies.view_model.utils;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.view_model.activities.AuthActivity;
import samatov.space.spookies.view_model.activities.FeedActivity;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;

public class BmbMenuFactory {

    public static void setupBmb(AppCompatActivity activity, BoomMenuButton bmb) {
        addMyFeed(bmb, activity);
        addMyProfile(bmb, activity);
        addLogout(bmb, activity);
    }


    private static void addMyFeed(BoomMenuButton bmb, AppCompatActivity activity) {
        HamButton.Builder builder = getBasicBuilder()
                .normalText("My Feed")
                .normalImageRes(R.drawable.menu_home_icon)
                .highlightedImageRes(R.drawable.menu_home_icon_white)
                .listener(index ->
                        ActivityFactory.startActivity(activity,
                                FeedActivity.class, true, true));
        bmb.addBuilder(builder);
    }


    private static void addMyProfile(BoomMenuButton bmb, AppCompatActivity activity) {
        HamButton.Builder builder = getBasicBuilder()
                .normalText("My Profile")
                .normalImageRes(R.drawable.menu_profile_icon)
                .highlightedImageRes(R.drawable.menu_profile_icon_white)
                .listener(index -> {
                    MyPreferenceManager.cleanViewedUsersStack(activity);
                    ActivityFactory.startActivity(activity, MyProfileActivity.class, true, true);
                });
        bmb.addBuilder(builder);
    }


    private static void addLogout(BoomMenuButton bmb, AppCompatActivity activity) {
        HamButton.Builder builder = getBasicBuilder()
                .normalText("Logout")
                .normalImageRes(R.drawable.menu_logout_icon)
                .highlightedImageRes(R.drawable.menu_logout_icon_white)
                .listener(index -> {
                    MyPreferenceManager.cleanPreferencesOnLogout(activity);
                    ActivityFactory.startActivity(activity, AuthActivity.class, true, true);
                });
        bmb.addBuilder(builder);
    }


    private static HamButton.Builder getBasicBuilder() {
        return new HamButton.Builder()
                .normalTextColorRes(R.color.colorPrimary)
                .highlightedTextColor(Color.WHITE)
                .containsSubText(false)
                .highlightedColorRes(R.color.colorPrimary)
                .normalColor(Color.WHITE);
    }
}
