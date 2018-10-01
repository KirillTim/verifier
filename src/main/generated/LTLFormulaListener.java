// Generated from LTLFormula.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LTLFormulaParser}.
 */
public interface LTLFormulaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code next}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterNext(LTLFormulaParser.NextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code next}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitNext(LTLFormulaParser.NextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code negation}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterNegation(LTLFormulaParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code negation}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitNegation(LTLFormulaParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(LTLFormulaParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(LTLFormulaParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(LTLFormulaParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(LTLFormulaParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code future}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFuture(LTLFormulaParser.FutureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code future}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFuture(LTLFormulaParser.FutureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globally}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterGlobally(LTLFormulaParser.GloballyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globally}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitGlobally(LTLFormulaParser.GloballyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code release}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterRelease(LTLFormulaParser.ReleaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code release}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitRelease(LTLFormulaParser.ReleaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code implication}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterImplication(LTLFormulaParser.ImplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code implication}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitImplication(LTLFormulaParser.ImplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterVariable(LTLFormulaParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitVariable(LTLFormulaParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code until}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterUntil(LTLFormulaParser.UntilContext ctx);
	/**
	 * Exit a parse tree produced by the {@code until}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitUntil(LTLFormulaParser.UntilContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis(LTLFormulaParser.ParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis(LTLFormulaParser.ParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(LTLFormulaParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link LTLFormulaParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(LTLFormulaParser.BooleanLiteralContext ctx);
}