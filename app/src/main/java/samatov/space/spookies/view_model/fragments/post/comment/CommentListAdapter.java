package samatov.space.spookies.view_model.fragments.post.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.Comment;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListItemViewholder> {

    List<Comment> mComments;

    public CommentListAdapter(List<Comment> comments) {
        mComments = comments;
    }



    @NonNull
    @Override
    public CommentListItemViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_layout, viewGroup, false);

        return new CommentListItemViewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentListItemViewholder commentListItemViewholder, int position) {
        Comment comment = mComments.get(position);
        commentListItemViewholder.bind(comment);
    }


    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
