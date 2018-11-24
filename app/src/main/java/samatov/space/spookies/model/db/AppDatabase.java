package samatov.space.spookies.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import samatov.space.spookies.model.db.dao.UserDao;
import samatov.space.spookies.model.db.entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
