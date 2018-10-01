package input;

@SuppressWarnings("WeakerAccess")
public class Event {
    public final String name;
    public final String comment;

    Event(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }
}
