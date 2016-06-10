package mychat.filter.obfuscate;

import mychat.filter.Filter;

/**
 * Abstract class for representing a filter
 * which obfuscates content of a message
 */
 abstract class ObfuscateFilter implements Filter {

    protected String regex;
    protected String obfuscatedString = "\\*redacted\\*";

}
