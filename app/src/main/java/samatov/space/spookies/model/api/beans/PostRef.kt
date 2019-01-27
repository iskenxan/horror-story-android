package samatov.space.spookies.model.api.beans

class PostRef {

    //TODO: add this to the user Published and Drafts refs instead of the JsonObject
    var title: String? = null
    var favorite: List<String> = ArrayList()
    var created: Long = 0
    var lastUpdated: Long = 0
}