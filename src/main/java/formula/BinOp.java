package formula;

import java.util.Objects;

public abstract class BinOp extends LTLFormula {
    public final String name;
    public final LTLFormula lhs;
    public final LTLFormula rhs;

    BinOp(String name, LTLFormula lhs, LTLFormula rhs) {
        this.name = name;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return "(" + lhs + ") " + name + " (" + rhs + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinOp)) return false;
        BinOp binOp = (BinOp) o;
        return Objects.equals(name, binOp.name) &&
                Objects.equals(lhs, binOp.lhs) &&
                Objects.equals(rhs, binOp.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lhs, rhs);
    }
}
