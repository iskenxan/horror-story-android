package samatov.space.spookies.model.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Single;
import samatov.space.spookies.model.db.entities.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("select * from user order by last_signed_in desc")
    Single<User> getLastSignedIn();

    @Delete
    void delete(User user);
}
