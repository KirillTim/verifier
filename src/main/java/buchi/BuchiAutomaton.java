package buchi;

import formula.Variable;
import input.Automaton;
import input.State;
import input.Transition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.Util.setOf;

public class BuchiAutomaton {
    public final Set<String> atomPropositions;
    public final Set<Node> states;
    public final Set<Node> start;
    public final Set<Node> finish;
    public final Map<Node, Map<Symbol, List<Node>>> delta;

    public BuchiAutomaton(Set<String> atomPropositions, Set<Node> states, Set<Node> start, Set<Node> finish, Map<Node, Map<Symbol, List<Node>>> delta) {
        this.atomPropositions = atomPropositions;
        this.states = states;
        this.start = start;
        this.finish = finish;
        this.delta = delta;
    }

    private StringBuilder transitionsString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Node, Map<Symbol, List<Node>>> entry : delta.entrySet()) {
            final Node from = entry.getKey();
            final Map<Symbol, List<Node>> transitions = entry.getValue();
            for (Map.Entry<Symbol, List<Node>> transition : transitions.entrySet()) {
                final Symbol symbol = transition.getKey();
                String toStr = transition.getValue().stream().map(Node::toString).collect(Collectors.joining(", "));
                builder.append(from).append(" -> ").append(symbol).append("->").append(toStr).append("\n");
            }
        }
        return builder;
    }

    @Override
    public String toString() {
        return "BuchiAutomaton:" +
                "\nAP:\n" +
                String.join(", ", atomPropositions) +
                "\nStates:\n" +
                states.stream().map(Node::toString).collect(Collectors.joining(", ")) +
                "\nStart:\n" +
                start.stream().map(Node::toString).collect(Collectors.joining(", ")) +
                "\nFinish:\n" +
                finish.stream().map(Node::toString).collect(Collectors.joining(", ")) +
                "\nTransitions\n" +
                transitionsString();
    }

    public static BuchiAutomaton fromGLBA(GeneralizedLabeledBuchiAutomaton glba) {
        final HashSet<Node> nodes = new HashSet<>();
        final HashSet<Node> start = new HashSet<>();
        final HashSet<Node> finish = new HashSet<>();
        final HashMap<Node, Map<Symbol, List<Node>>> delta = new HashMap<>();

        for (Node node : glba.states) {
            for (int i = 1; i <= glba.finish.size(); i++) {
                final CountingNode countingNode = new CountingNode(node, i);
                nodes.add(countingNode);
                if (i == 1 && glba.start.contains(node)) {
                    start.add(countingNode);
                }
                if (i == 1 && glba.finish.get(0).contains(node)) {
                    finish.add(countingNode);
                }
            }
        }

        for (Node state : glba.states) {
            final Symbol label = glba.labels.get(state);
            if (label == null) throw new IllegalArgumentException("No label for " + state);
            final List<Node> toList = glba.delta.getOrDefault(state, Collections.singletonList(state));
            for (int i = 0; i < glba.finish.size(); i++) {
                final Set<Node> f = glba.finish.get(i);
                int fromCount = i + 1;
                int toCount = f.contains(state) ? (fromCount % glba.finish.size()) + 1 : fromCount;
                for (Node toNode : toList) {
                    final CountingNode from = new CountingNode(state, fromCount);
                    final CountingNode to = new CountingNode(toNode, toCount);
                    addTransition(delta, from, label, to);
                }
            }
        }
        return new BuchiAutomaton(glba.atomPropositions, nodes, start, finish, delta);
    }

    public static BuchiAutomaton fromAutomation(Automaton automaton) {
        final Set<String> atomPropositions = extractVariables(automaton);
        final HashSet<Node> states = new HashSet<>();
        final HashSet<Node> start = new HashSet<>();
        final HashMap<Integer, Trans> nodesByStateId = new HashMap<>();
        for (State state : automaton.states) {
            final Trans trans = new Trans(new Node(state.name + "_enter"), new Node(state.name));
            nodesByStateId.put(state.id, trans);
            states.add(trans.from);
            states.add(trans.to);
            if (state.type == 1) {
                start.add(trans.from);
            }
        }

        final HashMap<Node, Map<Symbol, List<Node>>> delta = new HashMap<>();
        for (Trans trans : nodesByStateId.values()) {
            final Node enterState = trans.from;
            final Node state = trans.to;
            addTransition(delta, enterState, new Symbol(new Variable(state.name)), state);
        }
        for (Transition transition : automaton.transitions) {
            Optional<State> automatonTransitionStart = automaton.states.stream().filter(s -> s.outgoing.contains(transition.id)).findFirst();
            if (!automatonTransitionStart.isPresent()) throw new IllegalArgumentException("no start for " + transition);
            Optional<State> automatonTransitionEnd = automaton.states.stream().filter(s -> s.incoming.contains(transition.id)).findFirst();
            if (!automatonTransitionEnd.isPresent()) throw new IllegalArgumentException("no end for " + transition);
            final Node from = nodesByStateId.get(automatonTransitionStart.get().id).to; //huh
            final Node to = nodesByStateId.get(automatonTransitionEnd.get().id).from;

            final Variable eventVariable = new Variable(transition.event);
            final Variable stateVariable = new Variable(from.name);
            final Symbol eventLabel = new Symbol(setOf(stateVariable, eventVariable));
            final Node eventTransitionEnd;
            if (transition.actions.isEmpty()) {
                eventTransitionEnd = to;
            } else {
                eventTransitionEnd = new Node("temp_" + transition.event);
                states.add(eventTransitionEnd);
            }
            addTransition(delta, from, eventLabel, eventTransitionEnd);

            Node transitionStart = eventTransitionEnd;
            for (int i = 0; i < transition.actions.size(); i++) {
                final String action = transition.actions.get(i);
                final Variable actionVariable = new Variable(action);
                final Node transitionEnd;
                if (i == transition.actions.size() - 1) {
                    transitionEnd = to;
                } else {
                    transitionEnd = new Node("temp_" + action);
                    states.add(transitionEnd);
                }
                final Symbol label = new Symbol(setOf(stateVariable, eventVariable, actionVariable));
                addTransition(delta, transitionStart, label, transitionEnd);
                transitionStart = transitionEnd;
            }
        }
        return new BuchiAutomaton(atomPropositions, states, start, states, delta);
    }

    private static class Trans {
        public final Node from;
        public final Node to;

        public Trans(Node from, Node to) {
            this.from = from;
            this.to = to;
        }
    }

    private static Set<String> extractVariables(Automaton automaton) {
        return Stream.of(
                automaton.states.stream().map(s -> s.name),
                automaton.events.stream().map(s -> s.name),
                automaton.transitions.stream().map(t -> t.event),
                automaton.transitions.stream().flatMap(t -> t.actions.stream())
        ).flatMap(s -> s).collect(Collectors.toSet());
    }

    static class CountingNode extends Node {
        public final Node node;
        public final int n;

        public CountingNode(Node node, int n) {
            super(node);
            this.node = node;
            this.n = n;
        }

        @Override
        public String toString() {
            return "CountingNode{node=" + node + ", n=" + n + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CountingNode)) return false;
            CountingNode that = (CountingNode) o;
            return n == that.n &&
                    Objects.equals(node, that.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, n);
        }
    }

    private static void addTransition(HashMap<Node, Map<Symbol, List<Node>>> delta,
                                      Node start, Symbol symbol, Node end) {
        //System.out.println("addTransition(" + start +", " + symbol +", "+ end +")");
        final Map<Symbol, List<Node>> fromStart = delta.computeIfAbsent(start, k -> new HashMap<>());
        final List<Node> endNodes = fromStart.computeIfAbsent(symbol, k -> new ArrayList<>());
        endNodes.add(end);
    }
}
