package samatov.space.spookies.model.api.beans;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import samatov.space.spookies.model.api.middleware.ProfileMiddleware;

public class User extends ProfileMiddleware {
    private String username;
    private String profileUrl;
    private List<String> followers;
    private List<String> following;
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

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}
