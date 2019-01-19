package samatov.space.spookies.model.api.beans

import samatov.space.spookies.model.api.middleware.FeedMiddleware

class Feed(): FeedMiddleware() {
    var timeline: List<Post>? = null
    var popular: List<Post>? = null
}