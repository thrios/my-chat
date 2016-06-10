package mychat.filter;

import mychat.conversation.Message;

/**
 * Replaces substrings (banned keyword) of a string with a replacement string
 */
public class StringReplacer implements Filter {

    private String keyword;
    private final String replacementString = "\\\\*redacted\\\\*";

    public StringReplacer(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Message apply(Message message) {

        message.setContent(message.getContent().replaceAll(keyword, replacementString));

        return message;
    }
}
