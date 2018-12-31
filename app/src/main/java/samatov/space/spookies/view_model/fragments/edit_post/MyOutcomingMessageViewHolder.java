package samatov.space.spookies.view_model.fragments.edit_post;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.MessagesListAdapter;

import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.edit_post.Message;

public class MyOutcomingMessageViewHolder extends MessagesListAdapter.OutcomingMessageViewHolder<Message> implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View mView;

    public MyOutcomingMessageViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        String color = MyPreferenceManager.getString(mView.getContext(), "current_bubble_color");
        setBubbleColor(Color.parseColor(color));
        setupSharedPreferenceListener();
    }


    private void setupSharedPreferenceListener() {
        MyPreferenceManager.addSharedPreferenceListener(mView.getContext(), this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals("current_bubble_color"))
            return;

        String color = MyPreferenceManager.getString(mView.getContext(), "current_bubble_color");
        setBubbleColor(Color.parseColor(color));
    }


    @Override
    public void onBind(Message message) {
        super.onBind(message);
        if (!isSelected()) {
            String color = MyPreferenceManager.getString(mView.getContext(), "current_bubble_color");
            setBubbleColor(Color.parseColor(color));
        } else {
            int color = R.color.colorSecondary;
            setBubbleColor(color);
        }
    }


    private void setBubbleColor(int color) {
        ViewGroup bubbleView = mView.findViewById(com.stfalcon.chatkit.R.id.bubble);
        Drawable bg = mView.getContext().getResources().getDrawable(com.stfalcon.chatkit.R.drawable.shape_outcoming_message);
        GradientDrawable gradientBg = (GradientDrawable) bg;
        gradientBg.setColor(color);
        ViewCompat.setBackground(bubbleView, gradientBg);
    }


    private void setTextColor(int color) {
        TextView textView = mView.findViewById(com.stfalcon.chatkit.R.id.messageText);
        if (textView != null) {
            textView.setBackgroundColor(color);
        }
    }
}