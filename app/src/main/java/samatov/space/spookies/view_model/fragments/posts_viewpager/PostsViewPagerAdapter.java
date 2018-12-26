package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.enums.POST_TYPE;

public class PostsViewPagerAdapter extends FragmentStatePagerAdapter {

    User mUser;


    public PostsViewPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        mUser = user;
    }


    public void setUser(User user) {
        mUser = user;
    }


    @Override
    public Fragment getItem(int i) {
        if (i == 0)
            return PostsListFragment.newInstance(mUser.getPublishedRefs(), POST_TYPE.PUBLISHED);
        else
            return PostsListFragment.newInstance(mUser.getDraftRefs(), POST_TYPE.DRAFT);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Posts" : "Drafts";
    }
}
