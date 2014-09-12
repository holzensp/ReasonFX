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
public interface Entailment {
    public Collection<Term> getPremisses();
    public Term getConclusion();
}
