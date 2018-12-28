package samatov.space.spookies.view_model.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import samatov.space.spookies.R;

public class SearchableActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        checkIntent();
    }


    private void checkIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            //TODO: on click show user's profile page
            Uri data = intent.getData();
        }
    }
}
