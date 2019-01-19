package samatov.space.spookies.view_model.fragments.feed_viewpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import samatov.space.spookies.model.api.beans.Post


class FeedViewPagerAdapter(fm: FragmentManager, timeline: List<Post>, popular: List<Post>)
    : FragmentStatePagerAdapter(fm) {

    private var timeline: List<Post> = timeline
    private var popular: List<Post> = popular


    override fun getItem(p0: Int): Fragment =
            if (p0 == 0)
                FeedListFragment.newInstance(timeline, FeedType.TIMELINE)
            else
                FeedListFragment.newInstance(popular, FeedType.POPULAR)


    override fun getCount(): Int = 2


    override fun getPageTitle(position: Int): CharSequence? =
            if (position == 0) "My feed" else "Popular"
}