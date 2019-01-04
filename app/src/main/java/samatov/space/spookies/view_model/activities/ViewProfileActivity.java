package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.view_model.fragments.ViewProfileFragment;

public class ViewProfileActivity extends BaseActivity {


    @BindView(R.id.viewProfileToolbar) Toolbar mToolbar;
    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);

        setupActionBar(mToolbar, "View profile");
        getCurrentUser();
        replaceFragment(ViewProfileFragment.newInstance(mUser), R.id.viewProfileMainPlaceholder);
    }


    private void getCurrentUser() {
        mUser = MyPreferenceManager
                .getObject(this, MyPreferenceManager.USER_SEARCH_CLICKED_ITEM, User.class);
    }
}
