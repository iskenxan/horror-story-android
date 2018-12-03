package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PostsViewPagerAdapter extends FragmentStatePagerAdapter {

    public PostsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        return PostsListFragment.newInstance();
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
