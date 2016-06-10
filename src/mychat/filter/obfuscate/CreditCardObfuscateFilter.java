package mychat.filter.obfuscate;

import mychat.conversation.Message;

/**
 * Filter for obfuscating the credit cards numbers
 */
public class CreditCardObfuscateFilter extends ObfuscateFilter {

    public CreditCardObfuscateFilter() {
        this.regex = "[0-9]{12,14}"; //TODO: can be improved
    }

    @Override
    public Message apply(Message message) {

        message.setContent(message.getContent().replaceAll(regex, obfuscatedString));

        return message;
    }
}
