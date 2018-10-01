package input;

import java.util.List;

public class State {
    public final int id;
    public final String name;
    public final int type;

    public final List<Integer> incoming;
    public final List<Integer> outgoing;

    public State(int id, String name, int type, List<Integer> incoming, List<Integer> outgoing) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }
}
