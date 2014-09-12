/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author holzensp
 */
public interface Term extends PrettyPrintable {
    /* Analysis methods */
    public void unify(Unifier unifier, Term wanted) throws UnificationException;
    public default <T extends Term> void collect(Collection<T> ts, Class<T> cls) {
        if(cls.isAssignableFrom(this.getClass()))
            ts.add(cls.cast(this));
        else
            children().stream().forEach(c -> c.collect(ts, cls));
    }
    /* For copying of terms */
    public Collection<Term> children();
    public Term copyWithChildren(Collection<Term> chlds);
    
    public default <T extends Term, R extends Term> Term copyWith(Function<T,R> r, Class<T> cls) {
        if(cls.isAssignableFrom(this.getClass())) {
            return r.apply(cls.cast(this));
        } else {
            List<Term> chlds = new ArrayList();
            children().stream().map(c -> c.copyWith(r,cls)).forEach(chlds::add);
            return this.copyWithChildren(chlds);
        }
    }
}
