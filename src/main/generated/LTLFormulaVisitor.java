// Generated from LTLFormula.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LTLFormulaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LTLFormulaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code next}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNext(LTLFormulaParser.NextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code negation}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(LTLFormulaParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(LTLFormulaParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(LTLFormulaParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code future}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuture(LTLFormulaParser.FutureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globally}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobally(LTLFormulaParser.GloballyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code release}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelease(LTLFormulaParser.ReleaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code implication}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplication(LTLFormulaParser.ImplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(LTLFormulaParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code until}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil(LTLFormulaParser.UntilContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesis(LTLFormulaParser.ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(LTLFormulaParser.BooleanLiteralContext ctx);
}