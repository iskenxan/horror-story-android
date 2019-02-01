package samatov.space.spookies.model.api.beans.notification

import samatov.space.spookies.model.api.middleware.FeedMiddleware

class NotificationsFeed: FeedMiddleware() {
    var results: List<NotificationGroup> = ArrayList()
    var unseen = 0
    var unread = 0
}