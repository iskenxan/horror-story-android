package samatov.space.spookies.view_model.utils;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class ActivityFactory {

    public static void startActivity(AppCompatActivity current, Class newActivityClass, boolean animation, boolean finishCurrent) {
        Intent myIntent = new Intent(current, newActivityClass);
        if (animation)
            current.startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(current).toBundle());
        else
            current.startActivity(myIntent);
        if (finishCurrent)
            current.finish();
    }
}
