/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.stream.Collectors;

/**
 *
 * @author holzensp
 */
public class RuleInstance extends EntailmentBase {
    public RuleInstance(Rule bluePrint) {
        super(
            bluePrint.getConclusion()
                .copyWith(RuleInstanceVariable::new, RuleVariable.class),
            bluePrint.getPremisses().stream()
                .map(t -> t.copyWith(RuleInstanceVariable::new,RuleVariable.class))
                .collect(Collectors.toList())
        );
    }
    
    @Override public String toString() { return show(); }
}
