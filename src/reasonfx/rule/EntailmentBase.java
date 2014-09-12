/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author holzensp
 */
public class EntailmentBase implements Entailment {
    protected final Term[] premisses;
    protected final Term   conclusion;
    
    public EntailmentBase(Term c, Term... ps) {
        premisses  = ps;
        conclusion = c;
    }
    
    @Override public Collection<Term> getPremisses() { return Arrays.asList(premisses); }
    @Override public Term getConclusion()            { return conclusion; }
}