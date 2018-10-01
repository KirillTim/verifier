import buchi.Symbol;

public class Main {
    public static void main(String[] args) {
        String usage = "Usage: java -jar checker.jar <path to XML file containing automation> <ltl formula>";
        if (args.length < 2) {
            System.out.println(usage);
            System.exit(1);
        }
        final Checker checker = Checker.create(args[0], args[1]);
        final Checker.Answer answer = checker.verify();
        if (answer instanceof Checker.OK) {
            System.out.println("Correct");
        } else {
            System.out.println("Incorrect, counter example:");
            for (int i = 0; i < ((Checker.Error) answer).path.size(); i++) {
                final Symbol symbol = ((Checker.Error) answer).path.get(i);
                assert symbol.min == symbol.max;
                System.out.println(i +": " + symbol.min);
            }
            System.out.println("Cycle start from index: " + ((Checker.Error) answer).cycleStart);
        }
    }

    //public static
}
