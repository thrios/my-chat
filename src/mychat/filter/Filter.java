package mychat.filter;

import mychat.conversation.Message;

/**
 * An interface for different filters to implement
 */
public interface Filter {

    /**
     * Applies a filter to a message and returns the
     * modified message or null if the message is to
     * be deleted
     * @param message the message to process
     * @return the Message object result
     */
    Message apply(Message message);
}
