package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Util {
    private Util() {}
    @SafeVarargs
    public static <E> Set<E> setOf(E... item) {
        return new HashSet<>(Arrays.asList(item));
    }

    public static <E> boolean contentEquals(Set<E> first, Set<E> second) {
        return first.containsAll(second) && second.containsAll(first);
    }
}
