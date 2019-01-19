package samatov.space.spookies.view_model.fragments.feed_viewpager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.Post

class FeedListAdapter(posts: List<Post>) : RecyclerView.Adapter<FeedItemViewHolder>() {

    private val posts = posts


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FeedItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_list_item, parent, false)
        return FeedItemViewHolder(view)
    }


    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(posts[position])
    }
}