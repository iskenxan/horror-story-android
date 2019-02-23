package samatov.space.spookies.view_model.fragments.post.messageViewHolder

import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.post.Message

class SecondCharMessageViewHolder(view: View) : BaseMessageHolder(view), SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        setupMessageholder(MyPreferenceManager.SECOND_CHARACTER_COLOR, this)
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
        if (key == null || key !== MyPreferenceManager.SECOND_CHARACTER_COLOR)
            return

        onCharacterColorPreferenceChange(key)
    }


    override fun onBind(message: Message?) {
        super.onBind(message)
        val color = MyPreferenceManager.getString(mView.context, MyPreferenceManager.SECOND_CHARACTER_COLOR)
        setBubbleColor(Color.parseColor(color))
    }
}