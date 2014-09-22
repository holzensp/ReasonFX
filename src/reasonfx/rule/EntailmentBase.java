/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import reasonfx.term.Term;
import reasonfx.term.RuleInstanceVariable;
import reasonfx.util.PrettyPrintable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author holzensp
 */
public class EntailmentBase extends Given implements Entailment, PrettyPrintable {
    protected final Collection<Term> premisses;
    
    public EntailmentBase(Term c, Term... ps) {
        this(c,Arrays.asList(ps));
    }
    
    public EntailmentBase(Term c, Collection<Term> ps) {
        super(c);
        premisses = ps;
    }
    
    @Override public Collection<Term> getPremisses() { return premisses; }
    @Override public Term getConclusion()            { return asTerm(); }

    @Override public String toString() { return asStringExpression(-1).get(); }
/*
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        String glue = "";
        for(Term t : getPremisses()) {
            result.append(glue);
            t.prettyPrint(result, -1, debugging);
            glue = ", ";
        }
        result.append(" |- ");
        getConclusion().prettyPrint(result, -1, debugging);
    }
*/
    
    @Override
    public StringExpression asStringExpression(int prec) {
        StringExpression result = new ReadOnlyStringWrapper("");
        String glue = "";
        for(Term t : getPremisses()) {
            result = result.concat(glue)
                           .concat(t.asStringExpression(-1));
            glue = ", ";
        }
        return result.concat(" |- ").concat(getConclusion().asStringExpression(-1));
    }

    @Override
    public int renumber(int prettyID) {
        Iterator<RuleInstanceVariable> vs = collect(RuleInstanceVariable.class)
                .filter(v -> !v.isUnified())
                .distinct()
                .iterator();
        while(vs.hasNext())
            vs.next().setPrettyID(prettyID++);
        return prettyID;
    }

}
