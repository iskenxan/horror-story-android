package samatov.space.spookies.view_model.fragments.edit_post;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.edit_post.Author;
import samatov.space.spookies.model.edit_post.ChatSettingsListener;
import samatov.space.spookies.model.edit_post.Message;
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class EditPostFragment extends Fragment implements ChatSettingsListener, MessageInput.InputListener {

    public static String CURRENT_POST = "current_edit_post";



    public EditPostFragment() {
        // Required empty public constructor
    }


    public static EditPostFragment newInstance() {
        EditPostFragment fragment = new EditPostFragment();

        return fragment;
    }


    @BindView(R.id.editPostChattingWithTextView) TextView mChattingWithTextView;
    @BindView(R.id.editPostFragmentTitleTextView) TextView mTitleTextView;
    @BindView(R.id.editPostColorPlaceholder) ImageView mColorImageView;
    @BindView(R.id.editPostUserSpinner) Spinner mUserSpinner;
    @BindView(R.id.editPostMessageList) MessagesList mMessageList;
    @BindView(R.id.editPostMessageInput) MessageInput mMessageInput;
    @BindView(R.id.editPostDeleteMessagesImageView) ImageView mDeleteMessageImageView;
    @BindView(R.id.editPostSaveDraftButton) Button mSaveDraftButton;
    @BindView(R.id.editPostPublishButton) Button mPublishButton;
    @BindView(R.id.editPostDeletePostTextView) TextView mDeletePostTextView;
    @BindView(R.id.editPostUnpublishTextView) TextView mUnpublishTextView;
    @BindView(R.id.editPostSettingsImageView) ImageView mSettingsImageView;
    @BindView(R.id.editPostInputContainer)ConstraintLayout mInputContainer;



    private EditPostActivity mActivity;
    private EditPostDialogHandler mSettingsDialogHandler;
    private ArrayAdapter<String> mSpinnerAdapter;
    private MessagesListAdapter<Message> mMessageListAdapter;
    private Author mChattingWithAuthor = new Author("1", null);
    private Author mUserAuthor = new Author("0", "User");
    private Post mPost;
    private POST_TYPE mPostType;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
       ButterKnife.bind(this, view);
       mActivity = (EditPostActivity) getActivity();

       setupModel();
       setupViews();

       return view;
    }


    private void setupModel() {
        setupForCurrentPost();
        MyPreferenceManager.saveString(getContext(), "current_bubble_color", mPost.getChatBubbleColor());
    }


    private void setupForCurrentPost() {
        mPost = (Post) MyPreferenceManager.getObject(getContext(), CURRENT_POST, Post.class);
        mPostType = (POST_TYPE) MyPreferenceManager.getObject(getContext(),
                EditPostActivity.CURRENT_POST_TYPE, POST_TYPE.class);

        if (mPost == null) {
            mPost = new Post();
            return;
        }
        setupOtherCharacterName();
        if (!Validator.isNullOrEmpty(mPost.getTitle()))
            mTitleTextView.setText(mPost.getTitle());
    }


    private void setupOtherCharacterName() {
        String name = mPost.getOtherCharacterName();
        if (Validator.isNullOrEmpty(name))
            return;
        mChattingWithTextView.setText(name);
        mChattingWithAuthor.setName(name);
    }


    private void setupViews() {
        setupForPublished();
        mSettingsDialogHandler = new EditPostDialogHandler(mActivity,this);
        mMessageInput.setInputListener(this);
        mDeleteMessageImageView.setVisibility(View.INVISIBLE);
        updateColorImageView();
        setupSpinner();
        setupMessageList();
    }


    private void setupForPublished() {
        if (mPostType == POST_TYPE.PUBLISHED) {
            mUnpublishTextView.setVisibility(View.VISIBLE);
            mDeletePostTextView.setVisibility(View.GONE);
            mPublishButton.setVisibility(View.GONE);
            mSaveDraftButton.setVisibility(View.GONE);
            mSettingsImageView.setVisibility(View.GONE);
            mInputContainer.setVisibility(View.GONE);
        }
    }


    private void setupSpinner() {
        mSpinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, mPost.getCharacterList());
        mUserSpinner.setAdapter(mSpinnerAdapter);
    }


    private void setupMessageList() {
        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setOutcoming(MyOutcomingMessageViewHolder.class, com.stfalcon.chatkit.R.layout.item_outcoming_text_message);
        mMessageListAdapter = new MessagesListAdapter<>("0", holdersConfig, null);
        mMessageList.setAdapter(mMessageListAdapter);
        setupOnMessageHoldListener();
        addCurrentPostMessages();
    }


    private void addCurrentPostMessages() {
        try {
            List<Message> messages = mPost.getSortedDialog();
            for (Message message : messages)
                mMessageListAdapter.addToStart(message, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupOnMessageHoldListener() {
        if (mPostType == POST_TYPE.PUBLISHED)
            return;

        mMessageListAdapter.enableSelectionMode(count -> {
            if (count > 0) {
                mSaveDraftButton.setVisibility(View.GONE);
                mPublishButton.setVisibility(View.GONE);
                mDeletePostTextView.setVisibility(View.GONE);
                mDeleteMessageImageView.setVisibility(View.VISIBLE);
            } else {
                mSaveDraftButton.setVisibility(View.VISIBLE);
                mPublishButton.setVisibility(View.VISIBLE);
                mDeleteMessageImageView.setVisibility(View.INVISIBLE);
                mDeletePostTextView.setVisibility(View.VISIBLE);
            }
        });
    }


    @OnClick(R.id.editPostUnpublishTextView)
    public void onUnpublishClicked() {
        mActivity.unpublishPost(mPost);
    }


    @OnClick(R.id.editPostDeletePostTextView)
    public void onDeletePostClicked() {
        mActivity.deleteDraft(mPost);
    }


    @OnClick(R.id.editPostSaveDraftButton)
    public void onSaveDraftClicked() {
        if (!titleEmpty())
            mActivity.saveDraft(mPost);
    }


    @OnClick(R.id.editPostPublishButton)
    public void onPublishButtonClicked() {
        if (!titleEmpty() && !notEnoughMessages())
            mActivity.publishPost(mPost);
    }


    public boolean titleEmpty() {
        if (Validator.isNullOrEmpty(mPost.getTitle())) {
            String text = "Please make sure to set the title value in the settings";
            String title = "Title cannot be empty";
            showWarningDialog(text, title);
            return true;
        }

        return false;
    }


    public boolean notEnoughMessages() {
        if (mPost.getDialogCount() < 5) {
            String text = "Your post must have at least 5 messages";
            String title = "Not enough messages";
            showWarningDialog(text, title);
            return true;
        }

        return false;
    }


    private void showWarningDialog(String text, String title) {
        SweetAlertDialog dialog = DialogFactory
                .getAlertDialog(mActivity, title, text);
        dialog.show();
    }


    @OnClick(R.id.editPostDeleteMessagesImageView)
    public void onDeleteClicked() {
        List<Message> messages = mMessageListAdapter.getSelectedMessages();
        for (Message message : messages) {
            mPost.deleteMessage(message.getId());
        }
        mMessageListAdapter.deleteSelectedMessages();
    }


    @OnClick(R.id.editPostSettingsImageView)
    public void onSettingsClick() {
        String chattingWith = getChattingWithValue();
        String title = getTitleValue();
        DialogPlus settingsDialog = mSettingsDialogHandler
                .getDialog(chattingWith, mPost.getChatBubbleColor(),  title);
        settingsDialog.show();
    }


    private String getChattingWithValue() {
        String chattingWith = mChattingWithTextView.getText() + "";
        if (chattingWith.equals("Edit in settings"))
            chattingWith = "";

        return chattingWith;
    }


    private String getTitleValue() {
        String title = mTitleTextView.getText() + "";
        if (title.equals("Edit in settings"))
            title = "";

        return title;
    }


    @Override
    public void onChatSettingsChanged(String name, String color, String title) {
        if (!Validator.isNullOrEmpty(color)) {
            mPost.changeCharacterSettings("User", "color", color);
            updateColorImageView();
            MyPreferenceManager.saveString(getContext(), "current_bubble_color", color);
        }

        if (!Validator.isNullOrEmpty(name)) {
            mChattingWithTextView.setText(name);
            mChattingWithAuthor.setName(name);
            resetSpinner(name);
            mPost.setOtherCharacter(name);
        }

        if (!Validator.isNullOrEmpty(title)) {
            mTitleTextView.setText(title);
            mPost.setTitle(title);
        }
    }


    private void resetSpinner(String name) {
        mSpinnerAdapter.clear();
        mSpinnerAdapter.add("User");
        mSpinnerAdapter.add(name);
    }


    private void updateColorImageView() {
        GradientDrawable drawable = (GradientDrawable) mColorImageView.getDrawable();
        drawable.setColor(Color.parseColor(mPost.getChatBubbleColor()));
    }


    //on new message sent
    @Override
    public boolean onSubmit(CharSequence input) {
        String text = input + "";
        Message message = new Message();
        message.setText(text);
        message.setCreatedAt(new Date());
        String id = UUID.randomUUID().toString();
        message.setId(id);

        if (mUserSpinner.getSelectedItem().equals("User")) {
            message.setAuthor(mUserAuthor);
            mPost.addMessageFromUser(text, id);
        } else {
            message.setAuthor(mChattingWithAuthor);
            mPost.addMessageFromOtherCharacter(text, id);
        }

        mMessageListAdapter.addToStart(message, true);

        return true;
    }
}
