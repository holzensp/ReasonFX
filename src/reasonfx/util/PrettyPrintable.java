/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.util;

import javafx.beans.binding.StringExpression;

/**
 *
 * @author holzensp
 */
public interface PrettyPrintable {
    public default String dbgString() {
        return show();
    }
    public default String show() {
        return asStringExpression(-1).get();
    }
    
    public StringExpression asStringExpression(int prec);
    
}
