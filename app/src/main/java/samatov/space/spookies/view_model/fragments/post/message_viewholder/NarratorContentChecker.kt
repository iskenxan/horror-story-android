package samatov.space.spookies.view_model.fragments.post.message_viewholder

import com.stfalcon.chatkit.messages.MessageHolders
import samatov.space.spookies.model.post.Message
import samatov.space.spookies.model.post.NarratorMessage


class NarratorContentChecker: MessageHolders.ContentChecker<Message> {


    override fun hasContentFor(message: Message?, type: Byte): Boolean {
        when (type) {
            NarratorMessage.CONTENT_TYPE -> {
                return message is NarratorMessage
            }
        }
        return false
    }

}