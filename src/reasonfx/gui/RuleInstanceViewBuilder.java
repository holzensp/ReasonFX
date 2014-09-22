/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import reasonfx.rule.Given;
import reasonfx.rule.RuleInstance;
import reasonfx.term.Term;

/**
 *
 * @author holzensp
 */
public class RuleInstanceViewBuilder {
    public static void build(String name, RuleInstance r, Pane p) {
/*
        final Pane rulePane = new Pane();
        String colour = "-fx-background-color: #";
        for(int i = 0; i < 3; i++)
            colour += String.format("%2h", 200 + Math.round(40 * Math.random()));
        rulePane.setStyle(colour);
*/      
        List<Node> chlds = p.getChildren();
        Label ruleName = new Label(name);
        ruleName.setStyle("-fx-background-color: #ffffff");
        Node lbl = DraggableBuilder.build(ruleName);
        
        int i = 0;
        for(Term t : r.getPremisses()) {
            Node wanted = DraggableBuilder.build(new WantedView(t));
            wanted.relocate(i * 200, 50);
            chlds.add(flexLine(lbl, wanted));
            chlds.add(wanted);
            i++;
        }
        Node given = DraggableBuilder.build(new GivenView(new Given(r.getConclusion())));
        given.relocate(100 + i * 200, 50 + i * 100);
        chlds.add(flexLine(lbl,given));
        chlds.add(given);

        chlds.add(lbl);
    }
    
    private static Line flexLine(Node from, Node to) {
        Line l = new Line(50, 60, 100, 70);
        l.startXProperty().bind(Bindings.createDoubleBinding(() -> 
                from.getBoundsInParent().getMinX() + from.getBoundsInParent().getWidth()/2
            , from.boundsInParentProperty()));
        l.startYProperty().bind(Bindings.createDoubleBinding(() -> 
                from.getBoundsInParent().getMinY() + from.getBoundsInParent().getHeight()/2
            , from.boundsInParentProperty()));
        l.endXProperty().bind(Bindings.createDoubleBinding(() -> 
                to.getBoundsInParent().getMinX() + to.getBoundsInParent().getWidth()/2
            , to.boundsInParentProperty()));
        l.endYProperty().bind(Bindings.createDoubleBinding(() -> 
                to.getBoundsInParent().getMinY() + to.getBoundsInParent().getHeight()/2
            , to.boundsInParentProperty()));
        return l;
    }
    
}
