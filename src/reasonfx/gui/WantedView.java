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
import reasonfx.term.Term;
import reasonfx.rule.Wanted;

/**
 *
 * @author holzensp
 */
public class WantedView extends Pane {
    private GivenView satisfier = null;
    private final Wanted wanted;
    private final Label unsatisfied;
    
    public WantedView(Wanted w) {
        super();
        wanted = w;
        unsatisfied = new Label(w.asTerm().toString());
        this.getChildren().add(unsatisfied);
        String colour = "-fx-background-color: #";
        for(int i = 0; i < 3; i++)
            colour += String.format("%2h", 200 + Math.round(40 * Math.random()));
        System.out.println(colour);
        this.setStyle(colour);
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                GUITest.setActiveWantedView(WantedView.this);
            }
        });
    }
    
    public WantedView(Term t) { this(new Wanted(t)); }
    
    public boolean satisfy(GivenView gv) {
        if(wanted.satsify(gv.given)) {
            satisfier = gv;
            this.getChildren().clear();
            this.getChildren().add(satisfier);
            return true;
        }
        return false;
    }
    
    public GivenView disconnect() {
        GivenView res = satisfier;
        res.given.disconnect();
        satisfier = null;
        this.getChildren().clear();
        this.getChildren().add(unsatisfied);
        return res;
    }
}
