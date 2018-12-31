package samatov.space.spookies.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.middleware.SearchMiddleware;
import samatov.space.spookies.model.utils.Formatter;
import samatov.space.spookies.model.utils.Validator;

public class SearchUsersContentProvider extends ContentProvider {

    private List<User> mUsers;

    private static String BASE_AUTHORITY = "space.samatov.android.searchuserprovider";

    private static int ROUTE_QUERY = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(BASE_AUTHORITY, "search_suggest_query/*", ROUTE_QUERY);
    }


    @Override
    public boolean onCreate() {
        mUsers = new ArrayList<>();

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        if (match != ROUTE_QUERY)
            return null;

        String query = uri.getLastPathSegment();
        if (Validator.isNullOrEmpty(query))
            return null;
        try {
            mUsers = SearchMiddleware.searchForUsersSync(query, getContext());
            MyPreferenceManager.saveObjectAsJson(getContext(), MyPreferenceManager.CURRENT_USER_QUERY_RESULTS, mUsers);
            return createCursorFromResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Cursor createCursorFromResult() throws Exception {
        String[] menuCols = new String[] { "_id", "suggest_text_1", "suggest_icon_1", "suggest_intent_data" };
        MatrixCursor cursor = new MatrixCursor(menuCols);
        int counter = 0;

        for (User user : mUsers) {
            Object profileUri =  R.drawable.ic_profile_placeholder;
            if (!Validator.isNullOrEmpty(user.getProfileUrl()))
                profileUri = Formatter.uriFromUrl(user.getProfileUrl(), getContext());

            cursor.addRow(new Object[]{ counter, user.getUsername(), profileUri, user.getUsername() });
            counter++;
        }

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
