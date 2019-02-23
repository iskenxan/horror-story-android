package samatov.space.spookies.view_model.fragments.post.messageViewHolder

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import com.stfalcon.chatkit.messages.MessagesListAdapter
import samatov.space.spookies.R
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.post.Message

abstract class BaseMessageHolder(view: View) : MessagesListAdapter.OutcomingMessageViewHolder<Message>(view) {
    protected val mView = view


    protected fun setupMessageholder(preferenceKey: String, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        val color = MyPreferenceManager.getString(mView.context, preferenceKey)
        setBubbleColor(Color.parseColor(color))
        setupSharedPreferenceListener(listener)
    }


    private fun setupSharedPreferenceListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        MyPreferenceManager.addSharedPreferenceListener(mView.context, listener)
    }


    protected fun setBubbleColor(color: Int) {
        val bubbleView = mView.findViewById<ViewGroup>(com.stfalcon.chatkit.R.id.bubble)
        val bg = mView.context.resources.getDrawable(com.stfalcon.chatkit.R.drawable.shape_outcoming_message)
        val gradientBg = bg as GradientDrawable
        gradientBg.setColor(color)
        ViewCompat.setBackground(bubbleView, gradientBg)
    }


    protected fun onCharacterColorPreferenceChange(preferenceKey: String?) {
        if (!isSelected) {
            val color = MyPreferenceManager.getString(mView.context, preferenceKey)
            setBubbleColor(Color.parseColor(color))
        } else {
            val color = R.color.colorSecondary
            setBubbleColor(color)
        }
    }
}