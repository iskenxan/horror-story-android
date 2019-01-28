package samatov.space.spookies.view_model.dialogs.favorite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.view_model.dialogs.user_list.UserListItemClickedListener;
import samatov.space.spookies.view_model.dialogs.user_list.UserListItemViewHolder;

public class FavoriteListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {

    List<String> mFavoriteList;
    UserListItemClickedListener mListener;

    public FavoriteListAdapter(List<String> favoriteList, UserListItemClickedListener listener) {
        mListener = listener;
        Collections.sort(favoriteList, Collections.reverseOrder());
        mFavoriteList = favoriteList;
    }


    @NonNull
    @Override
    public UserListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_list_item, viewGroup, false);

        return new UserListItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserListItemViewHolder favoriteListItemViewHolder, int i) {
        String username = mFavoriteList.get(i);

        favoriteListItemViewHolder.bind(username, mListener);
    }


    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }
}
