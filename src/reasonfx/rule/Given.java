/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

/**
 *
 * @author holzensp
 */
public interface Given {
    public Term asTerm();
    
    public default boolean unify(Wanted wanted) {
        final Term w = wanted.asTerm();
        try {
            this.asTerm().unify(this, w);
        } catch(UnificationException e) {
            return false;
        }
        return true;
    }

    public void register(RuleInstanceVariable v);
    public void register(Given g);
    
    public void reUnify();
}
