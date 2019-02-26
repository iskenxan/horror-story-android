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
import android.widget.ViewSwitcher;

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
import samatov.space.spookies.model.post.ChatSettingsListener;
import samatov.space.spookies.model.post.Message;
import samatov.space.spookies.model.post.NarratorMessage;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.fragments.post.message_viewholder.MessageholderHelper;
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
    @BindView(R.id.editPostFragmentTitleEditText) EditText mTitleEditText;
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
    @BindView(R.id.editPostInputContainer) ConstraintLayout mInputContainer;
    @BindView(R.id.editPostFragmentTitleSwitcher) ViewSwitcher mTitleSwitcher;
    @BindView(R.id.editPostFragmentTitleTextView) TextView mTitleTextView;



    private EditPostActivity mActivity;
    private EditPostDialogHandler mSettingsDialogHandler;
    private ArrayAdapter<String> mSpinnerAdapter;
    private MessagesListAdapter<Message> mMessageListAdapter;
    private Post mPost;
    private POST_TYPE mPostType;
    private String mCurrentName;



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
        mPost.addCharacterIfNull("Narrator", 2);
    }


    private void setupCharColors() {
        MyPreferenceManager.saveString(getContext(),
                MyPreferenceManager.FIRST_CHARACTER_COLOR, mPost.getCharacterColor(0));
        MyPreferenceManager.saveString(getContext(),
                MyPreferenceManager.SECOND_CHARACTER_COLOR, mPost.getCharacterColor(1));
    }


    private void setupViews() {
        setupForPublished();
        if (!Validator.isNullOrEmpty(mPost.getTitle()))
            mTitleEditText.setText(mPost.getTitle());
        mSettingsDialogHandler = new EditPostDialogHandler(mActivity,this);
        mMessageInput.setInputListener(this);
        mDeleteMessageImageView.setVisibility(View.INVISIBLE);
        setCharacterNames();
        setCharacterColors();

        setupSpinner();
        setupMessageList();
    }


    private void setCharacterNames() {
        String name = mPost.getCharacterName(0);
        setCharacterName(0, name);
        name = mPost.getCharacterName(1);
        setCharacterName(1, name);
    }


    private void setCharacterColors() {
        setCharacterColor(0, mPost.getCharacterColor(0));
        setCharacterColor(1, mPost.getCharacterColor(1));
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
            mTitleSwitcher.showNext();
            mTitleTextView.setText(mPost.getTitle());
        }
    }


    private void setupSpinner() {
        mSpinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, mPost.getCharacterNameList());
        mUserSpinner.setAdapter(mSpinnerAdapter);
    }


    private void setupMessageList() {
        mMessageListAdapter = MessageholderHelper.Companion.getMessageListAdapter();
        mMessageList.setAdapter(mMessageListAdapter);
        mMessageList.getLayoutManager().setAutoMeasureEnabled(false);
        mMessageList.setHasFixedSize(true);
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
        mPost.setTitle(mTitleEditText.getText() + "");
        if (!titleEmpty())
            mActivity.saveDraft(mPost);
    }


    @OnClick(R.id.editPostPublishButton)
    public void onPublishButtonClicked() {
        mPost.setTitle(mTitleEditText.getText() + "");
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
        startSettingsDialog(0);
    }


    @OnClick(R.id.editPostFragmentSecondCharacterEditIcon)
    public void onSecondSettingsClick() {
        startSettingsDialog(1);
    }


    private void startSettingsDialog(int characterId) {
        mCurrentName = mPost.getCharacterName(characterId);
        String color = mPost.getCharacterColor(characterId);
        DialogPlus settingsDialog = mSettingsDialogHandler.getDialog(mCurrentName, color, characterId);
        settingsDialog.show();
    }


    @Override
    public void onChatSettingsChanged(String name, String color, int characterId) {
        if (!validSettingsChange(name))
            return;
        onCharacterNameChanged(name, characterId);
        onCharacterColorChanged(color, name, characterId);
    }


    private void onCharacterColorChanged(String color, String name, int characterId) {
        if (!Validator.isNullOrEmpty(color)) {
            mPost.addCharacterIfNull(name, characterId);
            mPost.changeCharacterSettings(name, "color", color);
            setCharacterColor(characterId, color);
            if (characterId == 0)
                MyPreferenceManager.saveString(mActivity, MyPreferenceManager.FIRST_CHARACTER_COLOR, color);
            else if (characterId == 1)
                MyPreferenceManager.saveString(mActivity, MyPreferenceManager.SECOND_CHARACTER_COLOR, color);
        }
    }


    private void onCharacterNameChanged(String name, int characterId) {
        mPost.addCharacterIfNull(name, characterId);
        setCharacterName(characterId, name);
        resetSpinner();
    }


    private boolean validSettingsChange(String name) {
        if (Validator.isNullOrEmpty(name))
            return false;
        if ((!Validator.isNullOrEmpty(mCurrentName) && mCurrentName.equals(name)))
            return true;
        if (mPost.getCharacterNameList().contains(name)) {
            showWarningDialog("Character with that name already exists", "Name is taken");
            return false;
        }

        return true;
    }


    private void setCharacterName(int characterId, String newName) {
        if (newName == null)
            return;

        mPost.setCharacterName(newName, characterId);
        if (!Validator.isNullOrEmpty(mCurrentName) && !newName.equals(mCurrentName)){
            mPost.updateMessagesAuthorName(mCurrentName, newName);
            mMessageListAdapter.clear(true);
            addCurrentPostMessages();
        }

        TextView textView = characterId == 0 ? mFirstCharacterTextView : mSecondCharacterTextView;
        textView.setText(newName);
    }


    private void resetSpinner() {
        mSpinnerAdapter.clear();
        List<String> userNames = mPost.getCharacterNameList();
        for (String name: userNames)
            mSpinnerAdapter.add(name);
    }


    private void setCharacterColor(int characterId, String newColor) {
        ImageView imageView = characterId == 0 ? mFirstCharacterColorImageView : mSecondCharacterColorImageView;
        GradientDrawable drawable = (GradientDrawable) imageView.getDrawable();
        drawable.setColor(Color.parseColor(newColor));
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

        String characterName = mUserSpinner.getSelectedItem().toString();


        int characterId = mPost.getCharacterId(characterName);
        message.setAuthor(characterName, characterId);

        if (characterName.equals("Narrator"))
            message = new NarratorMessage(message);

        mMessageListAdapter.addToStart(message, true);
        mPost.addMessage(message);

        return true;
    }


    private void showWarningDialog(String text, String title) {
        SweetAlertDialog dialog = DialogFactory
                .getAlertDialog(mActivity, title, text);
        dialog.show();
    }
}
