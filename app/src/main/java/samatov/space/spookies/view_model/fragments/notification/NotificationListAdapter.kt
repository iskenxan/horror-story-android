package samatov.space.spookies.view_model.fragments.notification

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class NotificationListAdapter: RecyclerView.Adapter<NotificationListViewholder>() {

    override fun getItemCount(): Int {
        return 0
    }


    override fun onBindViewHolder(holder: NotificationListViewholder, position: Int) {

    }


    override fun onCreateViewHolder(view: ViewGroup, position: Int): NotificationListViewholder {
        return NotificationListViewholder(view)
    }
}