package samatov.space.spookies.view_model.fragments.post.message_viewholder

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import samatov.space.spookies.R
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.post.Message

abstract class MessageholderHelper {


    companion object {
        fun setupMessageholder(preferenceKey: String, listener: SharedPreferences.OnSharedPreferenceChangeListener, view: View, type: Message.TYPE) {
            val color = MyPreferenceManager.getString(view.context, preferenceKey)
            setBubbleColor(Color.parseColor(color), view, type)
            setupSharedPreferenceListener(listener, view)
        }


        private fun setupSharedPreferenceListener(listener: SharedPreferences.OnSharedPreferenceChangeListener, view: View) {
            MyPreferenceManager.addSharedPreferenceListener(view.context, listener)
        }


        fun onCharacterColorPreferenceChange(preferenceKey: String?, isSelected: Boolean, view: View, type: Message.TYPE) {
            if (!isSelected) {
                val color = MyPreferenceManager.getString(view.context, preferenceKey)
                setBubbleColor(Color.parseColor(color), view, type)
            } else {
                val color = R.color.colorSecondary
                setBubbleColor(color, view, type)
            }
        }


        fun setBubbleColor(color: Int, view: View, type: Message.TYPE) {
            val bubbleView = view.findViewById<ViewGroup>(com.stfalcon.chatkit.R.id.bubble)
            val resourceId = if (type ==Message.TYPE.outcoming) com.stfalcon.chatkit.R.drawable.shape_outcoming_message
                    else com.stfalcon.chatkit.R.drawable.shape_incoming_message

            val bg = view.context.resources.getDrawable(resourceId)
            val gradientBg = bg as GradientDrawable
            gradientBg.setColor(color)
            ViewCompat.setBackground(bubbleView, gradientBg)
        }


        fun adjustOnBind(time: TextView, text:TextView, message: Message, isSelected: Boolean) {
            time.text = message?.user?.name
            if (isSelected)
                text.setTextColor(text.resources.getColor(R.color.colorSecondary))
            else
                text.setTextColor(text.resources.getColor(android.R.color.white))
        }
    }
}