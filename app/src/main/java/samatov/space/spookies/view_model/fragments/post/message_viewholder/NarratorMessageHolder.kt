package samatov.space.spookies.view_model.fragments.post.message_viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.stfalcon.chatkit.messages.MessagesListAdapter
import samatov.space.spookies.R
import samatov.space.spookies.model.post.NarratorMessage

class NarratorMessageHolder(view: View) : MessagesListAdapter.OutcomingMessageViewHolder<NarratorMessage>(view) {
    private val mView = view

    override fun onBind(data: NarratorMessage?) {
        val textView = mView.findViewById<ViewGroup>(R.id.narratorViewTextView) as TextView
        textView.text = data?.text

        if (isSelected){
            textView.setTextColor(mView.resources.getColor(R.color.white))
            textView.setBackgroundColor(mView.resources.getColor(R.color.colorSecondary))
        }
        else {
            textView.setTextColor(mView.resources.getColor(R.color.colorSecondary))
            textView.setBackgroundColor(mView.resources.getColor(R.color.transparent))
        }
    }
}