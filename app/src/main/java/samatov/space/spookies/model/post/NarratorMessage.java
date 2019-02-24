package samatov.space.spookies.model.post;

import com.stfalcon.chatkit.commons.models.MessageContentType;

public class NarratorMessage extends Message implements MessageContentType {
    public static final byte CONTENT_TYPE = 119;

    public NarratorMessage() {

    }


    public NarratorMessage(Message message) {
        setAuthor(message.getUser());
        setCreatedAt(message.getCreatedAt());
        setId(message.getId());
        setText(message.getText());
    }
}
