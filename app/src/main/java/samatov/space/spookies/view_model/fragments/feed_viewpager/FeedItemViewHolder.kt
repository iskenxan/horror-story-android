package samatov.space.spookies.view_model.fragments.feed_viewpager

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_list_item.*
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.Post
import samatov.space.spookies.model.utils.FormatterK
import samatov.space.spookies.model.utils.TimeSince


class FeedItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {


    fun bind (post: Post) {
        val timeSince = TimeSince.getTimeAgo(post.lastUpdated)
        feedListItemTimestampTextView.text = timeSince

        val profileUrl = FormatterK.getUserProfileUrl(post.author)
        feedListItemImageView.setImageResource(R.drawable.ic_profile_placeholder)
        feedListItemUsernameTextview.text = post.author
        feedListItemTitleTextView.text = post.title
        feedListItemFavoriteTextView.text = post.favorite.size().toString()
        feedListItemCommentTextView.text = post.comments.size.toString()


        Picasso.get().load(profileUrl)
                .resize(30, 30)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .into(feedListItemImageView)
    }
}