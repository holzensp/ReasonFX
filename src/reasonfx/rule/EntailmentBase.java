/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 *
 * @author holzensp
 */
public class EntailmentBase extends GivenImpl implements Entailment, PrettyPrintable {
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

    @Override public String toString() { return dbgString(); }
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

    @Override
    public void renumber(int prettyID) {
        int i = 0;
        for(Term r : getPremisses()) {
            r.postOrder().forEach((Consumer<Term>) (t -> {
                if(RuleInstanceVariable.class.isAssignableFrom(t.getClass())) {
                    RuleInstanceVariable v = (RuleInstanceVariable) t;
                    if(!v.isBound()) {
                        v.setPrettyID(v.getID()+prettyID);
                    } 
                }
            }));
        }
    }

}
