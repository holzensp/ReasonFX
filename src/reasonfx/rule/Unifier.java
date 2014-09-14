/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.rule;

import java.util.Collection;

/**
 *
 * @author holzensp
 */
public class Unifier implements PrettyPrintable {
    public final Collection<RuleInstanceVariable> bnds;
    
    public Unifier(Collection<RuleInstanceVariable> bs) { bnds = bs; }
    
    @Override public String toString() { return dbgString(); }

    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(bnds.isEmpty()) {
            result.appendCodePoint(0x2205);
        } else {
            result.append('{');
            String glue = "";
            for(RuleInstanceVariable v : bnds) {
                result.append(glue);
                v.prettyPrint(result, prec, debugging);
                glue = ", ";
            }
            result.append('}');
        }
    }
}
