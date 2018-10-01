package buchi;

import formula.LTLFormula;
import util.Util;

import java.util.Set;

public class Symbol {
    public final Set<LTLFormula> min;
    public final Set<LTLFormula> max;

    public Symbol(Set<LTLFormula> min, Set<LTLFormula> max) {
        this.min = min;
        this.max = max;
    }

    public Symbol(Set<LTLFormula> minmax) {
        this(minmax, minmax);
    }

    public Symbol(LTLFormula f) {
        this(Util.setOf(f));
    }

    public boolean subsetOf(Symbol other) {
        return min.containsAll(other.min) && other.max.containsAll(max);
    }

    @Override
    public String toString() {
        if (min == max) return "Symbol: [" + min + "]";
        return "Symbol: [" + min + "," + max + "]";
    }
}
