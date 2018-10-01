package input;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Transition {
    public final int id;
    public final String event;
    public final String comment;
    public final List<String> actions;
    public final String guard; //not used
    public final String code; //not used

    public Transition(int id, String event, String comment, String guard, List<String> actions, String code) {
        this.id = id;
        this.event = event;
        this.comment = comment;
        this.guard = guard;
        this.actions = actions;
        this.code = code;
    }
}
