package formula;

public class And extends BinOp {
    public And(LTLFormula lhs, LTLFormula rhs) {
        super("&&", lhs, rhs);
    }
}
