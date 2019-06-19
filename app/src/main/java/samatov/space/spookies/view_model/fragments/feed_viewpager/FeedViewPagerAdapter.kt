package samatov.space.spookies.view_model.fragments.feed_viewpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import samatov.space.spookies.model.api.beans.FeedItem


class FeedViewPagerAdapter(fm: FragmentManager, timeline: List<FeedItem>, popular: List<FeedItem>, new: List<FeedItem>)
    : FragmentStatePagerAdapter(fm) {

    private var timeline: List<FeedItem> = timeline
    private var popular: List<FeedItem> = popular
    private var new: List<FeedItem> = new


    override fun getItem(p0: Int): Fragment =
            if (p0 == 0)
                FeedListFragment.newInstance(timeline, FeedType.TIMELINE)
            else if (p0 == 1)
                FeedListFragment.newInstance(popular, FeedType.POPULAR)
            else
                FeedListFragment.newInstance(new, FeedType.NEW)


    override fun getCount(): Int = 3


    override fun getPageTitle(position: Int): CharSequence? =
            when {
                   position == 0 -> "My Feed"
                   position == 1 -> "Popular"
                else -> "New"
            }
}
