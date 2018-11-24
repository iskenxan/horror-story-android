package samatov.space.spookies.model.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "token")
    public String token;

    @ColumnInfo(name = "last_signed_in")
    public long lastSignedIn;


    public User() {

    }

    @Ignore
    public User(String username, String token, long lastSignedIn) {
        this.username = username;
        this.token = token;
        this.lastSignedIn = lastSignedIn;
    }
}
