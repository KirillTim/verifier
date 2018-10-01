import formula.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class LTLFormulaBuilder extends LTLFormulaBaseVisitor<LTLFormula> {

    private LTLFormulaBuilder() {
    }

    public static LTLFormula build(String string) {
        final LTLFormulaLexer lexer = new LTLFormulaLexer(new ANTLRInputStream(string));
        final LTLFormulaParser parser = new LTLFormulaParser(new CommonTokenStream(lexer));
        return new LTLFormulaBuilder().visit(parser.formula());
    }

    @Override
    public LTLFormula visitParenthesis(LTLFormulaParser.ParenthesisContext ctx) {
        return visit(ctx.getChild(1));
    }

    @Override
    public LTLFormula visitNegation(LTLFormulaParser.NegationContext ctx) {
        return new Not(visit(ctx.formula()));
    }

    @Override
    public LTLFormula visitNext(LTLFormulaParser.NextContext ctx) {
        return new Next(visit(ctx.formula()));
    }

    @Override
    public LTLFormula visitFuture(LTLFormulaParser.FutureContext ctx) {
        return new Future(visit(ctx.formula()));
    }

    @Override
    public LTLFormula visitGlobally(LTLFormulaParser.GloballyContext ctx) {
        return new Globally(visit(ctx.formula()));
    }

    @Override
    public LTLFormula visitUntil(LTLFormulaParser.UntilContext ctx) {
        return new Until(visit(ctx.lhs), visit(ctx.rhs));
    }

    @Override
    public LTLFormula visitRelease(LTLFormulaParser.ReleaseContext ctx) {
        return new Release(visit(ctx.lhs), visit(ctx.rhs));
    }

    @Override
    public LTLFormula visitConjunction(LTLFormulaParser.ConjunctionContext ctx) {
        return new And(visit(ctx.lhs), visit(ctx.rhs));
    }

    @Override
    public LTLFormula visitDisjunction(LTLFormulaParser.DisjunctionContext ctx) {
        return new Or(visit(ctx.lhs), visit(ctx.rhs));
    }

    @Override
    public LTLFormula visitImplication(LTLFormulaParser.ImplicationContext ctx) {
        return new Or(new Not(visit(ctx.lhs)), visit(ctx.rhs));
    }

    @Override
    public LTLFormula visitVariable(LTLFormulaParser.VariableContext ctx) {
        return new Variable(ctx.getText());
    }

    @Override
    public LTLFormula visitBooleanLiteral(LTLFormulaParser.BooleanLiteralContext ctx) {
        boolean val = Boolean.parseBoolean(ctx.getText());
        return val ? Const.TRUE : Const.FALSE;
    }
}
