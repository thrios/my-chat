package mychat.conversation;

import mychat.filter.Filter;

import java.util.*;

/**
 * Represents the model of a conversation.
 */
public final class Conversation {

    /**
     * The name of the conversation.
     */
    private String name;

    /**
     * The messages in the conversation.
     */
    private List<Message> messages;

    /**
     * The filters used for the conversation.
     */
    private transient Collection<Filter> filters;

    /**
     * The users of the conversation
     */
    private Map<String, User> users = new HashMap<>();

    /**
     * Initializes a new instance of the {@link Conversation} class.
     * @param name The name of the conversation.
     * @param messages The messages in the conversation.
     */
    public Conversation(String name, List<Message> messages) {
        this.name = name;
        this.messages = messages;
        initializeUsers();
        users = sortByComparator(users);
    }

    /**
     * Sorts a hashmap of users based on their activity
     * @param unsortedMap the input HashMap
     * @return the sorted HashMap by activity
     */
    private static Map<String, User> sortByComparator(Map<String, User> unsortedMap) {

        // Convert Map to List
        List<Map.Entry<String, User>> list =
                new LinkedList<Map.Entry<String, User>>(unsortedMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, User>>() {
            public int compare(Map.Entry<String, User> o1,
                               Map.Entry<String, User> o2) {
                return Integer.compare(o1.getValue().getActivity(), o2.getValue().getActivity());
            }
        });

        // Convert sorted map back to a Map
        Map<String, User> sortedMap = new LinkedHashMap<String, User>();
        for (Iterator<Map.Entry<String, User>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, User> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * Applies filters in parallel to a conversation & sequentially for each message
     * @param filters the filters used
     */
    public void applyFilters(Collection<Filter> filters) {

        ListIterator iterator = messages.listIterator();
        while (iterator.hasNext()) {
            Message message = (Message) iterator.next();

            for (Filter filter : filters) {

                message = filter.apply(message);
                if (message == null) {
                    iterator.remove();
                    break;
                }
            }

        }
    }

    /**
     * Initializes the users HashMap based on the List of Message objects
     */
    private void initializeUsers() {

        ListIterator iterator = messages.listIterator();
        while (iterator.hasNext()) {
            Message message = (Message) iterator.next();

            if (users.containsKey(message.getSenderId())) {
                users.get(message.getSenderId()).incrementActivity();
            } else {
                User user = new User(message.getSenderId());
                users.put(message.getSenderId(), user);
            }
        }

    }


    /* Setters */

    public void setFilters(Collection<Filter> filters) {
        this.filters = filters;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Getters */

    public String getName() {
        return name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Collection<Filter> getFilters() {
        return filters;
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
