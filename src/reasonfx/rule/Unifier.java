/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.rule;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author holzensp
 */
public class Unifier extends TreeMap<RuleInstanceVariable, Term> implements PrettyPrintable {
    @Override public String toString() { return show(); }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(size() == 0) {
            result.append("\u2205");
        } else {
            result.append('{');
            String glue = "";
            for(Entry<RuleInstanceVariable,Term> e : entrySet()) {
                result.append(glue);
                e.getKey().prettyPrint(result, prec, debugging);
                result.append(" => ");
                e.getValue().prettyPrint(result, prec, debugging);
                glue = ", ";
            }
            result.append('}');
        }
    }
    
}
