/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.util;

import javafx.beans.property.ReadOnlyStringPropertyBase;

/**
 *
 * @author holzensp
 */
public class StringConstant extends ReadOnlyStringPropertyBase {
    private final String propValue;
    private final Object propBean;
    private final String propName;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public StringConstant(String value) {
        propValue = value;
        propBean  = this;
        propName  = "propValue";
    }

    public StringConstant(String value, Object bean, String name) {
        propValue = value;
        propBean  = bean;
        propName  = name;
    }

    @Override public String get()     { return propValue; }
    @Override public Object getBean() { return propBean;  }
    @Override public String getName() { return propName;  }

}
