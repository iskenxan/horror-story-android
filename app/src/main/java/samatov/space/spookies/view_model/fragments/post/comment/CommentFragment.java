package samatov.space.spookies.view_model.fragments.post.comment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.MessageInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;


public class CommentFragment extends Fragment implements MessageInput.InputListener {

    public CommentFragment() {
        // Required empty public constructor
    }


    public static CommentFragment newInstance(AppCompatActivity activity) {
        CommentFragment fragment = new CommentFragment();
        fragment.setEnterTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_right));
        fragment.setExitTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_right));

        return fragment;
    }

    @BindView(R.id.commentFragmentCommentCountTextView)TextView mCommentCountTextView;
    @BindView(R.id.commentFragmentMessageInput)MessageInput mMessageInput;
    @BindView(R.id.commentFragmentRecyclerView)RecyclerView mRecyclerView;

    Post mPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);
        getPost();
        setupViews();

        return view;
    }


    private void getPost() {
        mPost = MyPreferenceManager.getObject(getContext(), MyPreferenceManager.CURRENT_POST, Post.class);
    }


    private void setupViews() {
        mCommentCountTextView.setText(mPost.getComments().size() + "");
        setupRecyclerView();
        mMessageInput.setInputListener(this);
    }


    private void setupRecyclerView() {

    }


    // on new comment submit
    @Override
    public boolean onSubmit(CharSequence input) {
        String text = input + "";


        return true;
    }
}
