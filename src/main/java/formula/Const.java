package formula;

public abstract class Const extends LTLFormula {
    abstract boolean getValue();

    @Override
    public String toString() {
        return getValue() ? "True" : "False";
    }

    public static Const TRUE = new True();
    public static Const FALSE = new False();

    private static class True extends Const {
        @Override
        boolean getValue() {
            return true;
        }
    }

    private static class False extends Const {
        @Override
        boolean getValue() {
            return false;
        }
    }
}
