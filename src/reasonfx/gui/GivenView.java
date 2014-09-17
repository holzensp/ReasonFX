/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import reasonfx.SATests.GUITest;
import reasonfx.rule.Given;
import reasonfx.rule.RuleInstance;

/**
 *
 * @author holzensp
 */
public class GivenView extends Pane {
    public final Given given;
    
    public GivenView(RuleInstance r) {
        super();
        given = r;
        Label lbl = new Label();
        lbl.textProperty().bind(given);
        FlowPane wanteds = new FlowPane(Orientation.HORIZONTAL);
        this.getChildren().add(
            new FlowPane(Orientation.VERTICAL, wanteds, new Separator(), lbl));
        ObservableList<Node> chlds = wanteds.getChildren();
        r.getPremisses().stream().map(WantedView::new).forEach(chlds::add);
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                GUITest.setActiveGivenView(GivenView.this);
            }
        });
    }
}
