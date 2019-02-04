package samatov.space.spookies.model.utils

import com.google.gson.JsonObject
import samatov.space.spookies.model.api.beans.PostRef
import java.util.*
import kotlin.collections.ArrayList

class FormatterK {

    companion object {
        fun getUserProfileUrl(username: String) =
                "https://firebasestorage.googleapis.com/v0/b/travelguide-bf6df.appspot.com/o/$username.jpg?alt=media"


        fun getFormattedFavorite(favorite: JsonObject): List<String> {
            val list: MutableList<String> = ArrayList()

            favorite.keySet().forEach {
                list.add(it)
            }

            return list
        }


        fun getFormattedPostList(posts: Map<String, PostRef>?): List<PostRef>? {
            val list = ArrayList<PostRef>()

            if (posts == null)
                return null

            for (key in posts.keys)
                list.add(posts.getValue(key))

            sortPostsByTimestamp(list)

            return list
        }


        private fun sortPostsByTimestamp(list: List<PostRef>) {
            Collections.sort(list) { a, b -> (b.lastUpdated - a.lastUpdated).toInt() }
        }
    }
}