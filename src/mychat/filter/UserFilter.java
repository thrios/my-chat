package mychat.filter;

import mychat.conversation.Message;

/**
 * Filter for messages by user id
 */
public class UserFilter implements Filter {

    private final String user;

    public UserFilter(String user) {
        this.user = user;
    }

    @Override
    public Message apply(Message message) {

        if (!message.getSenderId().equals(user)) {
            return null;
        }
        return message;
    }
}
