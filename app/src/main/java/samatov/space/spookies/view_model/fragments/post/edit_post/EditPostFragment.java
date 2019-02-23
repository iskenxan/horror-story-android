package samatov.space.spookies.view_model.fragments.post.edit_post;

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
import android.widget.EditText;
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
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.model.post.Author;
import samatov.space.spookies.model.post.ChatSettingsListener;
import samatov.space.spookies.model.post.Message;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.fragments.post.messageViewHolder.FirstCharMessageViewHolder;
import samatov.space.spookies.view_model.fragments.post.messageViewHolder.SecondCharMessageViewHolder;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class EditPostFragment extends Fragment implements ChatSettingsListener, MessageInput.InputListener {


    public EditPostFragment() {
        // Required empty public constructor
    }


    public static EditPostFragment newInstance() {
        EditPostFragment fragment = new EditPostFragment();

        return fragment;
    }


    @BindView(R.id.editPostFragmentFistCharacterTextView) TextView mFirstCharacterTextView;
    @BindView(R.id.editPostFragmentSecondCharacterTextView) TextView mSecondCharacterTextView;
    @BindView(R.id.editPostFragmentTitleEditText) EditText mTitleTextView;
    @BindView(R.id.editPostFragmentFirstCharacterColor) ImageView mFirstCharacterColorImageView;
    @BindView(R.id.editPostFragmentSecondCharacterColor) ImageView mSecondCharacterColorImageView;
    @BindView(R.id.editPostUserSpinner) Spinner mUserSpinner;
    @BindView(R.id.editPostMessageList) MessagesList mMessageList;
    @BindView(R.id.editPostMessageInput) MessageInput mMessageInput;
    @BindView(R.id.editPostDeleteMessagesImageView) ImageView mDeleteMessageImageView;
    @BindView(R.id.editPostSaveDraftButton) Button mSaveDraftButton;
    @BindView(R.id.editPostPublishButton) Button mPublishButton;
    @BindView(R.id.editPostDeletePostTextView) TextView mDeletePostTextView;
    @BindView(R.id.editPostUnpublishTextView) TextView mUnpublishTextView;
    @BindView(R.id.editPostFragmentFirstCharacterEditIcon) ImageView mFirstCharacterEditIcon;
    @BindView(R.id.editPostFragmentSecondCharacterEditIcon) ImageView mSecondCharacterEditIcon;
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
        setupCharColors();
    }


    private void setupForCurrentPost() {
        mPost = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_POST, Post.class);
        mPostType = MyPreferenceManager.getObject(getContext(),
                MyPreferenceManager.CURRENT_POST_TYPE, POST_TYPE.class);

        if (mPost == null)
            mPost = new Post();
    }


    private void setupCharColors() {
        MyPreferenceManager.saveString(getContext(),
                MyPreferenceManager.FIRST_CHARACTER_COLOR, mPost.getCharacterColor(1)); // characterId 0 belongs to narrator
        MyPreferenceManager.saveString(getContext(),
                MyPreferenceManager.SECOND_CHARACTER_COLOR, mPost.getCharacterColor(2));
    }


    private void setupViews() {
        setupForPublished();
        if (!Validator.isNullOrEmpty(mPost.getTitle()))
            mTitleTextView.setText(mPost.getTitle());
        mSettingsDialogHandler = new EditPostDialogHandler(mActivity,this);
        mMessageInput.setInputListener(this);
        mDeleteMessageImageView.setVisibility(View.INVISIBLE);
        setCharacterNames();
        setCharacterColors();

        setupSpinner();
        setupMessageList();
    }


    private void setCharacterNames() {
        String name = mPost.getCharacterName(1); // characterId 0 belongs to narrator
        setCharacterName(1, name);
        name = mPost.getCharacterName(2);
        setCharacterName(2, name);
    }


    private void setCharacterColors() {
        setCharacterColor(1, mPost.getCharacterColor(1));
        setCharacterColor(2, mPost.getCharacterColor(2));
    }


    private void setupForPublished() {
        if (mPostType == POST_TYPE.PUBLISHED) {
            mUnpublishTextView.setVisibility(View.VISIBLE);
            mDeletePostTextView.setVisibility(View.GONE);
            mPublishButton.setVisibility(View.GONE);
            mSaveDraftButton.setVisibility(View.GONE);
            mFirstCharacterEditIcon.setVisibility(View.GONE);
            mSecondCharacterEditIcon.setVisibility(View.GONE);
            mInputContainer.setVisibility(View.GONE);
        }
    }


    private void setupSpinner() {
        mSpinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, mPost.getCharacterNameList());
        mUserSpinner.setAdapter(mSpinnerAdapter);
    }


    private void setupMessageList() {
        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setOutcoming(FirstCharMessageViewHolder.class, com.stfalcon.chatkit.R.layout.item_outcoming_text_message);
        holdersConfig.setIncoming(SecondCharMessageViewHolder.class, com.stfalcon.chatkit.R.layout.item_incoming_text_message);
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


    @OnClick(R.id.editPostFragmentFirstCharacterEditIcon)
    public void onFirstSettingsClick() {
        startSettingsDialog(1);
    }


    @OnClick(R.id.editPostFragmentSecondCharacterEditIcon)
    public void onSecondSettingsClick() {
        startSettingsDialog(2);
    }


    private void startSettingsDialog(int characterId) {
        String chattingWith = mPost.getCharacterName(characterId);
        DialogPlus settingsDialog = mSettingsDialogHandler
                .getDialog(chattingWith, mPost.getCharacterColor(characterId), characterId);
        settingsDialog.show();
    }


    @Override
    public void onChatSettingsChanged(String name, String color, int characterId) {
        if (!Validator.isNullOrEmpty(color)) {
            mPost.changeCharacterSettings(name, "color", color);
            setCharacterColor(characterId, color);
            if (characterId == 1)
                MyPreferenceManager.saveString(mActivity, MyPreferenceManager.FIRST_CHARACTER_COLOR, color);
            else
                MyPreferenceManager.saveString(mActivity, MyPreferenceManager.SECOND_CHARACTER_COLOR, color);
        }

        if (!Validator.isNullOrEmpty(name)) {
//            mChattingWithTextView.setText(name);
//            mChattingWithAuthor.setName(name);
//            resetSpinner(name);
//            mPost.setOtherCharacter(name);
            setCharacterName(characterId, name);
            resetSpinner(name);
        }
    }


    private void resetSpinner(String name) {
        mSpinnerAdapter.clear();
        mSpinnerAdapter.add("User");
        mSpinnerAdapter.add(name);
    }


    private void setCharacterColor(int characterId, String newColor) {
        ImageView imageView = characterId == 1 ? mFirstCharacterColorImageView : mSecondCharacterColorImageView;
        GradientDrawable drawable = (GradientDrawable) imageView.getDrawable();
        drawable.setColor(Color.parseColor(newColor));
    }


    private void setCharacterName(int characterId, String newName) {
        if (newName == null)
            return;

        TextView textView = characterId == 1 ? mFirstCharacterTextView : mSecondCharacterTextView;
        textView.setText(newName);
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
