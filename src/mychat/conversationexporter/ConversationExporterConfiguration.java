package mychat.conversationexporter;

import mychat.filter.Filter;

import java.util.Collection;

/**
 * Represents the configuration for the exporter.
 */
public final class ConversationExporterConfiguration {

    /**
     * Gets the input file path.

     */
    private String inputFilePath;

    /**
     * Gets the output file path.
     */
    private String outputFilePath;

    /**
     * Represents an inclusive apply on user name
     */
    private Collection<Filter> filters;

    /**
     * Initializes a new instance of the {@link ConversationExporterConfiguration} class.
     * @param inputFilePath The input file path.
     * @param outputFilePath The output file path.
     */
    private ConversationExporterConfiguration(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    /**
     * @see #ConversationExporterConfiguration
     * @param filters the filters to be used
     */
    public ConversationExporterConfiguration(String inputFilePath, String outputFilePath, Collection<Filter> filters) {
        this(inputFilePath, outputFilePath);
        this.filters = filters;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public Collection<Filter> getFilters() {
        return filters;
    }
}
