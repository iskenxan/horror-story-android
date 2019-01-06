package samatov.space.spookies.model.api.beans;

import com.google.gson.JsonObject;

import java.util.Map;

import samatov.space.spookies.model.api.middleware.ProfileMiddleware;

public class User extends ProfileMiddleware {
    private String username;
    private String profileUrl;
    private Map<String, JsonObject> followers; //username, picture_url
    private Map<String, JsonObject> following; //username, picture_url
    private Map<String, JsonObject> draftRefs; //draftId, draft data
    private Map<String, JsonObject> publishedRefs; //postId, draft data

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Map<String, JsonObject> getDraftRefs() {
        return draftRefs;
    }

    public void setDraftRefs(Map<String, JsonObject> draftRefs) {
        this.draftRefs = draftRefs;
    }

    public Map<String, JsonObject> getPublishedRefs() {
        return publishedRefs;
    }

    public void setPublishedRefs(Map<String, JsonObject> publishedRefs) {
        this.publishedRefs = publishedRefs;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }


    public Map<String, JsonObject> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, JsonObject> followers) {
        this.followers = followers;
    }

    public Map<String, JsonObject> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, JsonObject> following) {
        this.following = following;
    }
}
