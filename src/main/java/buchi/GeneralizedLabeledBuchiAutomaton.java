package buchi;

import formula.*;

import static util.Util.*;

import java.util.*;
import java.util.stream.Collectors;

public class GeneralizedLabeledBuchiAutomaton {
    public final Set<String> atomPropositions;
    public final List<Node> states;
    public final Map<Node, Symbol> labels;
    public final Map<Node, List<Node>> delta;
    public final List<Node> start;
    public final List<Set<Node>> finish;

    public GeneralizedLabeledBuchiAutomaton(Set<String> atomPropositions, List<Node> states, Map<Node, Symbol> labels, Map<Node, List<Node>> delta, List<Node> start, List<Set<Node>> finish) {
        this.atomPropositions = atomPropositions;
        this.states = states;
        this.labels = labels;
        this.delta = delta;
        this.start = start;
        this.finish = finish;
    }

    private static int expandCounter = 0;

    public static GeneralizedLabeledBuchiAutomaton fromLTLFormula(LTLFormula ltlFormula, Set<String> atomPropositions) {
        LTLFormula inNNF = LTLFormula.toNNF(ltlFormula);
        Node init = new Node("init");
        ArrayList<Node> nodes = new ArrayList<>();
        expandCounter = 0;
        expand(setOf(inNNF), Collections.emptySet(), Collections.emptySet(), setOf(init), new NodeNameGenerator(), nodes);
        return fromNodes(inNNF, nodes, init, atomPropositions);
    }

    private static GeneralizedLabeledBuchiAutomaton fromNodes(LTLFormula ltlFormula, List<Node> nodes, Node init, Set<String> atomPropositions) {
        final Set<Variable> atomVars = atomPropositions.stream().map(Variable::new).collect(Collectors.toSet());
        final HashMap<Node, Symbol> labels = new HashMap<>();
        for (Node node : nodes) {
            final Set<LTLFormula> minSet = new HashSet<>(node.now);
            minSet.retainAll(atomVars);
            final Set<LTLFormula> maxSet = atomVars.stream().filter(v -> !node.now.contains(new Not(v))).collect(Collectors.toSet());
            labels.put(node, new Symbol(minSet, maxSet));
        }

        final HashMap<Node, List<Node>> delta = new HashMap<>();
        for (Node node : nodes) {
            for (Node from : node.incoming) {
                if (from == init) continue;
                delta.merge(from, Collections.singletonList(node), (a, b) -> {
                    final ArrayList<Node> res = new ArrayList<>(a);
                    res.addAll(b);
                    return res;
                });
            }
        }

        final List<Node> start = nodes.stream().filter(n -> n.incoming.contains(init)).collect(Collectors.toList());
        final List<Set<Node>> finish = LTLFormula.closure(ltlFormula).stream()
                .filter(f -> f instanceof Until)
                .map(f -> nodes.stream()
                        .filter(n -> n.now.contains(((Until) f).rhs) || !n.now.contains(f))
                        .collect(Collectors.toSet())
                ).collect(Collectors.toList());
        return new GeneralizedLabeledBuchiAutomaton(atomPropositions, nodes, labels, delta, start, finish);
    }

    private static void expand(Set<LTLFormula> curr, Set<LTLFormula> old, Set<LTLFormula> next, Set<Node> incoming,
                               NodeNameGenerator nameGenerator, ArrayList<Node> nodes) {
        expandCounter++;
        /*System.out.println("expand #" + expandCounter + ":");
        System.out.println("curr:\n" + curr.stream().map(LTLFormula::toString).collect(Collectors.joining("\n")));
        System.out.println("old:\n" + old.stream().map(LTLFormula::toString).collect(Collectors.joining("\n")));
        System.out.println("next:\n" + next.stream().map(LTLFormula::toString).collect(Collectors.joining("\n")));
        System.out.println("incoming:\n" + incoming.stream().map(Node::toString).collect(Collectors.joining("\n")));
        System.out.println("--------------------");*/
        if (curr.isEmpty()) {
            Optional<Node> r = nodes.stream().filter(n -> contentEquals(n.next, next) && contentEquals(n.now, old)).findFirst();
            if (r.isPresent()) {
                r.get().incoming.addAll(incoming);
            } else {
                Node q = new Node(nameGenerator.next());
                nodes.add(q);
                q.incoming.addAll(incoming);
                q.now.addAll(old);
                q.next.addAll(next);
                expand(q.next, Collections.emptySet(), Collections.emptySet(), setOf(q), nameGenerator, nodes);
            }
        } else {
            final LTLFormula f = curr.iterator().next();
            final Set<LTLFormula> nCurr = new HashSet<>(curr);
            nCurr.remove(f);
            final Set<LTLFormula> nOld = new HashSet<>(old);
            nOld.add(f);
            if (isBase(f)) {
                if (f == Const.FALSE || nOld.contains(LTLFormula.negate(f))) return;
                expand(nCurr, nOld, next, incoming, nameGenerator, nodes);
            } else if (f instanceof And) {
                final Set<LTLFormula> additional = setOf(((And) f).lhs, ((And) f).rhs);
                additional.removeAll(nOld);
                nCurr.addAll(additional);
                expand(nCurr, nOld, next, incoming, nameGenerator, nodes);
            } else if (f instanceof Next) {
                final Set<LTLFormula> newNext = new HashSet<>(next);
                newNext.add(((Next) f).sub);
                expand(nCurr, nOld, newNext, incoming, nameGenerator, nodes);
            } else if (f instanceof Or || f instanceof Until || f instanceof Release) {
                final Set<LTLFormula> curr1Set = curr1(f);
                curr1Set.removeAll(nOld);
                curr1Set.addAll(nCurr);
                final Set<LTLFormula> newNext = new HashSet<>(next);
                newNext.addAll(next1(f));
                expand(curr1Set, nOld, newNext, incoming, nameGenerator, nodes);
                final Set<LTLFormula> curr2Set = curr2(f);
                curr2Set.removeAll(nOld);
                curr2Set.addAll(nCurr);
                expand(curr2Set, nOld, next, incoming, nameGenerator, nodes);
            } else {
                throw new IllegalArgumentException("Not in Negative Normal Form");
            }
        }
    }

    private static Set<LTLFormula> curr1(LTLFormula f) {
        if (f instanceof Until) return setOf(((Until) f).lhs);
        if (f instanceof Release || f instanceof Or) return setOf(((BinOp) f).rhs);
        throw new IllegalArgumentException("undefined for " + f);
    }

    private static Set<LTLFormula> curr2(LTLFormula f) {
        if (f instanceof Until) return setOf(((Until) f).rhs);
        if (f instanceof Release) return setOf(((Release) f).lhs, ((Release) f).rhs);
        if (f instanceof Or) return setOf(((Or) f).lhs);
        throw new IllegalArgumentException("undefined for " + f);
    }

    private static Set<LTLFormula> next1(LTLFormula f) {
        if (f instanceof Until || f instanceof Release) return setOf(f);
        if (f instanceof Or) return Collections.emptySet();
        throw new IllegalArgumentException("undefined for " + f);
    }

    private static boolean isBase(LTLFormula f) {
        return f instanceof Const || f instanceof Variable
                || (f instanceof Not && ((Not) f).sub instanceof Variable);
    }

    private static class NodeNameGenerator {
        private int id = 0;

        String next() {
            return "node #" + (++id);
        }
    }
}
