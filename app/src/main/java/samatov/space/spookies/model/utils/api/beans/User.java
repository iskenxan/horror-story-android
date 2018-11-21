package samatov.space.spookies.model.utils.api.beans;

import java.util.Map;

public class User {
    private String username;
    private Map<String, String> followers; //username, picture_url
    private Map<String, String> following; //username, picture_url
    private Map<String, String> draftRefs; //draftId, title
    private Map<String, String> publishedRefs; //postId, title

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, String> followers) {
        this.followers = followers;
    }

    public Map<String, String> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, String> following) {
        this.following = following;
    }

    public Map<String, String> getDraftRefs() {
        return draftRefs;
    }

    public void setDraftRefs(Map<String, String> draftRefs) {
        this.draftRefs = draftRefs;
    }

    public Map<String, String> getPublishedRefs() {
        return publishedRefs;
    }

    public void setPublishedRefs(Map<String, String> publishedRefs) {
        this.publishedRefs = publishedRefs;
    }
}
