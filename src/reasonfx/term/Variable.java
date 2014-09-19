/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import java.util.Collection;
import reasonfx.util.EmptyCollection;

/**
 *
 * @author holzensp
 * @param <T>
 */
public interface Variable<T extends Variable> extends Term, Comparable<T> {
    public int getID();

    @Override
    public default int compareTo(T that) {
        return Integer.compare(this.getID(), that.getID());
    }
    
    // default copying from Term; Variables really can't be copied; they're unique
    @Override public default Collection<Term> children() { return new EmptyCollection(); }
    @Override public default Term copyWithChildren(Collection<Term> chlds) { return this; }
}
