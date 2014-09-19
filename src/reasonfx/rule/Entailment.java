/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import reasonfx.term.Term;
import java.util.Collection;
import java.util.stream.Stream;

/**
 *
 * @author holzensp
 */
public interface Entailment {
    public int renumber(int prettyID);
    public Collection<Term> getPremisses();
    public Term getConclusion();
    
    public default <T> Stream<T> collect(Class<T> cls) {
        Stream<T> chlds = getPremisses().stream().flatMap(t -> t.collect(cls));
        return (cls.isAssignableFrom(getConclusion().getClass())
               ? Stream.concat(chlds,Stream.of((T)getConclusion()))
               : chlds
               ).distinct();
    }
}
