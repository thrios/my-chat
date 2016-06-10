package mychat.filter;

import mychat.conversation.Message;

/**
 * Discards entries that do not have the desired keyword
 */
public class KeywordFinderFilter implements Filter {

    private String keyword;

    public KeywordFinderFilter(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Message apply(Message message) {

        if (!message.getContent().contains(keyword)) {
            return null;
        }
        return message;
    }
}
