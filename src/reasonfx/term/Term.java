/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import reasonfx.util.PrettyPrintable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import reasonfx.rule.Given;
import reasonfx.rule.UnificationException;

/**
 *
 * @author holzensp
 */
public interface Term extends PrettyPrintable {
    /* Analysis methods */
    public default void unify(Given unifier, Term wanted) throws UnificationException {
        if(this == wanted) return;
        if(UnificationVariable.class.isAssignableFrom(wanted.getClass()))
            wanted.unifyImpl(unifier, this);
        else
            this.unifyImpl(unifier,wanted);
    }
    
    public void unifyImpl(Given unifier, Term wanted) throws UnificationException;
    
    public default <T> Stream<T> collect(Class<T> cls) {
        Stream<T> chlds = children().stream().flatMap(t -> t.collect(cls));
        Stream<T> res = cls.isAssignableFrom(this.getClass())
                ? Stream.concat(Stream.of((T)this), chlds)
                : chlds;
        return res.distinct();
    }
    
    public default Stream<Term> postOrder() {
        return Stream.concat(children().stream().flatMap(Term::postOrder), Stream.of(this));
    }
    
    public default Stream<Term> preOrder() {
        return Stream.concat(Stream.of(this), children().stream().flatMap(Term::preOrder));
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
