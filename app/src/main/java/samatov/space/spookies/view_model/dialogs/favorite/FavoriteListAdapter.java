package samatov.space.spookies.view_model.dialogs.favorite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import samatov.space.spookies.R;
import samatov.space.spookies.view_model.dialogs.user_list.UserListItemClickedListener;
import samatov.space.spookies.view_model.dialogs.user_list.UserListItemViewHolder;

public class FavoriteListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {

    List<JsonObject> mFavoriteList;
    UserListItemClickedListener mListener;

    public FavoriteListAdapter(JsonObject favoriteList, UserListItemClickedListener listener) {
        mListener = listener;
        Set<String> keyset = favoriteList.keySet();
        List<String> keyList = new ArrayList<>(keyset);
        Collections.sort(keyList, Collections.reverseOrder());
        mFavoriteList = new ArrayList<>();

        for (int i = 0; i < favoriteList.size(); i++) {
            JsonObject item = new JsonObject();
            String username = keyList.get(i);
            item.addProperty("username", username);

            mFavoriteList.add(item);
        }
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
        JsonObject item = mFavoriteList.get(i);
        String username = item.getAsJsonPrimitive("username").getAsString();

        favoriteListItemViewHolder.bind(username, mListener);
    }


    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }
}
