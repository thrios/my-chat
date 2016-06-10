package mychat.conversationexporter;

import com.google.gson.*;
import mychat.conversation.Conversation;
import mychat.filter.Filter;
import mychat.conversation.Message;

import java.io.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a conversation exporter that can read a conversation and write it out in JSON.
 */
public class ConversationExporter {

    /**
     * The application entry point.
     * @param args The command line arguments.
     * @throws Exception Thrown when something bad happens.
     */
    public static void main(String[] args) throws Exception {

        args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-u", "bob",           // user id matcher
                "-kw", "there",        // keyword matcher
                "-bl", "Hello",        // keyword obfuscator
                "-uo",                 // user id obfuscator
                "-cco",                // credit card obfuscator
                "-to"                  // telephone obfuscator
        };

        ConversationExporter exporter = new ConversationExporter();
        ConversationExporterConfiguration conf = new CommandLineArgumentParser().parseCommandLineArguments(args);

        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());
    }

    /**
     * Exports the conversation at {@code inputFilePath} as JSON to {@code outputFilePath}.
     * @param inputFilePath The input file path.
     * @param outputFilePath The output file path.
     * @param filters The filters on the file specified by the input file path
     * @throws Exception Thrown when something bad happens.
     */
    public void exportConversation(String inputFilePath, String outputFilePath, Collection<Filter> filters) throws Exception {
        Conversation conversation = readConversation(inputFilePath);

        conversation.setFilters(filters);
        conversation.applyFilters(filters);

        writeConversation(conversation, outputFilePath);

        // TODO: Add more logging...
        System.out.println("Conversation exported from '" + inputFilePath + "' to '" + outputFilePath);
    }

    /**
     * Helper method to write the given {@code conversation} as JSON to the given {@code outputFilePath}.
     * @param conversation The conversation to write.
     * @param outputFilePath The file path where the conversation should be written.
     * @throws Exception Thrown when something bad happens.
     */
    private void writeConversation(Conversation conversation, String outputFilePath) throws Exception {
        // TODO: Do we need both to be resources, or will buffered writer close the stream?
        try (OutputStream os = new FileOutputStream(outputFilePath);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {

            // TODO: Maybe reuse this? Make it more testable...
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Instant.class, new InstantSerializer());

            Gson g = gsonBuilder.create();

            bw.write(g.toJson(conversation));
        } catch (FileNotFoundException e) {
            // TODO: Maybe include more information?
            throw new IllegalArgumentException("The file was not found.");
        } catch (IOException e) {
            // TODO: Should probably throw different exception to be more meaningful :/
            throw new Exception("Something went wrong");
        }
    }

    /**
     * Represents a helper to read a conversation from the given {@code inputFilePath}.
     * @param inputFilePath The path to the input file.
     * @return The {@link Conversation} representing by the input file.
     * @throws Exception Thrown when something bad happens.
     */
    private Conversation readConversation(String inputFilePath) throws Exception {
        try(InputStream is = new FileInputStream(inputFilePath);
            BufferedReader r = new BufferedReader(new InputStreamReader(is))) {

            List<Message> messages = new ArrayList<>();

            String conversationName = r.readLine();
            String line;

            while ((line = r.readLine()) != null) {
                String delimiter = " ";

                int tsIndex = line.indexOf(delimiter);
                Instant timeStamp = Instant.ofEpochSecond(Long.parseUnsignedLong(line.substring(0, tsIndex)));

                int senderIndex = line.indexOf(delimiter, tsIndex + 1);
                String senderId = line.substring(tsIndex + 1, senderIndex);

                String content = line.substring(senderIndex + 1, line.length());

                messages.add(new Message(timeStamp, senderId, content));
            }

            return new Conversation(conversationName, messages);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The file was not found.");
        } catch (IOException e) {
            throw new Exception("Something went wrong");
        }
    }

    /**
     * A class for JSON serialization of an Instant
     */
    private class InstantSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.getEpochSecond());
        }
    }
}
