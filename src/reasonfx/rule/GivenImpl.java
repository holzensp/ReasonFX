/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.rule;

import java.util.ArrayList;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author holzensp
 */
public class GivenImpl implements Given {
    private final SimpleStringProperty stringProp = new SimpleStringProperty();
    private final Term given;
    private Wanted satisfying;
    private Collection<Given> deps = new ArrayList();
    private Collection<RuleInstanceVariable> vars = new ArrayList();

    public GivenImpl(Term t) {
        given = t;
        stringProp.set(given.toString());
    }
    
    @Override
    public Term asTerm() {
        return satisfying == null ? given : satisfying.asTerm();
    }
    
    @Override
    public void disconnect() {
        if(satisfying == null) return;
        satisfying = null;
        Collection<Given> ds = deps;
        Collection<RuleInstanceVariable> vs = vars;
        deps = new ArrayList();
        vars = new ArrayList();
        vs.stream().forEach(RuleInstanceVariable::unbind);
        ds.stream().forEach(Given::reUnify);
        stringProp.set(given.toString());
    }
    
    @Override
    public boolean unify(Wanted w) {
        boolean res = Given.super.unify(w);
        if(res) {
            satisfying = w;
        } else {
            disconnect();
        }
        stringProp.set(given.toString());
        return res;
    }

    @Override public void reUnify() { if(satisfying != null) this.unify(satisfying); }
    @Override public void register(RuleInstanceVariable v) { vars.add(v); }
    @Override public void register(Given g)                { deps.add(g); }

    public Collection<RuleInstanceVariable> getBoundVariables() { return vars; }

    @Override public void addListener(   ChangeListener<? super String> listener) { stringProp.addListener(   listener); }
    @Override public void removeListener(ChangeListener<? super String> listener) { stringProp.removeListener(listener); }
    @Override public void addListener(   InvalidationListener           listener) { stringProp.addListener(   listener); }
    @Override public void removeListener(InvalidationListener           listener) { stringProp.removeListener(listener); }
    @Override public String getValue() { return stringProp.getValue(); }
}
