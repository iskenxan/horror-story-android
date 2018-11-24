package samatov.space.spookies.model.db;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AppDatabaseInstance {
    private static AppDatabase appDatabase = null;


    public static AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null)
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "horror-stories.db").build();
        return appDatabase;
    }

}
