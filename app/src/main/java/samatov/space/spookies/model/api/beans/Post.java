package samatov.space.spookies.model.api.beans;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import samatov.space.spookies.model.api.middleware.PostsMiddleware;
import samatov.space.spookies.model.post.Author;
import samatov.space.spookies.model.post.Message;

public class Post extends PostsMiddleware {

    private String title;
    private String author;
    private String id;
    private int dialogCount = 0;
    private JsonObject dialog = new JsonObject();
    private JsonObject characters = new JsonObject();
    private JsonObject favorite = new JsonObject();
    private List<Comment> comments = new ArrayList<>();
    private long created;
    private long lastUpdated;


    public Post() {
        characters.add("User", new JsonObject());
        changeCharacterSettings("User", "isMain", "true");
    }


    public List<Message> getSortedDialog() throws Exception {
        List<Message> messages = getMessageList();
        sortPostsByTimestamp(messages);

        return messages;
    }


    private void sortPostsByTimestamp(List<Message> list) {
        Collections.sort(list, (a, b) ->
                (int) (a.getCreatedAt().getTime()
                        - (b.getCreatedAt().getTime())));
    }


    private List<Message> getMessageList() throws Exception {
        List<Message> messages = new ArrayList<>();
        Set<String> keys = dialog.keySet();
        for (String key : keys) {
            JsonObject msgObject = dialog.getAsJsonObject(key);
            Message message = constructMessage(key, msgObject);
            messages.add(message);
        }

        return messages;
    }


    private Message constructMessage(String id, JsonObject msgObject) throws Exception {
        Message message = new Message();
        message.setId(id);
        String messageBy = msgObject.getAsJsonPrimitive("name").getAsString();
        String author =  messageBy.equals("User") ? "User" : getOtherCharacterName();
        String authorId  = messageBy.equals("User") ? "0" : "1";

        String text = msgObject.getAsJsonPrimitive("text").getAsString();
        long timeStamp = msgObject.getAsJsonPrimitive("timestamp").getAsLong();

        message.setAuthor(new Author(authorId, author));
        message.setCreatedAt(new Date(timeStamp));
        message.setText(text);

        return message;
    }


    public String getChatBubbleColor() {
        if (characters.has("User")
                && characters.getAsJsonObject("User").has("color")) {
            return characters.getAsJsonObject("User").getAsJsonPrimitive("color").getAsString();
        } else
            return "#4E5EDA";
    }


    public void changeCharacterSettings(String characterName, String settingName, String value) {
        characters.getAsJsonObject(characterName).addProperty(settingName, value);
    }


    public void addMessageFromUser(String text, String messageId) {
        addMessage(text, "User", messageId);
    }


    public void addMessageFromOtherCharacter(String text, String messageId) {
        addMessage(text, "Other", messageId);
    }


    private void addMessage(String text, String name, String id) {
        JsonObject message = new JsonObject();
        message.addProperty("timestamp", new Date().getTime());
        message.addProperty("name", name);
        message.addProperty("text", text);

        dialog.add(id, message);
        dialogCount++;
    }


    public JsonObject getMessage(String id) {
        return dialog.getAsJsonObject(id);
    }


    public void deleteMessage(String id) {
        dialog.remove(id);
        dialogCount--;
    }


    public void setOtherCharacter(String name) {
        JsonObject user = characters.getAsJsonObject("User");
        characters = new JsonObject();
        characters.add("User", user);
        characters.add(name, new JsonObject());
        changeCharacterSettings(name, "isMain", "false");
    }


    public String getOtherCharacterName() {
        Set<String> keys = characters.keySet();
        for (String key : keys) {
            if (key.equals("User"))
                continue;
            return key;
        }

        return null;
    }


    public List<String> getCharacterList() {
        List<String> list = new ArrayList<>();

        for (String key : characters.keySet()) {
            list.add(key);
        }

        return list;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDialogCount() {
        return dialogCount;
    }

    public void setDialogCount(int dialogCount) {
        this.dialogCount = dialogCount;
    }

    public JsonObject getDialog() {
        return dialog;
    }

    public void setDialog(JsonObject dialog) {
        this.dialog = dialog;
    }

    public JsonObject getCharacters() {
        return characters;
    }

    public void setCharacters(JsonObject characters) {
        this.characters = characters;
    }


    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public JsonObject getFavorite() {
        return favorite;
    }

    public void setFavorite(JsonObject favorite) {
        this.favorite = favorite;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
