package samatov.space.spookies.view_model.fragments.edit_post;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.thebluealliance.spectrum.SpectrumDialog;

import samatov.space.spookies.R;
import samatov.space.spookies.model.edit_post.ChatSettingsListener;

public class EditPostDialogHandler {

    private AppCompatActivity mActivity;
    private ChatSettingsListener mListener;
    private View mContainerView;
    private String mName;
    private String mColor;
    private String mTitle;


    public EditPostDialogHandler(AppCompatActivity activity, ChatSettingsListener listener) {
        mActivity = activity;
        mListener = listener;
    }


    public DialogPlus getDialog( String nameValue, String colorValue, String title) {
        DialogPlus dialogPlus = DialogPlus.newDialog(mActivity)
                .setContentHolder(new ViewHolder(R.layout.dialog_edit_post_settings))
                .setGravity(Gravity.CENTER)
                .setPadding(36, 26, 36 ,26)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(getOnClickListener())
                .create();
        mContainerView = dialogPlus.getHolderView();
        setupInitialValues(nameValue, colorValue, title);

        return dialogPlus;
    }


    private OnClickListener getOnClickListener() {
        return (dialog, view) -> {
            if (view.getId() == R.id.chatSettingsSaveButton) {
                mName = getNameInput();
                mTitle = getTitleInput();

                mListener.onChatSettingsChanged(mName, mColor, mTitle);
                dialog.dismiss();
            }
            if (view.getId() == R.id.chatSettingsPickColorButton) {
                getColorPickerDialog(view, mActivity)
                        .show(mActivity.getSupportFragmentManager(), "color_picker_dialog");
            }
        };
    }


    private void setupInitialValues( String nameValue, String colorValue, String title) {
        EditText chattingWithEditText = mContainerView.findViewById(R.id.chatSettingsChatWithEditText);
        chattingWithEditText.setText(nameValue);

        EditText titleEditText = mContainerView.findViewById(R.id.chatSettingsTitleEditText);
        titleEditText.setText(title);

        int colorInt = Color.parseColor(colorValue);
        setButtonColor(mContainerView, colorInt);
    }


    private String getNameInput() {
        EditText chattingWithEditText = mContainerView.findViewById(R.id.chatSettingsChatWithEditText);
        String chattingWith = chattingWithEditText.getText() + "";

        return chattingWith;
    }


    private String getTitleInput() {
        EditText titleEditText = mContainerView.findViewById(R.id.chatSettingsTitleEditText);
        String title = titleEditText.getText() + "";


        return title;
    }


    private SpectrumDialog getColorPickerDialog(View view, AppCompatActivity activity) {
        return new SpectrumDialog.Builder(activity)
                .setColors(R.array.picker_colors)
                .setSelectedColorRes(R.color.colorPrimary)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(0)
                .setOnColorSelectedListener((positiveResult, color) -> {
                    if (positiveResult) {
                        setButtonColor(view, color);
                        mColor = String.format("#%s", Integer.toHexString(color).toUpperCase());
                    }
                }).build();
    }


    private static void setButtonColor(View view, int color) {
        Button colorButton = view.findViewById(R.id.chatSettingsPickColorButton);
        Drawable bgDrawable = colorButton.getBackground();
        GradientDrawable gradientDrawable = (GradientDrawable) bgDrawable;
        gradientDrawable.setColor(color);
    }

}