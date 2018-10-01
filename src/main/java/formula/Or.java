package formula;

public class Or extends BinOp {
    public Or(LTLFormula lhs, LTLFormula rhs) {
        super("||", lhs, rhs);
    }
}
