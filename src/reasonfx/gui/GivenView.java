/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import reasonfx.SATests.GUITest;
import reasonfx.rule.Given;

/**
 *
 * @author holzensp
 */
public class GivenView extends Pane {
    public final Given given;
    
    public GivenView(Given g) {
        super();
        given = g;
        Label lbl = new Label();
        lbl.textProperty().bind(given.stringProperty());
        getChildren().add(lbl);
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                GUITest.setActiveGivenView(GivenView.this);
            }
        });
    }
/*
    public GivenView(RuleInstance r) {
        super();
        given = r;

        Label lbl = new Label();
        lbl.textProperty().bind(given.stringProperty());
        
        Separator sep = new Separator();
        sep.setPrefWidth(10);

        FlowPane wanteds = new FlowPane(Orientation.HORIZONTAL);
        FlowPane rule = new FlowPane(Orientation.VERTICAL, wanteds, sep, lbl);

        this.getChildren().add(rule);
        
        DoubleExpression prefWidth = new SimpleDoubleProperty(10);
        
        List<Node> chlds = wanteds.getChildren();
        r.getPremisses().stream().map(WantedView::new).forEach(chlds::add);
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                GUITest.setActiveGivenView(GivenView.this);
            }
        });
    }
*/    
    @Override public String toString() { return given.asTerm().asStringExpression(-1).get(); }
}
