package formula;

import java.util.HashSet;
import java.util.Set;

public abstract class LTLFormula {
    public static LTLFormula negate(LTLFormula f) {
        if (f == Const.FALSE) return Const.TRUE;
        if (f == Const.TRUE) return Const.FALSE;
        if (f instanceof Not) return ((Not) f).sub;
        return new Not(f);
    }

    public static LTLFormula toNNF(LTLFormula f) {
        if (f instanceof Not) {
            final LTLFormula sub = ((Not) f).sub;
            if (sub == Const.TRUE) return Const.FALSE;
            if (sub == Const.FALSE) return Const.TRUE;
            if (sub instanceof Variable) return f;

            if (sub instanceof Not) return toNNF(((Not) sub).sub);

            if (sub instanceof And) return new Or(toNNF(new Not(((And) sub).lhs)), toNNF(new Not(((And) sub).rhs)));
            if (sub instanceof Or) return new And(toNNF(new Not(((Or) sub).lhs)), toNNF(new Not(((Or) sub).rhs)));

            if (sub instanceof Next) return toNNF(new Next(new Not(((Next) sub).sub)));
            if (sub instanceof Future) return toNNF(new Not(new Until(Const.TRUE, ((Future) sub).sub)));
            if (sub instanceof Globally) return toNNF(new Not(new Release(Const.FALSE, ((Globally) sub).sub)));

            if (sub instanceof Until) return new Release(toNNF(new Not(((Until) sub).lhs)), toNNF(new Not(((Until) sub).rhs)));
            if (sub instanceof Release) return new Until(toNNF(new Not(((Release) sub).lhs)), toNNF(new Not(((Release) sub).rhs)));
        }

        if (f instanceof Variable) return f;
        if (f instanceof Const) return f;

        if (f instanceof And) return new And(toNNF(((And) f).lhs), toNNF(((And) f).rhs));
        if (f instanceof Or) return new Or(toNNF(((Or) f).lhs), toNNF(((Or) f).rhs));

        if (f instanceof Next) return new Next(toNNF(((Next) f).sub));
        if (f instanceof Future) return toNNF(new Until(Const.TRUE, ((Future) f).sub));
        if (f instanceof Globally) return toNNF(new Release(Const.FALSE, ((Globally) f).sub));

        if (f instanceof Until) return new Until(toNNF(((Until) f).lhs), toNNF(((Until) f).rhs));
        if (f instanceof Release) return new Release(toNNF(((Release) f).lhs), toNNF(((Release) f).rhs));

        throw new IllegalStateException();
    }

    public static Set<LTLFormula> closure(LTLFormula f) {
        return new ClosureGenerator().generate(f).closure;
    }

    private static class ClosureGenerator {
        final Set<LTLFormula> closure = new HashSet<>();

        ClosureGenerator() {
            closure.add(Const.FALSE);
            closure.add(Const.TRUE);
        }

        ClosureGenerator generate(LTLFormula f) {
            closure.add(f);
            closure.add(toNNF(negate(f)));
            if (f instanceof UnaryOp) {
                generate(((UnaryOp) f).sub);
            } else if (f instanceof BinOp) {
                generate(((BinOp) f).lhs);
                generate(((BinOp) f).rhs);
            }
            return this;
        }
    }
}