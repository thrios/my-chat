package mychat.filter.obfuscate;

import mychat.conversation.Message;

/**
 * Filter for obfuscating telephone numbers
 */
public class TelephoneObfuscateFilter extends ObfuscateFilter {

    public TelephoneObfuscateFilter() {
        this.regex = "[0-9]{6,14}"; //TODO: can be improved
    }

    @Override
    public Message apply(Message message) {

        message.setContent(message.getContent().replaceAll(regex, obfuscatedString));

        return message;
    }

}
