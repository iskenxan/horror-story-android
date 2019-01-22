package samatov.space.spookies.view_model.fragments.feed_viewpager


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_feed_list.*
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.FeedItem
import samatov.space.spookies.view_model.activities.FeedActivity
import samatov.space.spookies.view_model.fragments.BaseFragment


class FeedListFragment : BaseFragment() {


    var mAdapter: FeedListAdapter? = null
    var mPosts: List<FeedItem>? = null
    var mType: FeedType? = null
    var mActivity: FeedActivity? = null


    companion object {

        fun newInstance(feed: List<FeedItem>, type: FeedType): FeedListFragment {
            val fragment = FeedListFragment()
            val args = Bundle()
            val gson = GsonBuilder().serializeNulls().create()

            args.putString("type", type.toString())
            args.putString("feed", gson.toJson(feed))

            fragment.arguments = args

            return fragment
        }
    }
    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getArguments(arguments)
        mActivity = activity as FeedActivity?
        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getArguments(arguments: Bundle?) {
        val typeStr = arguments?.getString("type")
        var feedStr = arguments?.getString("feed")

        mType = typeStr?.let { FeedType.valueOf(it) }

        val listType = object : TypeToken<List<FeedItem>>() {}.type
        mPosts = feedStr?.let { GsonBuilder().serializeNulls().create().fromJson(feedStr, listType) }
    }


    private fun setupRecyclerView() {
        feedListRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = FeedListAdapter(mPosts!!, getItemClickedListener())
        feedListRecyclerView.adapter = mAdapter
    }


    private fun getItemClickedListener(): (FeedClickedOn, Any?) -> Unit {
        return { clickedOn, value ->
            when (clickedOn) {
                FeedClickedOn.USERNAME ->
                    mActivity?.getUserAndStartViewProfileActivity(value as String, false)
                FeedClickedOn.READ_BTN ->
                    mActivity?.startReadPostActivity(value as FeedItem)
                FeedClickedOn.COMMENTS ->
                    mActivity?.startReadCommentsFragment(value as FeedItem)
            }
        }
    }
}
