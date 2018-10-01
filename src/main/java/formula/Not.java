package formula;

public class Not extends UnaryOp {
    public Not(LTLFormula sub) {
        super("!", sub);
    }
}
