package samatov.space.spookies.model.api.beans

import samatov.space.spookies.model.api.middleware.FeedMiddleware

class Feed : FeedMiddleware() {

    var timeline: List<FeedItem>? = ArrayList()
    var popular: List<FeedItem>? = ArrayList()
}