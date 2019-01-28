package samatov.space.spookies.model.api.beans

class IdPostRef(postRef: PostRef): PostRef() {
    var id: String? = null

    init {
        this.title = postRef.title
        this.created = postRef.created
        this.lastUpdated = postRef.lastUpdated
        this.favorite = postRef.favorite
    }
}