package samatov.space.spookies.model.api.beans;

import java.util.List;
import java.util.Map;

import samatov.space.spookies.model.api.middleware.ProfileMiddleware;

public class User extends ProfileMiddleware {
    private String username;
    private List<String> followers;
    private List<String> following;
    private Map<String, PostRef> draftRefs; //draftId, draft data
    private Map<String, PostRef> publishedRefs; //postId, draft data

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Map<String, PostRef> getDraftRefs() {
        return draftRefs;
    }

    public void setDraftRefs(Map<String, PostRef> draftRefs) {
        this.draftRefs = draftRefs;
    }

    public Map<String, PostRef> getPublishedRefs() {
        return publishedRefs;
    }

    public void setPublishedRefs(Map<String, PostRef> publishedRefs) {
        this.publishedRefs = publishedRefs;
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
