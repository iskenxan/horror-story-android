package samatov.space.spookies.model.post;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {

   private String id;
   private String name;
   private String avatar;


   public Author(String id, String name) {
       this.id = id;
       this.name = name;
   }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}