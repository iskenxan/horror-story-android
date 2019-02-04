package samatov.space.spookies.view_model.fragments.posts_viewpager;

import samatov.space.spookies.model.api.beans.PostRef;

public interface PostListItemClicked {
    void onItemClicked(PostRef post, ClickItemType clickedType);
}
