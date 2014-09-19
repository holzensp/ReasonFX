/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import reasonfx.term.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
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
    
    protected Collection<Given> deps = new ArrayList();
    protected final Term given;
    protected final SimpleStringProperty stringProp = new SimpleStringProperty();

    public boolean unify(Wanted w) {
        LOGGER.entering("unify", this, w);
        final Term wanted = w.asTerm();
        LOGGER.fine("Trying to unify given {0} with wanted {1}", given.show(), wanted.show());
        this.set(w);
        try {
            given.unify(this, wanted);
            this.set(w);
            return true;
        } catch(UnificationException e) {
            disconnect();
            return false;
        } finally {
            //TODO: This should just come from binding stringProp to this.
            stringProp.set(given.toString());
        }
    }
    
    public boolean isSatisfying() {
        return null != this.get();
    }

    
    public StringExpression stringProperty() {
        //TODO
        return new ReadOnlyStringWrapper(asTerm().toString() + "TODO");
    }
    
    public Given(Term t) {
        super(null);
        onlyVarDeps = true;
        given = t;
        stringProp.set(given.toString());
    }
    
    

    public Term asTerm() {
        //TODO; should we return the Wanted variant, or could we always return given?
        //Since they're unified... do we care?
        return isSatisfying() ? this.get().asTerm() : given;
    }

    public void disconnect() {
        System.out.println("disconnecting " + this.toString());
        //Only to check the invariant explained below
        onlyVarDeps = true;

        final Collection<Given> ds = deps;
        deps = new ArrayList();

        //TODO; should we just return, or is this reason to raise an exception?
        if (!isSatisfying()) return;

        stringProp.set(given.toString());
        this.set(null);

        //AFTER releasing al univars and setting this property to null, re-unify the dependencies.
        //If we do this in order of listener-addition, univars and dependencies are interleaved and
        //so some dependencies would not be notified of variables released later. The question is
        //whether this can occur, since dependencies should only be added as listeners after the
        //unification of this with a wanted (which causes all the univars to be added as listeners).
        ds.stream().forEach(Given::reUnify);
    }

    public void reUnify() { if (isSatisfying()) this.unify(this.get()); }

    public void register(Given g) { deps.add(g); }
    
    
    private boolean onlyVarDeps;
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
