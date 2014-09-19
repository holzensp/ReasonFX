/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.ArrayList;
import java.util.Collection;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author holzensp
 */
public class Given extends SimpleObjectProperty<Wanted> {
    protected Collection<Given> deps = new ArrayList();
    protected final Term given;
    protected Wanted satisfying;
    protected final SimpleStringProperty stringProp = new SimpleStringProperty();
    public boolean unifyInternal(Wanted wanted) {
        final Term w = wanted.asTerm();
        this.set(wanted);
        try {
            this.asTerm().unify(this, w);
        } catch(UnificationException e) {
            return false;
        }
        return true;
    }

    public boolean unify(Wanted w) {
        boolean res = unifyInternal(w);
        if(res) {
            satisfying = w;
            this.set(w);
        } else {
            disconnect();
        }
        stringProp.set(given.toString());
        return res;
    }

    
    public StringExpression stringProperty() {
        //TODO
        return new ReadOnlyStringWrapper(asTerm().toString() + "TODO");
    }
    
    public Given(Term t) {
        given = t;
        stringProp.set(given.toString());
    }
    
    

    public Term asTerm() {
        return satisfying == null ? given : satisfying.asTerm();
    }

    public void disconnect() {
        if (satisfying == null) return;
        satisfying = null;
        Collection<Given> ds = deps;
        deps = new ArrayList();
        ds.stream().forEach(Given::reUnify);
        stringProp.set(given.toString());
        System.out.println("Disconnected " + this.toString());
        this.set(null);
        System.out.println("Finished disconnecting");
    }

    public void reUnify() {
        if (satisfying != null) {
            this.unify(satisfying);
        }
    }

    public void register(Given g) {
        deps.add(g);
    }
}
