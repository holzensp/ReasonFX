/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author holzensp
 */
public class ProofStep {
    private final Wanted wanted;
    private final Given  given;
    
    public ProofStep(Wanted w, Given g) {
        wanted = w;
        given  = g;
    }

    void notifyUnbinding(RuleInstanceVariable aThis) {
        try {
            given.unify(wanted);
        } catch (DoubleBindingException ex) {
            Logger.getLogger(ProofStep.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
}
