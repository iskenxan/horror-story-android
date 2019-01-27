package samatov.space.spookies.view_model.dialogs.user_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import samatov.space.spookies.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {

    List<String> mUserList;
    UserListItemClickedListener mListener;


    public UserListAdapter(List<String> userList, UserListItemClickedListener listItemClickedListener) {
        mListener = listItemClickedListener;
        mUserList = userList;
    }


    @NonNull
    @Override
    public UserListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_list_item, viewGroup, false);

        return new UserListItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserListItemViewHolder userListItemViewHolder, int i) {
        String username = mUserList.get(i);
        userListItemViewHolder.bind(username, mListener);
    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
