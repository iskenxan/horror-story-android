package samatov.space.spookies.view_model.dialogs.user_list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.gson.JsonObject;

import java.util.Map;

import samatov.space.spookies.R;
import samatov.space.spookies.view_model.activities.BaseActivity;

public class UserListDialogHandler {

    Map<String, JsonObject> mUserList;
    BaseActivity mActivity;
    BottomDialog mDialog;
    View mContainerView;
    String mTitle;


    public UserListDialogHandler(BaseActivity activity, Map<String, JsonObject> userList, String title) {
        mActivity = activity;
        mUserList = userList;
        mTitle = title;
        mContainerView = activity.getLayoutInflater()
                .inflate(R.layout.user_list_view, null, false);
    }


    public void showDialog() {
        setupRecyclerView();
        mDialog = new BottomDialog.Builder(mActivity)
                .setTitle(mTitle)
                .setCustomView(mContainerView)
                .setCancelable(true)
                .show();
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = mContainerView.findViewById(R.id.favoritesDialogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        UserListAdapter adapter = new UserListAdapter(mUserList, username -> {
            mDialog.dismiss();
            mActivity.getUserAndStartViewProfileActivity(username, false);
        });
        recyclerView.setAdapter(adapter);
    }
}

