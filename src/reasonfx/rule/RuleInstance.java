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
import java.util.TreeMap;
import java.util.function.Function;

/**
 *
 * @author holzensp
 */
public class RuleInstance extends EntailmentBase {
    public RuleInstance(Term c, Collection<Term> ps) { super(c,ps); }
    
    public static RuleInstance instantiate(Rule bluePrint) {
        final Map<RuleVariable,RuleInstanceVariable> insts = new TreeMap();
        Function<RuleVariable,RuleInstanceVariable> mkInst = v -> {
                if(!insts.containsKey(v))
                    insts.put(v,new RuleInstanceVariable(v));
                return insts.get(v);
            };
        Term c = bluePrint.getConclusion().copyWith(mkInst, RuleVariable.class);
        List<Term> ps = new ArrayList();
        for(Term t : bluePrint.getPremisses())
            ps.add(t.copyWith(mkInst, RuleVariable.class));
        return new RuleInstance(c, ps);
    }
    
    @Override public String toString() { return show(); }
}
