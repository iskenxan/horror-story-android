package samatov.space.spookies.view_model.fragments.post.message_viewholder

import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import com.stfalcon.chatkit.messages.MessagesListAdapter
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.post.Message

class FirstCharMessageViewHolder(itemView: View?) : MessagesListAdapter.OutcomingMessageViewHolder<Message>(itemView), SharedPreferences.OnSharedPreferenceChangeListener {
    private val mView = itemView

    init {
        MessageholderHelper.setupMessageholder(MyPreferenceManager.FIRST_CHARACTER_COLOR,
                this, mView!!, Message.TYPE.outcoming)
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
        if (key == null || key !== MyPreferenceManager.FIRST_CHARACTER_COLOR)
            return

        MessageholderHelper.onCharacterColorPreferenceChange(key, isSelected, mView!!, Message.TYPE.outcoming)
    }


    override fun onBind(message: Message?) {
        super.onBind(message)
        val color = getColor()
        MessageholderHelper.setBubbleColor(Color.parseColor(color), mView!!, Message.TYPE.outcoming)
        MessageholderHelper.adjustOnBind(time, text, message!!, isSelected)
    }


    private fun getColor(): String {
        if (isSelected)
            return "#FFFFFF"

        return MyPreferenceManager.getString(mView!!.context, MyPreferenceManager.FIRST_CHARACTER_COLOR)
    }

}