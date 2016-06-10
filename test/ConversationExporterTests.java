import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import mychat.conversation.Conversation;
import mychat.conversationexporter.CommandLineArgumentParser;
import mychat.conversationexporter.ConversationExporter;
import mychat.conversationexporter.ConversationExporterConfiguration;
import mychat.filter.Filter;
import mychat.conversation.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


/**
 * Tests for the {@link ConversationExporter}.
 */
public class ConversationExporterTests {

    private final String inputPath = "resources/chat.txt";
    private final String outputPath = "resources/chat.json";

    private ConversationExporter exporter;
    private ConversationExporterConfiguration conf;

    @Before
    public void setUp() {
        exporter = new ConversationExporter();
    }


    /**
     * Tests the behaviour of the program when random unknown parameters are provided
     * @throws Exception
     */
    @Test
    public void testRandomUnknownParameters() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-random", "unknown"
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/11.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests the behaviour of the program when no filters are provided
     * @throws Exception
     */
    @Test
    public void testSimpleExecution() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/11.json")));

        assertEquals(s1, s2);
    }

    /**
     * Tests the behaviour of the program when no values are provided with the flags
     * @throws Exception
     */
    @Test
    public void testEmptyFlags() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-u", "-kw"
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/10.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests multiple keyword obfuscator filters
     * @throws Exception
     */
    @Test
    public void testCombinationOfFilters() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-u", "bob",           // user id matcher
                "-kw", "there",        // keyword matcher
                "-bl", "Hello",        // keyword obfuscator
                "-uo",                 // user id obfuscator
                "-cco",                // credit card obfuscator
                "-to"                  // telephone obfuscator
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/9.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests multiple keyword obfuscator filters
     * @throws Exception
     */
    @Test
    public void testMultipleKeywordObfuscatorFilters() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-bl", "Hello",        // keyword obfuscator
                "-bl", "Credit"
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/8.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests the user obfuscator filter when no user is provided
     * @throws Exception
     */
    @Test
    public void testUserObfuscatorFilterWithoutUser() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-uo",                 // user id obfuscator
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/7.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests credit card obfuscator filter
     * @throws Exception
     */
    @Test
    public void testTelephoneObfuscatorFilter() throws Exception {

        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-to"                  // telephone obfuscator
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/6.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests credit card obfuscator filter
     * @throws Exception
     */
    @Test
    public void testCreditCardObfuscatorFilter() throws Exception {
        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-cco",                // credit card obfuscator
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/5.json")));

        assertEquals(s1, s2);
    }


    /**
     * Tests user id obfuscator filter
     * @throws Exception
     */
    @Test
    public void testUserObfuscatorFilter() throws Exception {
        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-u", "bob",           // user id matcher
                "-uo"                  // user id obfuscator
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/4.json")));

        assertEquals(s1, s2);
    }

    /**
     * Tests keyword obfuscator filter
     * @throws Exception
     */
    @Test
    public void testKeywordObfuscatorFilter() throws Exception {
        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-bl", "Hello",        // keyword obfuscator
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/3.json")));

        assertEquals(s1, s2);
    }

    /**
     * Tests user filter with a single user specified by the flag
     * @throws Exception
     */
    @Test
    public void testSingleUserFilter() throws Exception {
        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-u", "bob",           // user id matcher
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/2.json")));

        assertEquals(s1, s2);
    }

    /**
     * Tests keyword filter with a single keyword specified by the flag
     * @throws Exception
     */
    @Test
    public void testSingleKeywordFilter() throws Exception {
        String[] args = new String[]{
                "resources/chat.txt",  // input
                "resources/chat.json", // output
                "-kw", "there",        // keyword matcher
        };

        conf = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(conf.getInputFilePath(), conf.getOutputFilePath(), conf.getFilters());

        String s1 = new String(Files.readAllBytes(Paths.get(outputPath)));
        String s2 = new String(Files.readAllBytes(Paths.get("test_resources/1.json")));

        assertEquals(s1, s2);
    }

    /**
     * Tests that exporting a conversation will export the conversation correctly.
     * @throws Exception When something bad happens.
     */
    @Test
    public void testExportingConversationExportsConversation() throws Exception {

        exporter.exportConversation(inputPath, outputPath, new ArrayList<Filter>());

        JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(outputPath)));
        jsonReader.setLenient(true);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Instant.class, new InstantDeserializer());
        Gson g = builder.create();

        Conversation c = g.fromJson(new JsonParser().parse(jsonReader), Conversation.class);

        assertEquals("My Conversation", c.getName());

        assertEquals(7, c.getMessages().size());

        Message[] ms = new Message[c.getMessages().size()];
        c.getMessages().toArray(ms);

        assertEquals(ms[0].getTimestamp(), Instant.ofEpochSecond(1448470901));
        assertEquals(ms[0].getSenderId(), "bob");
        assertEquals(ms[0].getContent(), "Hello there! Credit card 12345678123456; Telephone 012382189");

        assertEquals(ms[1].getTimestamp(), Instant.ofEpochSecond(1448470905));
        assertEquals(ms[1].getSenderId(), "mike");
        assertEquals(ms[1].getContent(), "how are you?");

        assertEquals(ms[2].getTimestamp(), Instant.ofEpochSecond(1448470906));
        assertEquals(ms[2].getSenderId(), "bob");
        assertEquals(ms[2].getContent(), "I'm good thanks, do you like pie?");

        assertEquals(ms[3].getTimestamp(), Instant.ofEpochSecond(1448470910));
        assertEquals(ms[3].getSenderId(), "mike");
        assertEquals(ms[3].getContent(), "no, let me ask Angus...");

        assertEquals(ms[4].getTimestamp(), Instant.ofEpochSecond(1448470912));
        assertEquals(ms[4].getSenderId(), "angus");
        assertEquals(ms[4].getContent(), "Hell yes! Are we buying some pie?");

        assertEquals(ms[5].getTimestamp(), Instant.ofEpochSecond(1448470914));
        assertEquals(ms[5].getSenderId(), "bob");
        assertEquals(ms[5].getContent(), "No, just want to know if there's anybody else in the pie society...");

        assertEquals(ms[6].getTimestamp(), Instant.ofEpochSecond(1448470915));
        assertEquals(ms[6].getSenderId(), "angus");
        assertEquals(ms[6].getContent(), "YES! I'm the head pie eater there...");
    }

    private class InstantDeserializer implements JsonDeserializer<Instant> {

        @Override
        public Instant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonPrimitive()) {
                throw new JsonParseException("Expected instant represented as JSON number, but no primitive found.");
            }

            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

            if (!jsonPrimitive.isNumber()) {
                throw new JsonParseException("Expected instant represented as JSON number, but different primitive found.");
            }

            return Instant.ofEpochSecond(jsonPrimitive.getAsLong());
        }
    }
}
