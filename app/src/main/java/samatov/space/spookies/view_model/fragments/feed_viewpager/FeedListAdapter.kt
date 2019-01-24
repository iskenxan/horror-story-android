package samatov.space.spookies.view_model.fragments.feed_viewpager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.FeedItem

class FeedListAdapter(posts: List<FeedItem>, clickedListener: (clickedOn: FeedClickedOn, value: Any?) -> Unit )
    : RecyclerView.Adapter<FeedItemViewHolder>() {

    private var posts = posts
    private val listener = clickedListener


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FeedItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_list_item, parent, false)
        return FeedItemViewHolder(view)
    }

    fun refreshList(newList: List<FeedItem>) {
        posts = newList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(posts[position], listener)
    }
}