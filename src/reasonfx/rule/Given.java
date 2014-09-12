/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author holzensp
 */
public interface Given {
    public Term asTerm();
    
    public default boolean unify(Wanted wanted) throws DoubleBindingException {
        final Term w = wanted.asTerm();
        final Unifier unifier = new Unifier();
        try {
            this.asTerm().unify(unifier, w);
        } catch(UnificationException e) {
            return false;
        }
        ProofStep result = new ProofStep(wanted, this);
        for(RuleInstanceVariable v : unifier.keySet()) {
            v.bind(unifier.get(v), result);
        }
        return true;
    }
}
