/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import reasonfx.term.Term;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author holzensp
 */
public class Wanted extends SimpleObjectProperty<Given> {
    private final Term term;
    
    public Wanted(Term t) { super(null); term = t; }
    
    public Term asTerm(){ return term; }
    
    public boolean isSatisfied() { return null != get(); }
    
    @Override public String toString() { return isSatisfied() ? get().toString() : term.toString(); }
    
    public boolean satsify(Given g) {
        if(g.unify(this)) {
            set(g);
            return true;
        }
        return false;
    }
    
    public void disconnect() {
        if(isSatisfied()) {
            Given g = get();
            set(null);
            g.disconnect();
        }
    }

}
