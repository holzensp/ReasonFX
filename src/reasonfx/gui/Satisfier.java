/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import reasonfx.rule.Given;

/**
 *
 * @author holzensp
 */
public abstract class Satisfier extends StackPane {
    public Satisfier(Node... children) { super(children); }
    
    public void popOut() {
        Bounds bs = localToScene(getBoundsInLocal());
        if(this.getParent() != getWrapGroup()) {
            if(Pane.class.isAssignableFrom(this.getParent().getClass())) {
                ((Pane) this.getParent()).getChildren().remove(this);
            } else { // do NOT test for Group-type; we want to know about missing cases
                ((Group) this.getParent()).getChildren().remove(this);
            }
            getWrapGroup().getChildren().add(this);
            getWrapGroup().relocate(Math.max(10,bs.getMinX() - 10), Math.max(10,bs.getMinY() - 30 - bs.getHeight()));
            getGiven().disconnect();
        }

    }
    public abstract Group getWrapGroup();
    public abstract Given getGiven();
}
