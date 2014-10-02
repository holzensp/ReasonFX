/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reasonfx.parsers.LogicParser;
import reasonfx.rule.Given;
import reasonfx.rule.Rule;
import reasonfx.rule.RuleInstance;
import reasonfx.rule.Wanted;
import reasonfx.term.Term;

/**
 *
 * @author holzensp
 */
public class MainGUI extends Application {
    private static final Rule
            andEL = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r,
            andI  = LogicParser.parse(LogicParser::dedrule,"@x, @y |- @x^@y").r;
    private static final Term
            pAndQ = LogicParser.parse(LogicParser::proposition,"p ^ q").t,
            termP = LogicParser.parse(LogicParser::proposition,"p").t;


    @Override
    public void start(Stage stage) {
        Group root = new Group();

        WantedView goal   = new WantedView(new Wanted(termP));
        RuleView   rAndEL = new RuleView(RuleInstance.instantiate(andEL));
        RuleView   rAndI  = new RuleView(RuleInstance.instantiate(andI));
        GivenView  premis = new GivenView(new Given(pAndQ));
        
        // Yuck!
        goal.relocate(  500, 600);
        rAndEL.relocate(500, 400);
        rAndI.relocate( 600, 400);
        premis.relocate(500, 300);

        root.getChildren().addAll(goal, rAndEL.getWrapGroup(), rAndI.getWrapGroup(), premis.getWrapGroup());
        
        stage.setTitle("NatDuctGUI");
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }
    
    public static void main(String args[]) { Application.launch(args); }
}
