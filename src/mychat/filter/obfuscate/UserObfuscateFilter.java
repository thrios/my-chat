package mychat.filter.obfuscate;

import mychat.conversation.Message;

/**
 * Filter for obfuscating the user id
 */
public class UserObfuscateFilter extends ObfuscateFilter {

    public UserObfuscateFilter(String regex) {
        this.regex = regex;
    }

    @Override
    public Message apply(Message message) {

        if (regex.equals(message.getSenderId())) {
            message.setSenderId(obfuscatedString);
        }
        return message;
    }



}
