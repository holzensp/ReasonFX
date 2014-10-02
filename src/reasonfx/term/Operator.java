/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import java.util.HashMap;
import java.util.Objects;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author holzensp
 */
public class Operator {
    private static final HashMap<String,Operator> KNOWNOPS = new HashMap();
    
    public final ReadOnlyStringProperty stringRepr;
    public final int                    arity;
    public final boolean                associative;
    public final int                    precedence;
    
    private Operator(String name, int args, boolean assoc, int prec) {
        stringRepr  = new ReadOnlyStringWrapper(name);
        arity       = args;
        associative = assoc;
        precedence  = prec;
    }
    
    public static Operator getInstance(String name, int args, boolean assoc, int prec) throws Exception {
        if(!KNOWNOPS.containsKey(name)) {
            KNOWNOPS.put(name, new Operator(name, args, assoc, prec));
        }
        
        Operator o = KNOWNOPS.get(name);
        
        if (o.arity != args) {
            throw new Exception("Conflicting definitions of operator " + name +
                    " (arity of " + o.arity + " vs. arity of " + args + ")");
        }
        
        return o;
    }
    
    public static Operator getInstance(String name) throws NullPointerException {
        return Objects.requireNonNull(KNOWNOPS.get(name), "Unknown operator " + name);
    }
}
