package mychat.conversationexporter;

import mychat.filter.*;
import mychat.filter.obfuscate.CreditCardObfuscateFilter;
import mychat.filter.obfuscate.TelephoneObfuscateFilter;
import mychat.filter.obfuscate.UserObfuscateFilter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a helper to parse command line arguments.
 */
public final class CommandLineArgumentParser {

    /**
     * Parses the given {@code arguments} into the exporter configuration.
     * @param arguments The command line arguments.
     * @return The exporter configuration representing the command line arguments.
     */
    public ConversationExporterConfiguration parseCommandLineArguments(String[] arguments) {

        Collection<Filter> filters = new LinkedList<>();
        String user = "";

        int filterNumber = 2; // filters start from 3rd arg

        for (int i = filterNumber; i < arguments.length; i++) {

            // make filters depending on the arguments provided
            switch (arguments[i]) {
                case "-u": {
                    if (++i < arguments.length) {
                        user = arguments[i];
                        filters.add(new UserFilter(user));
                    }
                    break;
                }
                case "-kw": {
                    if (++i < arguments.length)
                        filters.add(new KeywordFinderFilter(arguments[i]));
                    break;
                }
                case "-bl": {
                    if (++i < arguments.length)
                        filters.add(new StringReplacer(arguments[i]));
                    break;
                }
                case "-uo" : { filters.add(new UserObfuscateFilter(user)); break; }
                case "-cco" : { filters.add( new CreditCardObfuscateFilter()); break; }
                case "-to" : { filters.add( new TelephoneObfuscateFilter()); break; }
            }
        }

        return new ConversationExporterConfiguration(arguments[0], arguments[1], filters);

    }
}
