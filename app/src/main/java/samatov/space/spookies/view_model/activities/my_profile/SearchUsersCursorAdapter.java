package samatov.space.spookies.view_model.activities.my_profile;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;

public class SearchUsersCursorAdapter extends CursorAdapter {

    private LayoutInflater mCursorInflater;

    public SearchUsersCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        mCursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mCursorInflater.inflate(R.layout.user_search_suggestions_item, viewGroup, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String profileUrl = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1));
        String username = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));

        CircleImageView imageView = view.findViewById(R.id.userSearchItemImageView);
        Picasso.get()
                .load(profileUrl)
                .error(R.drawable.ic_profile_placeholder)
                .resize(30,30)
                .centerCrop()
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(imageView);

        TextView textView = view.findViewById(R.id.userSearchItemTextView);
        textView.setText(username);
    }
}
