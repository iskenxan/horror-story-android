package samatov.space.spookies.view_model.dialogs.favorite;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.gson.JsonObject;

import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.view_model.activities.BaseActivity;

public class FavoriteDialogHandler {

    View mContainerView;
    BottomDialog mDialog;
    BaseActivity mActivity;
    List<String> mFavorite;


    public FavoriteDialogHandler(BaseActivity activity, List<String> favorite) {
        mActivity = activity;
        mContainerView = activity.getLayoutInflater()
                .inflate(R.layout.user_list_view, null, false);
        mFavorite = favorite;
    }


    public void showDialog() {
        setupRecyclerView();
        mDialog = new BottomDialog.Builder(mActivity)
                .setTitle("Liked by")
                .setIcon(R.drawable.favorite_icon_colored)
                .setCustomView(mContainerView)
                .setCancelable(true)
                .show();
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = mContainerView.findViewById(R.id.favoritesDialogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        FavoriteListAdapter adapter = new FavoriteListAdapter(mFavorite, username -> {
            mDialog.dismiss();
            mActivity.getUserAndStartViewProfileActivity(username, false);
        });

        recyclerView.setAdapter(adapter);
    }
}
