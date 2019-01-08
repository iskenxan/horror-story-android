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

import com.orhanobut.dialogplus.DialogPlus;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
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
        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setOutcoming(MyOutcomingMessageViewHolder.class, com.stfalcon.chatkit.R.layout.item_outcoming_text_message);
        mMessageListAdapter = new MessagesListAdapter<>("0", holdersConfig, null);
        mMessageList.setAdapter(mMessageListAdapter);
        mMessageListAdapter.setOnMessageViewClickListener((a, b) -> {
            checkAndAddNextMessage();
        });
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
          DialogPlus bottomDialog = DialogFactory.getDialogPlus
                  (getContext(), false, R.layout.read_post_dialog, Gravity.BOTTOM);
          View view = bottomDialog.getHolderView();
          setupDialogViewListeners(view);
          bottomDialog.show();
      }
    }


    private void setupDialogViewListeners(View view) {
        TextView backTextView = view.findViewById(R.id.readPostDialogBackTextView);
        backTextView.setOnClickListener((v) -> {
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST);
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST_ID);
            MyPreferenceManager.delete(mActivity, MyPreferenceManager.CURRENT_POST_AUTHOR);
            mActivity.finishAfterTransition();
        });
        ShineButton favoriteButton = view.findViewById(R.id.readPostDialogFavoriteButton);
        favoriteButton.init(mActivity);
        ImageView commentImageView = view.findViewById(R.id.readPostDialogCommendImageView);
    }

}
