/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.rule;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author holzensp
 */
public class GivenImpl implements Given {
    private final Term given;
    private Wanted satisfying;
    private Collection<Given> deps = new ArrayList();
    private Collection<RuleInstanceVariable> vars = new ArrayList();

    public GivenImpl(Term t) {
        given = t;
    }
    
    @Override
    public Term asTerm() {
        return satisfying == null ? given : satisfying.asTerm();
    }
    
    public void disconnect() {
        if(satisfying == null) return;
        satisfying = null;
        Collection<Given> ds = deps;
        Collection<RuleInstanceVariable> vs = vars;
        deps = new ArrayList();
        vars = new ArrayList();
        vs.stream().forEach(RuleInstanceVariable::unbind);
        ds.stream().forEach(Given::reUnify);
    }
    
    @Override
    public boolean unify(Wanted w) {
        boolean res = Given.super.unify(w);
        if(res) {
            satisfying = w;
        } else {
            disconnect();
        }
        return res;
    }

    @Override public void reUnify() { if(satisfying != null) this.unify(satisfying); }
    @Override public void register(RuleInstanceVariable v) { vars.add(v); }
    @Override public void register(Given g)                { deps.add(g); }

    public Collection<RuleInstanceVariable> getBoundVariables() { return vars; }
    
}
