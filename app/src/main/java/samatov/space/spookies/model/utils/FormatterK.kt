package samatov.space.spookies.model.utils

class FormatterK {

    companion object {
        fun getUserProfileUrl(username: String) =
                "https://firebasestorage.googleapis.com/v0/b/travelguide-bf6df.appspot.com/o/$username.jpg?alt=media"
    }

}