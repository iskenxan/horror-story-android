package samatov.space.spookies.view_model.fragments.notification

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.notification_list_item.*
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.notification.NotificationActivity
import samatov.space.spookies.model.utils.FormatterK
import samatov.space.spookies.model.utils.TimeSince


class NotificationListViewholder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    val view = itemView


    fun bind(activity: NotificationActivity, listener: (NotificationActivity) -> Unit) {

        notificationItemCircleImageView.setImageResource(R.drawable.ic_profile_placeholder)

        val profileUrl = FormatterK.getUserProfileUrl(activity.actor ?: "")

        var text = ""
        var icon = -1
        activity.verb.let {
            val actor = activity.actor
            when (it) {
                "like" -> {
                    text = "$actor liked your story"
                    icon = R.drawable.favorite_icon_colored
                }
                "comment" -> {
                    text = "$actor commented on your story"
                    icon = R.drawable.comment_icon
                }
                "follow" -> {
                    text = "$actor started following you"
                    icon = R.drawable.icon_follow
                }
            }
        }

        notificationItemIconImageView.setImageResource(icon)
        notificationItemTextView.text = text


        Picasso.get().load(profileUrl)
                .resize(35, 35)
                .error(R.drawable.ic_profile_placeholder)
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(notificationItemCircleImageView)

        notificationItemCircleImageView.setOnClickListener { listener(activity) }
        notificationItemTextView.setOnClickListener { listener(activity) }
        notificationItemIconImageView.setOnClickListener { listener(activity) }
        notificationItemDate.text = TimeSince.getTimeAgo(activity.timestamp)
    }
}