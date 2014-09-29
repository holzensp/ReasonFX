/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;

/**
 *
 * @author holzensp
 */
public class BoundsWidthBinding extends DoubleBinding {
    private final ReadOnlyObjectProperty<Bounds> bounds;
    
    public BoundsWidthBinding(ReadOnlyObjectProperty<Bounds> bnds) {
        super();
        super.bind(bnds);
        bounds = bnds;
    }
    
    @Override
    protected double computeValue() {
        return bounds.get().getWidth();
    }

}
