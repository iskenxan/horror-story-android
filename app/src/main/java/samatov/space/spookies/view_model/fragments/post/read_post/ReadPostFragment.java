package samatov.space.spookies.view_model.fragments.post.read_post;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.orhanobut.dialogplus.DialogPlus;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Completable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.post.Message;
import samatov.space.spookies.view_model.activities.ReadPostActivity;
import samatov.space.spookies.view_model.fragments.post.MyOutcomingMessageViewHolder;
import samatov.space.spookies.view_model.utils.DialogFactory;


public class ReadPostFragment extends Fragment {

    public ReadPostFragment() {}


    public static ReadPostFragment newInstance() {
        ReadPostFragment fragment = new ReadPostFragment();

        return fragment;
    }


    @BindView(R.id.readPostTipContainer) LinearLayout mTipContainer;
    @BindView(R.id.readPostMessageList) MessagesList mMessageList;

    ReadPostActivity mActivity;
    DialogPlus mBottomDialog;
    Post mPost;
    MessagesListAdapter<Message> mMessageListAdapter;
    List<Message> mMessages;
    int mMessageCounter = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_post, container, false);
        ButterKnife.bind(this, view);

        mActivity = (ReadPostActivity) getActivity();
        mPost = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_POST, Post.class);
        setupViews();

        return view;
    }


    private void setupViews() {
        try {
            setupMessageList();
        } catch (Exception e) { e.printStackTrace(); }
    }


    private void setupMessageList() throws Exception {
        MyPreferenceManager.saveString(getContext(), MyPreferenceManager.CURRENT_CHAT_BUBBLE_COLOR, mPost.getChatBubbleColor());
        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setOutcoming(MyOutcomingMessageViewHolder.class, com.stfalcon.chatkit.R.layout.item_outcoming_text_message);
        mMessageListAdapter = new MessagesListAdapter<>("0", holdersConfig, null);
        mMessageList.setAdapter(mMessageListAdapter);
        mMessageListAdapter.setOnMessageViewClickListener((a, b) -> checkAndAddNextMessage());
        mMessages = mPost.getSortedDialog();
    }


    @OnClick(R.id.readPostRootContainer)
    public void onScreenClick() {
        mTipContainer.setVisibility(View.GONE);
        if (mMessageList.getVisibility() == View.GONE)
            mMessageList.setVisibility(View.VISIBLE);
        checkAndAddNextMessage();
    }


    private void checkAndAddNextMessage() {
      if (mMessageCounter < mMessages.size()) {
          mMessageListAdapter.addToStart(mMessages.get(mMessageCounter), true);
          mMessageCounter ++;
      } else {
          mBottomDialog = DialogFactory.getDialogPlus
                  (getContext(), false, R.layout.read_post_dialog, Gravity.BOTTOM);
          View view = mBottomDialog.getHolderView();
          setupDialogViewListeners(view);
          mBottomDialog.show();
      }
    }


    private void setupDialogViewListeners(View view) {
        TextView backTextView = view.findViewById(R.id.readPostDialogBackTextView);
        backTextView.setOnClickListener(getOnBackClickedListener());

        ShineButton favoriteButton = view.findViewById(R.id.readPostDialogFavoriteButton);
        favoriteButton.init(mActivity);
        if (inFavorites())
            favoriteButton.setChecked(true);
        favoriteButton.setOnClickListener(getOnFavoriteClickedListener());

        ImageView commentImageView = view.findViewById(R.id.readPostDialogCommendImageView);
        commentImageView.setOnClickListener(getOnCommentClickedListener());
    }


    private View.OnClickListener getOnBackClickedListener() {
        return (view) -> {
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST);
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST_ID);
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST_AUTHOR);
            mActivity.finishAfterTransition();
        };
    }


    private View.OnClickListener getOnCommentClickedListener() {
        return (view) -> {
            mBottomDialog.dismiss();
            mActivity.startCommentFragment();
        };
    }


    private View.OnClickListener getOnFavoriteClickedListener() {
        return (view) -> {
            ShineButton favoriteButton = (ShineButton) view;
            favoriteButton.setEnabled(false);
            Completable completable = getCorrectFavoriteCompletable();
            if (completable == null) {
                onRequestError();
                return;
            }
            mActivity.listenToCompletable(completable, (result, exception) ->
                    onRequestResult(favoriteButton, exception));
        };
    }


    private Completable getCorrectFavoriteCompletable() {
        String authorUsername = MyPreferenceManager.getString(getContext(), MyPreferenceManager.CURRENT_POST_AUTHOR);
        if (authorUsername == null)
            return null;
        Completable observable =
                Post.addToFavorite(authorUsername, mPost.getId(), mPost.getTitle(), getContext());
        if (inFavorites())
            observable = Post.removeFromFavorite(authorUsername, mPost.getId(), getContext());

        return observable;
    }


    private void onRequestResult(ShineButton favoriteButton, Throwable exception) {
        favoriteButton.setEnabled(true);
        if (exception != null) {
            favoriteButton.setChecked(!favoriteButton.isChecked());
            onRequestError();
            return;
        }
        if(!inFavorites())
            addToFavorites();
        else
            removeFromFavorites();
    }


    private void onRequestError() {
        String errorText = "Error processing your request. Please try again later";
        SweetAlertDialog dialog = DialogFactory.getErrorDialog(mActivity, errorText, null);
        dialog.show();
    }


    private boolean inFavorites() {
        User user = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_USER, User.class);
        return mPost.getFavorite().has(user.getUsername());
    }


    private void addToFavorites() {
        User currentUser = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_USER, User.class);
        JsonObject favoriteItem = new JsonObject();
        favoriteItem.addProperty("profileImgUrl", currentUser.getProfileUrl());
        mPost.getFavorite().add(currentUser.getUsername(), favoriteItem);
        MyPreferenceManager.saveObjectAsJson(getContext(), MyPreferenceManager.CURRENT_POST, mPost);

        User viewedUser = MyPreferenceManager.peekViewedUsersStack(getContext());
        if (viewedUser == null)
            return;

        JsonObject postRef = viewedUser.getPublishedRefs().get(mPost.getId());
        if (postRef.has("favorite")) {
            postRef.getAsJsonObject("favorite").add(currentUser.getUsername(), favoriteItem);
        }
        MyPreferenceManager.popViewedUsersStack(getContext());
        MyPreferenceManager.addToViewedUsersStack(getContext(), viewedUser);
    }


    private void removeFromFavorites() {
        User currentUser = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_USER, User.class);
        mPost.getFavorite().remove(currentUser.getUsername());
        MyPreferenceManager.saveObjectAsJson(getContext(), MyPreferenceManager.CURRENT_POST, mPost);

        User viewedUser = MyPreferenceManager.peekViewedUsersStack(getContext());
        if (viewedUser == null)
            return;
        viewedUser.getPublishedRefs().get(mPost.getId()).getAsJsonObject("favorite").remove(currentUser.getUsername());
        MyPreferenceManager.popViewedUsersStack(getContext());
        MyPreferenceManager.addToViewedUsersStack(getContext(), viewedUser);
    }

}
