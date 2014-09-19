/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author holzensp
 */
public class RuleVariable implements UnificationVariable<RuleVariable> {
    private static int UniqueIDs = 0;
    private final int ruleLocalID;
    private final int varID;
    private final ReadOnlyStringWrapper pretty;

    public RuleVariable(int ruleID) {
        varID = UniqueIDs++;
        ruleLocalID = ruleID;
        pretty = new ReadOnlyStringWrapper(UnificationVariable.mkString(ruleID));
    }

    @Override public int getID() { return varID; }
    public int getRuleLocalID()  { return ruleLocalID; }

    @Override
    public void unify(Given unifier, Term wanted) throws UnificationException {
        throw new UnsupportedOperationException("RuleVariables may *never* be unified.");
    }
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        result.append(UnificationVariable.mkString(this.getID()));
    }
}
