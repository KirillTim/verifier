package formula;

public class Release extends BinOp {
    public Release(LTLFormula lhs, LTLFormula rhs) {
        super("R", lhs, rhs);
    }
}
