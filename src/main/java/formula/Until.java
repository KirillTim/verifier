package formula;

public class Until extends BinOp {
    public Until(LTLFormula lhs, LTLFormula rhs) {
        super("U", lhs, rhs);
    }
}
