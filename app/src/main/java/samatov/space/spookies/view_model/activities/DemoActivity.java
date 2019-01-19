package samatov.space.spookies.view_model.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.middleware.SearchMiddleware;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_action_bar_menu, menu);
//
//        Activity activity = this;
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) MenuItemCompat
//                .getActionView(menu.findItem(R.id.action_search));
//
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setQueryHint("Search for users...");
//        String [] columNames = { SearchManager.SUGGEST_COLUMN_TEXT_1 };
//        int [] viewIds = { android.R.id.text1 };
//        CursorAdapter adapter = new SimpleCursorAdapter(this,
//                android.R.layout.simple_list_item_1, null, columNames, viewIds);
//
//        searchView.setSuggestionsAdapter(adapter);
//        searchView.setOnSuggestionListener(getOnSuggestionClickListener());
//        searchView.setOnQueryTextListener(getOnQueryTextListener(activity, adapter));

        return true;
    }



    private SearchView.OnQueryTextListener getOnQueryTextListener(Activity activity, CursorAdapter adapter) {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() < 2) {
                    return false;
                }
                Observable<Map<String, User>> observable = SearchMiddleware.searchForUsers(s, activity);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getRequestObserver(adapter));
                return true;
            }
        };
    }


    private Observer getRequestObserver(CursorAdapter adapter) {
       return new Observer<Map<String, User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Map<String, User> users) {
                Cursor cursor = createCursorFromResult(users);
                adapter.swapCursor(cursor);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
    }


    private SearchView.OnSuggestionListener getOnSuggestionClickListener() {
        return new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int index) {
                // TODO: handle suggestion item click
                return true;
            }
        };
    }


    private Cursor createCursorFromResult(Map<String, User> users)  {
        String[] menuCols = new String[] { BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA };

        MatrixCursor cursor = new MatrixCursor(menuCols);
        int counter = 0;

        for (String username : users.keySet()) {
            User user = users.get(username);
            cursor.addRow(new Object[] { counter, user.getUsername(), user.getUsername() });
            counter++;
        }

        return cursor;
    }


}
