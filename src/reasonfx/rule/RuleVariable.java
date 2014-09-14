/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Collection;
import reasonfx.util.EmptyCollection;

/**
 *
 * @author holzensp
 */
public class RuleVariable extends Variable implements Comparable<RuleVariable> {
    private static final int varCodePoints[] = "φψ".codePoints().toArray();
    private static final int DigitOffset = 0x2080 - Character.codePointAt("0",0);
    
    protected static String mkString(final int id) {
        StringBuilder b = new StringBuilder()
            .appendCodePoint(varCodePoints[id % varCodePoints.length]);
        if(id >= varCodePoints.length) {
            String.valueOf(id / varCodePoints.length)
                .codePoints()
                .map(i -> DigitOffset + i)
                .forEach(b::appendCodePoint);
        }
        return b.toString();
    }

    public    RuleVariable(int varID)         { super(varID); }
    protected RuleVariable(RuleVariable that) { this(that.getID()); }

    @Override
    public void unify(Given unifier, Term wanted) throws UnificationException {
        throw new UnsupportedOperationException("RuleVariables may *never* be unified.");
    }

    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        result.append(mkString(this.getID()));
    }

    @Override public Collection<Term> children() { return new EmptyCollection(); }
    @Override public Term copyWithChildren(Collection<Term> chlds) { return new RuleVariable(this); }

    @Override
    public int compareTo(RuleVariable that) {
        return Integer.compare(this.getID(), that.getID());
    }
}
