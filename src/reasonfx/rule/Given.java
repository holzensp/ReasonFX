/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import reasonfx.term.Term;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import reasonfx.term.UnificationVariable;
import reasonfx.util.ReasonLogger;

/**
 *
 * @author holzensp
 */
public class Given extends SimpleObjectProperty<Wanted> {
    public static final ReasonLogger LOGGER = new ReasonLogger(Given.class);
    
    protected final Term given;
    protected final SimpleStringProperty stringProp = new SimpleStringProperty();
    private boolean onlyVarDeps = true; //Invariant checker; see disconnect() for comments

    @SuppressWarnings("LeakingThisInConstructor")
    public Given(Term t) {
        super(null);
        given = t;
        //TODO: This Given shouldn't require it's own stringProp, if all Terms can produce one.
        stringProp.bind(Bindings.createStringBinding(given::show, this));
    }
    
    public boolean          isSatisfying()   { return null != this.get(); }
    public StringExpression stringProperty() { return stringProp; }
    public Term             asTerm()         { return given; }


    public boolean unify(Wanted w) {
        if(isSatisfying()) return false;
        LOGGER.entering("unify", this, w);
        final Term wanted = w.asTerm();
        LOGGER.info("Trying to unify given {0} with wanted {1}", given.dbgString(), wanted.dbgString());
        this.set(w);
        try {
            given.unify(this, wanted);
            this.set(w);
            return true;
        } catch(UnificationException e) {
            disconnect();
            return false;
        }
    }
    
    public void disconnect() {
        //TODO: ultimately, this should really be as simple as this.set(null);
        if (!isSatisfying()) return;

        LOGGER.info("Disconnecting {0}", this);
        
        //Until we reach a better understanding, we need to check the following invariant:
        //When a 'Given' gets disconnected, the univars bound by the corresponding unification must
        //be unbound *before* any of the dependent 'Given's are notified and 'reUnify'd. Since the
        //univar listeners get added during unification of the "first" 'Given', they should all be
        //added before any dependent 'Given's get added. We use 'onlyVarDeps' to signal that we have
        //"so-far" only added univars and no 'Given's. When actually disconnecting (i.e. setting the
        //property value to null) 'reUnify' of some dependent Given may be called, causing new list-
        //eners to be added. This is why we first set 'onlyVarDeps' to true, so that we already flag
        //the clean initial state of this invariant.
        onlyVarDeps = true;
        this.set(null);
    }

    public void reUnify() { if (isSatisfying()) this.unify(this.get()); }

    public <T> void addListener(Class<T> cls, ChangeListener<? super Wanted> l) {
        if(onlyVarDeps) {
            onlyVarDeps = UnificationVariable.class.isAssignableFrom(cls);
            super.addListener(l);
        } else if(Given.class.isAssignableFrom(cls)) {
            super.addListener(l);
        } else {
            Logger.getLogger(Given.class.getName()).log(Level.SEVERE,
                "Registered a {0} instead of a Given-listener after first non-var-dependency!",
                cls.getName());
            System.exit(-1);
        }
    }
}
