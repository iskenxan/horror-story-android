package samatov.space.spookies.model.api.beans.notification

import samatov.space.spookies.model.api.middleware.FeedMiddleware

class NotificationsFeed: FeedMiddleware() {
    val results: List<NotificationGroup> = ArrayList()
    val unseen = 0
    val unread = 0
}