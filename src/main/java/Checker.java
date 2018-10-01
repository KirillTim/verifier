import buchi.BuchiAutomaton;
import buchi.GeneralizedLabeledBuchiAutomaton;
import buchi.Node;
import buchi.Symbol;
import formula.LTLFormula;
import formula.Not;
import input.Automaton;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class Checker {

    private final ArrayList<Pair> path1 = new ArrayList<>();
    private final Set<Pair> pathSet1 = new HashSet<>();

    private final ArrayList<Symbol> transitions1 = new ArrayList<>();

    private final Set<Pair> visited2 = new HashSet<>();
    private final ArrayList<Symbol> transitions2 = new ArrayList<>();

    private boolean foundPath = false;
    private ArrayList<Symbol> path = null;
    private int cycleStart = -1;

    public final BuchiAutomaton fromAutomaton;
    public final BuchiAutomaton fromLtl;

    public static Checker create(String automationFile, String ltlFormula) {
        final Automaton automaton;
        try {
            automaton = Automaton.fromXml(automationFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalArgumentException("Can't parse xml file", e);
        }
        final LTLFormula formula = new Not(LTLFormulaBuilder.build(ltlFormula));
        final BuchiAutomaton buchi = BuchiAutomaton.fromAutomation(automaton);
        final GeneralizedLabeledBuchiAutomaton glba = GeneralizedLabeledBuchiAutomaton.fromLTLFormula(formula, buchi.atomPropositions);
        final BuchiAutomaton buchiFromLTL = BuchiAutomaton.fromGLBA(glba);
        return new Checker(buchi, buchiFromLTL);
    }

    public Checker(BuchiAutomaton fromAutomaton, BuchiAutomaton fromLtl) {
        this.fromAutomaton = fromAutomaton;
        this.fromLtl = fromLtl;
    }

    public Answer verify() {
        /*System.out.println("Automation:");
        System.out.println(fromAutomaton);
        System.out.println("LTL:");
        System.out.println(fromLtl);*/
        for (Node q1 : fromAutomaton.start) {
            for (Node q2 : fromLtl.start) {
                outerDFS(new Pair(q1, q2));
                if (foundPath) {
                    return new Error(path, cycleStart);
                }
            }
        }
        return new OK();
    }

    private void innerDFS(final Pair q) {
        //System.out.println("innerDFS(" + q.first + ", " + q.second + ")");
        if (foundPath) return;
        visited2.add(q);
        commonDFS(q, (to, autoTransitionLabel) -> {
            if (path1.contains(to)) {
                foundPath = true;
                path = new ArrayList<>();
                path.addAll(transitions1);
                path.addAll(transitions2);
                path.add(autoTransitionLabel);
                cycleStart = path1.indexOf(to);
                return true;
            }
            if (!visited2.contains(to)) {
                transitions2.add(autoTransitionLabel);
                innerDFS(to);
                transitions2.remove(transitions2.size() - 1);
            }
            return false;
        });
        /*final Map<Symbol, List<Node>> automatonTransitions = fromAutomaton.delta.getOrDefault(q.first, constructLoop(q.first));
        final Map<Symbol, List<Node>> ltlTransitions = fromLtl.delta.getOrDefault(q.second, constructLoop(q.second));
        for (Symbol autoTransitionLabel : automatonTransitions.keySet()) {
            for (Symbol ltlTransitionLabel : ltlTransitions.keySet()) {
                if (autoTransitionLabel.subsetOf(ltlTransitionLabel)) {
                    final List<Node> autoNodes = automatonTransitions.getOrDefault(autoTransitionLabel, Collections.emptyList());
                    final List<Node> ltlNodes = ltlTransitions.getOrDefault(ltlTransitionLabel, Collections.emptyList());
                    for (Node autoNode : autoNodes) {
                        for (Node ltlNode : ltlNodes) {
                            final Pair to = new Pair(autoNode, ltlNode);
                            if (path1.contains(to)) {
                                foundPath = true;
                                path = new ArrayList<>();
                                path.addAll(transitions1);
                                path.addAll(transitions2);
                                path.add(autoTransitionLabel);
                                cycleStart = path1.indexOf(to);
                                return;
                            }
                            if (!visited2.contains(to)) {
                                transitions2.add(autoTransitionLabel);
                                innerDFS(to);
                                transitions2.remove(transitions2.size() - 1);
                            }
                        }
                    }
                }
            }
        }*/
    }

    private void commonDFS(final Pair q, BiFunction<Pair, Symbol, Boolean> body) {
        final Map<Symbol, List<Node>> automatonTransitions = fromAutomaton.delta.getOrDefault(q.first, constructLoop(q.first));
        final Map<Symbol, List<Node>> ltlTransitions = fromLtl.delta.getOrDefault(q.second, constructLoop(q.second));
        for (Symbol autoTransitionLabel : automatonTransitions.keySet()) {
            for (Symbol ltlTransitionLabel : ltlTransitions.keySet()) {
                if (autoTransitionLabel.subsetOf(ltlTransitionLabel)) {
                    final List<Node> autoNodes = automatonTransitions.getOrDefault(autoTransitionLabel, Collections.emptyList());
                    final List<Node> ltlNodes = ltlTransitions.getOrDefault(ltlTransitionLabel, Collections.emptyList());
                    for (Node autoNode : autoNodes) {
                        for (Node ltlNode : ltlNodes) {
                            final Pair to = new Pair(autoNode, ltlNode);
                            if (body.apply(to, autoTransitionLabel)) return;
                        }
                    }
                }
            }
        }
    }

    private void outerDFS(final Pair q) {
        //System.out.println("outerDFS(" + q.first + ", " + q.second + ")");
        if (foundPath) return;
        pathSet1.add(q);
        path1.add(q);
        commonDFS(q, (to, autoTransitionLabel) -> {
            if (!pathSet1.contains(to)) {
                transitions1.add(autoTransitionLabel);
                outerDFS(to);
                transitions1.remove(transitions1.size() - 1);
            }
            return false;
        });
//        final Map<Symbol, List<Node>> automatonTransitions = fromAutomaton.delta.getOrDefault(q.first, constructLoop(q.first));
//        final Map<Symbol, List<Node>> ltlTransitions = fromLtl.delta.getOrDefault(q.second, constructLoop(q.second));
//        for (Symbol autoTransitionLabel : automatonTransitions.keySet()) {
//            for (Symbol ltlTransitionLabel : ltlTransitions.keySet()) {
//                if (autoTransitionLabel.subsetOf(ltlTransitionLabel)) {
//                    final List<Node> autoNodes = automatonTransitions.getOrDefault(autoTransitionLabel, Collections.emptyList());
//                    final List<Node> ltlNodes = ltlTransitions.getOrDefault(ltlTransitionLabel, Collections.emptyList());
//                    for (Node autoNode : autoNodes) {
//                        for (Node ltlNode : ltlNodes) {
//                            final Pair to = new Pair(autoNode, ltlNode);
//                            if (!pathSet1.contains(to)) {
//                                transitions1.add(autoTransitionLabel);
//                                outerDFS(to);
//                                transitions1.remove(transitions1.size() - 1);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        if (fromAutomaton.finish.contains(q.first) && fromLtl.finish.contains(q.second)) {
            visited2.clear();
            innerDFS(q);
        }
        path1.remove(path1.size() - 1);
        pathSet1.remove(q);
    }

    private static Map<Symbol, List<Node>> constructLoop(final Node state) {
        final Map<Symbol, List<Node>> result = new HashMap<>();
        result.put(new Symbol(Collections.emptySet()), Collections.singletonList(state));
        return result;
    }

    abstract static class Answer {
    }

    public static class OK extends Answer {
    }

    public static class Error extends Answer {
        public final List<Symbol> path;
        public final int cycleStart;

        public Error(List<Symbol> path, int cycleStart) {
            this.path = path;
            this.cycleStart = cycleStart;
        }
    }

    private static class Pair {
        final Node first;
        final Node second;

        Pair(Node first, Node second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair pair = (Pair) o;
            return Objects.equals(first, pair.first) &&
                    Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
