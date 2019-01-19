package samatov.space.spookies.view_model.dialogs.user_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import samatov.space.spookies.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {

    List<JsonObject> mUserList;
    UserListItemClickedListener mListener;


    public UserListAdapter(Map<String, JsonObject> userMap, UserListItemClickedListener listItemClickedListener) {
        mListener = listItemClickedListener;
        Set<String> keyset = userMap.keySet();
        List<String> keyList = new ArrayList<>(keyset);
        Collections.sort(keyList, Collections.reverseOrder());
        mUserList = new ArrayList<>();

        for (int i = 0; i < userMap.size(); i++) {
            JsonObject item = userMap.get(keyList.get(i)).deepCopy();
            item.addProperty("username", keyList.get(i));
            mUserList.add(item);
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
    public void onBindViewHolder(@NonNull UserListItemViewHolder userListItemViewHolder, int i) {
        JsonObject item = mUserList.get(i);
        String username = item.getAsJsonPrimitive("username").getAsString();

        userListItemViewHolder.bind(username, mListener);
    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
