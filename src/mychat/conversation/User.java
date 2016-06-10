package mychat.conversation;

/**
 * Represents a User - name + activity
 */
 class User {

    private String name;
    private int activity = 1; // initial activity

    public User(String name) {
        this.name = name;
    }

    public int getActivity() {
        return activity;
    }

    public void incrementActivity() {
        activity++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
