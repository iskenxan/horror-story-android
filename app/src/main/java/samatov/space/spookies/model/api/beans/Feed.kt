package samatov.space.spookies.model.api.beans

import samatov.space.spookies.model.api.middleware.FeedMiddleware

class Feed : FeedMiddleware() {

    var timeline: List<FeedItem>? = ArrayList()
    var popular: List<FeedItem>? = ArrayList()
    var new: List<FeedItem>? = ArrayList()


    fun addNewPostsAndSort() {
        this.new = popular?.toList()
        this.new = this.new?.sortedBy { it.lastUpdated }
        this.new = this.new?.reversed()
    }
}