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
import samatov.space.spookies.model.post.NarratorMessage;

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
        characters.add("Narrator", new JsonObject());
        changeCharacterSettings("Narrator", "id", 2);
    }


    public List<Message> getSortedDialog() {
        List<Message> messages = getMessageList();
        sortPostsByTimestamp(messages);

        return messages;
    }


    private void sortPostsByTimestamp(List<Message> list) {
        Collections.sort(list, (a, b) ->
                (int) (a.getCreatedAt().getTime()
                        - (b.getCreatedAt().getTime())));
    }


    private List<Message> getMessageList() {
        List<Message> messages = new ArrayList<>();
        Set<String> keys = dialog.keySet();
        for (String key : keys) {
            JsonObject msgObject = dialog.getAsJsonObject(key);
            Message message = constructMessage(key, msgObject);
            messages.add(message);
        }

        return messages;
    }


    private Message constructMessage(String id, JsonObject msgObject) {
        Message message = new Message();
        message.setId(id);
        String messageBy = msgObject.getAsJsonPrimitive("name").getAsString();
        String author = messageBy;
        String authorId  = author.equals("Narrator") ? "2" : getCharacterId(author) + "";

        String text = msgObject.getAsJsonPrimitive("text").getAsString();
        long timeStamp = msgObject.getAsJsonPrimitive("timestamp").getAsLong();

        message.setAuthor(new Author(authorId, author));
        message.setCreatedAt(new Date(timeStamp));
        message.setText(text);

        if (authorId.equals("2"))
            message = new NarratorMessage(message);

        return message;
    }


    public void addMessage(Message message) {
        addMessage(message.getText(), message.getUser().getName(), message.getId());
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


    public void setCharacterName(String newName, int characterId) {
        String oldName = getCharacterName(characterId);
        if (oldName == null) {
            addCharacterIfNull(newName, characterId);
            return;
        }
        JsonObject character = characters.getAsJsonObject(oldName);
        characters.remove(oldName);
        characters.add(newName, character);
    }


    public void updateMessagesAuthorName(String oldName, String newName) {
        Set<String> keys = dialog.keySet();
        for (String key : keys) {
            JsonObject message = dialog.get(key).getAsJsonObject();
            String characterName = message.get("name").getAsString();
            if (characterName.equals(oldName))
                message.addProperty("name", newName);
        }
    }


    public void addCharacterIfNull(String name, int characterId) {
        String characterName = getCharacterName(characterId);
        if (characterName != null)
            return;

        characters.add(name, new JsonObject());
        changeCharacterSettings(name,"id", characterId);
    }


    public String getCharacterName(int characterId) {
        Set<String> keys = characters.keySet();
        for (String key : keys) {
            JsonObject character = characters.get(key).getAsJsonObject();
            int id = character.get("id").getAsInt();
            if (id == characterId)
                return key;
        }

        return null;
    }


    public int getCharacterId(String characterName) {
        Set<String> keys = characters.keySet();
        for (String key : keys) {
            JsonObject character = characters.get(key).getAsJsonObject();
            if (key.equals(characterName))
                return character.get("id").getAsInt();
        }

        return -1;
    }


    public String getCharacterColor(int characterId) {

        String characterName = getCharacterName(characterId);
        if (characterName == null)
            return "#4E5EDA";

        JsonObject character = characters.get(characterName).getAsJsonObject();
        if (!character.has("color"))
            return "#4E5EDA";
        return character.get("color").getAsString();
    }


    public void changeCharacterSettings(String characterName, String settingName, Object value) {
        if (value instanceof String)
            characters.getAsJsonObject(characterName).addProperty(settingName, (String) value);
        else
            characters.getAsJsonObject(characterName).addProperty(settingName, (Number) value);
    }


    public List<String> getCharacterNameList() {
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
