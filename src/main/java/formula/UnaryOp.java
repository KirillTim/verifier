package formula;

import java.util.Objects;

abstract public class UnaryOp extends LTLFormula{
    public final String name;
    public final LTLFormula sub;

    UnaryOp(String name, LTLFormula sub) {
        this.name = name;
        this.sub = sub;
    }

    @Override
    public String toString() {
        return name + " (" + sub.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnaryOp)) return false;
        UnaryOp unaryOp = (UnaryOp) o;
        return Objects.equals(name, unaryOp.name) &&
                Objects.equals(sub, unaryOp.sub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sub);
    }
}
