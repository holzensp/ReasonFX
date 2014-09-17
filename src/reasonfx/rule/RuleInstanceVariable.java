/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Collection;

/**
 *
 * @author holzensp
 */
public class RuleInstanceVariable extends RuleVariable {
    private int prettyID = -1;
    private RuleInstance parent;
    private class Binding {
        public final Term  value;
        public final Given origin;
        public Binding(Term v, Given o) { value = v; origin = o; }
    }
    private Binding binding = null;
    
    public RuleInstanceVariable(RuleVariable v) {
        super(v);
        prettyID = ruleLocalID;
    }
    
    protected void setParent(RuleInstance i) {
        if(parent != null)
            throw new NullPointerException("Parent set when it WASN'T null");
        parent = i;
    }
    
    public void setPrettyID(int id) { prettyID = id; }
    public void unsetPrettyID() { prettyID = ruleLocalID; }

    public void    unbind()     { binding = null; }
    public boolean isBound()    { return null != binding; }
    public Term    getBinding() { return binding.value; }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(debugging) {
            if(isBound()) result.append('(');
            result
                .append(mkString(prettyID))
                .append('[').append(String.valueOf(getID())).append(']');
            if(isBound()) {
                result.append(" => ");
                binding.value.prettyPrint(result, -1, debugging);
                result.append(')');
            }
        } else {
            if(isBound())
                binding.value.prettyPrint(result,prec,debugging);
            else {
                result.append(mkString(prettyID));
            }
        }
    }

    @Override
    public void unify(Given unifier, Term wanted) throws UnificationException {
        if(!isBound()) {
            unifier.register(this);
            binding = new Binding(wanted, unifier);
            System.out.println("Binding " + this.dbgString() + " to " + wanted.dbgString());
        } else {
            binding.origin.register(unifier);
            binding.value.unify(unifier, wanted);
        }
    }
    
    @Override public String toString() { return dbgString(); }
    @Override public Term copyWithChildren(Collection<Term> chlds) { return new RuleInstanceVariable(this); }
}
