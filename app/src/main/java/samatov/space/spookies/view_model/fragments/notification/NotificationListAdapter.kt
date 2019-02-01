package samatov.space.spookies.view_model.fragments.notification

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import samatov.space.spookies.R
import samatov.space.spookies.model.api.beans.notification.NotificationActivity

class NotificationListAdapter(activities: List<NotificationActivity>,
                              onClickListener: (NotificationActivity) -> Unit): RecyclerView.Adapter<NotificationListViewholder>() {

    val activities = activities
    val listener = onClickListener

    override fun getItemCount(): Int {
        return activities.size
    }


    override fun onBindViewHolder(holder: NotificationListViewholder, position: Int) {
        holder.bind(activities[position], listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): NotificationListViewholder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_list_item, parent, false)

        return NotificationListViewholder(view)
    }
}