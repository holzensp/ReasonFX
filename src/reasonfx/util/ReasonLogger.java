/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author holzensp
 */
public class ReasonLogger {
    private final String className;
    
    public ReasonLogger(Class cls) { className = cls.getName(); }
    
    private void log(Level l, String s, Object[] os) { Logger.getLogger(className).log(l, s, os); }
    
    public void entering(String s, Object... os) { Logger.getLogger(className).entering(className, s, os); }
    public void exiting(String s) { Logger.getLogger(className).exiting(className, s); }
    
    public void severe(String s, Object... os) { log(Level.SEVERE, s, os); }
    public void fine(  String s, Object... os) { log(Level.FINE,   s, os); }
    public void finer( String s, Object... os) { log(Level.FINER,  s, os); }
    public void finest(String s, Object... os) { log(Level.FINEST, s, os); }
    
}
