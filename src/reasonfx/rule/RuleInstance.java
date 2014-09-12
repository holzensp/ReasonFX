/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.function.Function;

/**
 *
 * @author holzensp
 */
public class RuleInstance extends EntailmentBase {
    private static final Function<RuleVariable,RuleInstanceVariable> newVar = RuleInstanceVariable::new;
    public RuleInstance(Rule bluePrint) {
        super( (Term) bluePrint.getConclusion().copyWith(newVar, RuleVariable.class)
             , new Term[bluePrint.getPremisses().size()]);
        int i = 0;
        
        for(Term t : bluePrint.getPremisses()) {
            this.premisses[i++] = (Term) t.copyWith(newVar, RuleVariable.class);
        }
    }
}
