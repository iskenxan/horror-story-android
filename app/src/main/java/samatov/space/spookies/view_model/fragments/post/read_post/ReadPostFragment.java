package samatov.space.spookies.view_model.fragments.post.read_post;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import samatov.space.spookies.model.api.beans.PostRef;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.post.Message;
import samatov.space.spookies.view_model.activities.ReadPostActivity;
import samatov.space.spookies.view_model.fragments.post.message_viewholder.FirstCharMessageViewHolder;
import samatov.space.spookies.view_model.fragments.post.message_viewholder.SecondCharMessageViewHolder;
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
        mPost = MyPreferenceManager.getObject(mActivity, MyPreferenceManager.CURRENT_POST, Post.class);
        mActivity.setMainToolbarTitle(mPost.getTitle());

        setupViews();

        return view;
    }


    private void setupViews() {
        try {
            setupMessageList();
        } catch (Exception e) { e.printStackTrace(); }
    }


    private void setupMessageList() throws Exception {
        MyPreferenceManager.saveString(getContext(),
                MyPreferenceManager.FIRST_CHARACTER_COLOR, mPost.getCharacterColor(1)); // characterId 0 belongs to the narrator
        MyPreferenceManager.saveString(getContext(),
                MyPreferenceManager.SECOND_CHARACTER_COLOR, mPost.getCharacterColor(2));
        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setOutcoming(FirstCharMessageViewHolder.class,
                com.stfalcon.chatkit.R.layout.item_outcoming_text_message);
        holdersConfig.setIncoming(SecondCharMessageViewHolder.class,
                com.stfalcon.chatkit.R.layout.item_incoming_text_message);
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
                  (mActivity, false, R.layout.read_post_dialog, Gravity.BOTTOM);
          View view = mBottomDialog.getHolderView();
          setupDialogViewListeners(view);
          mBottomDialog.show();
      }
    }


    private void setupDialogViewListeners(View view) {
        TextView backTextView = view.findViewById(R.id.readPostDialogBackTextView);
        backTextView.setOnClickListener(getOnBackClickedListener());

        boolean favoriteVisible = setupFavoriteButton(view);
        ImageView commentImageView = view.findViewById(R.id.readPostDialogCommendImageView);
        commentImageView.setOnClickListener(getOnCommentClickedListener());
        if (!favoriteVisible) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 20, 0, 20);
        }
    }


    private boolean setupFavoriteButton(View view) {
        User currentUser = MyPreferenceManager
                .getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        ShineButton favoriteButton = view.findViewById(R.id.readPostDialogFavoriteButton);
        if (!mPost.getAuthor().equals(currentUser.getUsername())) {

            favoriteButton.init(mActivity);
            if (inFavorites())
                favoriteButton.setChecked(true);
            favoriteButton.setOnClickListener(getOnFavoriteClickedListener());

            return true;
        }

        favoriteButton.setVisibility(View.GONE);

        return false;
    }


    private View.OnClickListener getOnBackClickedListener() {
        return (view) -> {
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST);
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
        Completable observable =
                Post.addToFavorite(new PostRef(mPost), getContext());
        if (inFavorites())
            observable = Post.removeFromFavorite(new PostRef(mPost), getContext());

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
        User user = MyPreferenceManager
                .getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        return mPost.getFavorite().has(user.getUsername());
    }


    private void addToFavorites() {
        User currentUser = MyPreferenceManager
                .getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        JsonObject favoriteItem = new JsonObject();
        mPost.getFavorite().add(currentUser.getUsername(), favoriteItem);
        MyPreferenceManager
                .saveString(mActivity, MyPreferenceManager.FAVORITE_ACTION, "add " + mPost.getId());
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_POST, mPost);


        User viewedUser = MyPreferenceManager.peekViewedUsersStack(mActivity);
        if (viewedUser == null || mPost.getAuthor() == null || !mPost.getAuthor().equals(viewedUser.getUsername()))
            return;

        PostRef postRef = viewedUser.getPublishedRefs().get(mPost.getId());
        postRef.getFavorite().add(currentUser.getUsername());
        MyPreferenceManager.popViewedUsersStack(mActivity);
        MyPreferenceManager.addToViewedUsersStack(mActivity, viewedUser);
    }


    private void removeFromFavorites() {
        User currentUser = MyPreferenceManager
                .getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        mPost.getFavorite().remove(currentUser.getUsername());
        MyPreferenceManager
                .saveString(mActivity, MyPreferenceManager.FAVORITE_ACTION, "remove " + mPost.getId());
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_POST, mPost);


        User viewedUser = MyPreferenceManager.peekViewedUsersStack(mActivity);
        if (viewedUser == null || mPost.getAuthor() == null || !mPost.getAuthor().equals(viewedUser.getUsername()))
            return;

        viewedUser.getPublishedRefs().get(mPost.getId()).getFavorite().remove(currentUser.getUsername());
        MyPreferenceManager.popViewedUsersStack(mActivity);
        MyPreferenceManager.addToViewedUsersStack(mActivity, viewedUser);
    }

}
