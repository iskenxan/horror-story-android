package samatov.space.spookies.model.api.beans

open class PostRef {
    var author: String? = null
    var title: String? = null
    var favorite: List<String>? = ArrayList()
    var created: Long = 0
    var lastUpdated: Long = 0
}