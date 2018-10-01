import org.junit.Assert;

abstract class TestBase {
    abstract String XML();

    void doTestOK(final String ltlFormula) {
        final Checker.Answer answer = Checker.create(XML(), ltlFormula).verify();
        Assert.assertTrue(answer instanceof Checker.OK);
    }

    void doTestCycle(final String ltlFormula) {
        final Checker.Answer answer = Checker.create(XML(), ltlFormula).verify();
        Assert.assertTrue(answer instanceof Checker.Error);
    }

//    void doTestCycle(final String ltlFormula, final List<Symbol> expectedPath, final int expectedCycleStart) {
//        final Checker.Answer answer = Checker.create(XML(), ltlFormula).verify();
//        Assert.assertTrue(answer instanceof Checker.Error);
//        final List<Symbol> path = ((Checker.Error) answer).path;
//        final int cycleStart = ((Checker.Error) answer).cycleStart;
//        path.forEach(s -> {
//            Assert.assertSame(s.min, s.max);
//        });
//        Assert.assertSame(path.stream().map(s -> s.min).collect(Collectors.toList()), expectedPath);
//        Assert.assertEquals(cycleStart, expectedCycleStart);
//    }
//
//    protected static List<Symbol> path(String... variables) {
//        return Arrays.stream(variables).map(Variable::new).map(v -> new Symbol(setOf(v))).collect(Collectors.toList());
//    }
}
