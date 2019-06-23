package samatov.space.spookies.model.api.beans

import samatov.space.spookies.model.utils.FormatterK

class PostRef(id: String, author: String): BasePostReference() {
    init {
        this.id = id
        this.author = author
    }


    constructor(post: Post) : this(post.id, post.author) {
        this.created = post.created
        this.lastUpdated = post.lastUpdated
        this.favorite = FormatterK.getFormattedFavorite(post.favorite)
        this.title = post.title
        this.preface = post.preface
    }


    var created: Long = 0
    var favorite: List<String>? = ArrayList()
}