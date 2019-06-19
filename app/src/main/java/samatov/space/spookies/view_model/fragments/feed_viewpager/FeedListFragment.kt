package samatov.space.spookies.view_model.fragments.feed_viewpager


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_feed_list.*
import samatov.space.spookies.R
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.api.beans.FeedItem
import samatov.space.spookies.model.utils.SerializerK
import samatov.space.spookies.view_model.activities.FeedActivity
import samatov.space.spookies.view_model.fragments.BaseFragment


class FeedListFragment : BaseFragment() {


    private var mAdapter: FeedListAdapter? = null
    private var mPosts: List<FeedItem>? = null
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


    private fun getArguments(arguments: Bundle?) {
        val typeStr = arguments?.getString("type")
        var feedStr = arguments?.getString("feed")

        mType = typeStr?.let { FeedType.valueOf(it) }

        mPosts = feedStr?.let { SerializerK.fromJson(feedStr) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSwipeRefreshListener()
        setupRecyclerView()
        addMyPreferenceListener()

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupSwipeRefreshListener() {
        feedListSwipeRefresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary),
                resources.getColor(R.color.colorAccent))
        feedListSwipeRefresh.setOnRefreshListener {
            feedListItemEmptyContainer.visibility = View.GONE
            mActivity?.fetchFeedUpdated(mType) { result, _ ->
                mPosts = result as List<FeedItem>
                mAdapter?.refreshList(mPosts as List<FeedItem>)
                feedListSwipeRefresh.isRefreshing = false
                mPosts?.let { if (it.isEmpty()) feedListItemEmptyContainer.visibility = View.VISIBLE }
            }
        }
    }


    private fun setupRecyclerView() {
        feedListRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = FeedListAdapter(mPosts!!, getItemClickedListener())
        feedListRecyclerView.adapter = mAdapter
        mPosts?.let { if (it.isEmpty()) feedListItemEmptyContainer.visibility = View.VISIBLE }
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


    private fun addMyPreferenceListener() {
        MyPreferenceManager.addSharedPreferenceListener(mActivity) { _, key ->
            if (key == MyPreferenceManager.FAVORITE_ACTION) {
                val action = MyPreferenceManager.getString(mActivity, MyPreferenceManager.FAVORITE_ACTION)
                val actionArray = action.split(" ")
                val type = actionArray[0]
                val postId = actionArray[1]
                findFeedItem(postId)?.let {
                    if (type == "add")
                        it.favoriteCount += 1
                    else
                        it.favoriteCount -= 1
                }
                mAdapter?.notifyDataSetChanged()
            }
        }
    }


    private fun findFeedItem(postId: String) : FeedItem? {
        mPosts?.let {
            for (item in it) {
                if (item.id == postId)
                    return item
            }
        }

        return null
    }
}
