package samatov.space.spookies.view_model.fragments.feed_viewpager

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_list_item.*
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.FeedItem
import samatov.space.spookies.model.utils.FormatterK
import samatov.space.spookies.model.utils.TimeSince
import samatov.space.spookies.model.utils.Validator


class FeedItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {


    fun bind (post: FeedItem, listener: (clickedOn: FeedClickedOn, value: Any?) -> Unit) {

        val timeSince = TimeSince.getTimeAgo(post.lastUpdated)
        feedListItemTimestampTextView.text = timeSince

        val profileUrl = FormatterK.getUserProfileUrl(post.author ?: "")
        feedListItemImageView.setImageResource(R.drawable.ic_profile_placeholder)
        feedListItemUsernameTextview.text = post.author
        feedListItemTitleTextView.text = post.title
        if (!Validator.isNullOrEmpty(post.preface)) {
            feedListItemPrefaceTextView.visibility = View.VISIBLE
            feedListItemPrefaceTextView.text = post.preface
        } else {
            feedListItemPrefaceTextView.visibility = View.GONE
            feedListItemPrefaceTextView.text = ""
        }
        feedListItemFavoriteTextView.text = post.favoriteCount.toString()
        feedListItemCommentTextView.text = post.commentCount.toString()

        Picasso.get().load(profileUrl)
                .resize(30, 30)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .into(feedListItemImageView)

        feedListitemReadButton.setOnClickListener {
            listener.invoke(FeedClickedOn.READ_BTN, post)
        }

        feedListItemUsernameTextview.setOnClickListener {
            listener.invoke(FeedClickedOn.USERNAME, post.author)
        }


        feedListItemCommentTextView.setOnClickListener {
            listener.invoke(FeedClickedOn.COMMENTS, post)
        }

        feedListItemCommentImageView.setOnClickListener {
            listener.invoke(FeedClickedOn.COMMENTS, post)
        }
    }
}