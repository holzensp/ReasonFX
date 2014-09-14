/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

/**
 *
 * @author holzensp
 */
public class Rule extends EntailmentBase {
    private final int varCnt;
    
    private Rule(int v, Term c, Term... ps) {
        super(c,ps);
        varCnt = v;
    }
    
    public Rule(Entailment ent) {
        super(ent.getConclusion(), ent.getPremisses().toArray(new Term[ent.getPremisses().size()]));
        Set<RuleVariable> rvs = new HashSet();
        getConclusion().collect(rvs,RuleVariable.class);
        getPremisses().stream().forEach(t ->
            t.collect(rvs, RuleVariable.class) );
        varCnt = rvs.size();
    }
    
    public Rule(Rule that) {
        super(that.getConclusion(), that.getPremisses());
        this.varCnt = that.varCnt;
    }
    
    protected static Rule instantiateFrom(Entailment ent) {
        return new Rule(0,ent.getConclusion(), ent.getPremisses().toArray(new Term[ent.getPremisses().size()]));
    }
    
    public static Rule abstractFrom(Entailment ent) {
        final Map<ConcreteVariable,RuleVariable> lut = new TreeMap();
        Function<ConcreteVariable,RuleVariable> f = c -> {
            if(!lut.containsKey(c)) {
                lut.put(c, new RuleVariable(lut.size()));
            }
            return lut.get(c);
        };
        Term c = (Term) ent.getConclusion().copyWith(f, ConcreteVariable.class);
        
        int length = ent.getPremisses().size();
        Term[] ps = new Term[length];
        int i = 0;
        for(Term t : ent.getPremisses())
            ps[i++] = (Term) t.copyWith(f, ConcreteVariable.class);
        return new Rule(lut.size(), c, ps);
    }
}
