package samatov.space.spookies.view_model.activities.my_profile;


import android.app.SearchManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.model.api.middleware.SearchMiddleware;
import samatov.space.spookies.model.utils.FormatterK;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.activities.BaseActivity;

public class SearchUserHandler implements SearchView.OnSuggestionListener {

    private Map<String, User> mUsers;
    private BaseActivity mActivity;
    OnSearchSuggestionClick mSuggestionClickListener;
    SearchView mSearchView;
    SearchUsersCursorAdapter mAdapter;


    public SearchUserHandler(SearchView searchView, SearchManager searchManager,
                             BaseActivity activity, ApiRequestListener apiRequestListener,
                             OnSearchSuggestionClick suggestionClickListener) {
        mActivity = activity;
        mSearchView = searchView;
        mSuggestionClickListener = suggestionClickListener;

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setQueryHint("Search for users...");
        mAdapter = new SearchUsersCursorAdapter(activity, null, 0);
        searchView.setSuggestionsAdapter(mAdapter);
        setupQueryListenerForSearchView(searchView, apiRequestListener);
        searchView.setOnSuggestionListener(this);
    }


    @Override
    public boolean onSuggestionSelect(int i) {
        return true;
    }


    @Override
    public boolean onSuggestionClick(int i) {
        Cursor cursor= mSearchView.getSuggestionsAdapter().getCursor();
        cursor.moveToPosition(i);
        String username = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        mSuggestionClickListener.onSuggestionClick(username);

        return true;
    }


    private void setupQueryListenerForSearchView(SearchView searchView, ApiRequestListener listener) {
        RxSearchView.queryTextChanges(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    String query = charSequence + "";
                    requestSearch(query, listener);
                });
    }


    private void requestSearch(String query, ApiRequestListener listener) {
        try {
            if (Validator.isNullOrEmpty(query))
                return;

            Observable<Map<String, User>> observable = SearchMiddleware.searchForUsers(query, mActivity);
            mActivity.listenToObservable(observable, (result, exception) ->
                    onSearchComplete(listener, result, exception));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void onSearchComplete(ApiRequestListener listener, Object result, Throwable exception) {
        listener.onRequestComplete(result, exception);
        if (exception != null)
            return;
        mUsers = (Map<String, User>) result;
        MyPreferenceManager.saveObjectAsJson(mActivity,
                MyPreferenceManager.USER_SEARCH_RESULT, mUsers);
        Cursor cursor = createCursorFromResult();
        mAdapter.swapCursor(cursor);
    }


    private Cursor createCursorFromResult()  {
        String[] menuCols = new String[] { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_ICON_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA };

        MatrixCursor cursor = new MatrixCursor(menuCols);
        int counter = 0;

        for (String username : mUsers.keySet()) {
            User user = mUsers.get(username);
            String profileUrl = FormatterK.Companion.getUserProfileUrl(username);
            cursor.addRow(new Object[] { counter, user.getUsername(), profileUrl, user.getUsername() });
            counter++;
        }

        return cursor;
    }
}
