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
import samatov.space.spookies.model.api.beans.Post
import samatov.space.spookies.view_model.fragments.BaseFragment


class FeedListFragment : BaseFragment() {


    var mAdapter: FeedListAdapter? = null
    var mPosts: List<Post>? = null
    var mType: FeedType? = null


    companion object {

        fun newInstance(feed: List<Post>, type: FeedType): FeedListFragment {
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

        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        feedListTestTextView.text = if (mType == FeedType.TIMELINE) "timeline" else "popular"
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getArguments(arguments: Bundle?) {
        val typeStr = arguments?.getString("type")
        var feedStr = arguments?.getString("feed")

        mType = typeStr?.let { FeedType.valueOf(it) }

        val listType = object : TypeToken<List<Post>>() {}.type
        mPosts = feedStr?.let { GsonBuilder().serializeNulls().create().fromJson(feedStr, listType) }
    }


    fun setupRecyclerView() {
        feedListRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = FeedListAdapter(mPosts!!)
        feedListRecyclerView.adapter = mAdapter
    }
}
