package samatov.space.spookies.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.middleware.SearchMiddleware;
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
        if (Validator.isNullOrEmpty(query) || query.equals("search_suggest_query"))
            return null;

        try {
            //TODO: save the result in my preferences to get item in case the user clicks on the suggestion
            mUsers = SearchMiddleware.searchForUsersSync(query, getContext());
            return createCursorFromResult();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Cursor createCursorFromResult() {
        String[] menuCols = new String[] { "_id", "suggest_text_1", "suggest_intent_data"};
        MatrixCursor cursor = new MatrixCursor(menuCols);
        int counter = 0;

        for (User user : mUsers) {
            cursor.addRow(new Object[]{ counter, user.getUsername(), user.getUsername() });
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
