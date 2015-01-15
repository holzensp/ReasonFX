/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.tactileGui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.utwente.cs.caes.tactile.control.TactilePane;
import nl.utwente.cs.caes.tactile.debug.DebugParent;
import reasonfx.parsers.LogicParser;
import reasonfx.rule.Rule;
import reasonfx.rule.Wanted;
import reasonfx.term.Term;

/**
 *
 * @author holzensp
 */
public class MainGUI extends Application {

    private static final Rule andEL = LogicParser.parse(LogicParser::dedrule, "@x^@y |- @x").r,
            andI = LogicParser.parse(LogicParser::dedrule, "@x, @y |- @x^@y").r;
    private static final Term pAndQ = LogicParser.parse(LogicParser::proposition, "p ^ q").t,
            termP = LogicParser.parse(LogicParser::proposition, "p").t;

    @Override
    public void start(Stage stage) throws Exception {
        TactilePane root;
        try {
//            root = (TactilePane) FXMLLoader.load(getClass().getResource("Main.fxml"));
        root = (TactilePane) FXMLLoader.load(getClass().getResource("Main_1.fxml"));
        
            TextItem cItem = new TextItem();
//            cItem.setColor(new Paint());
//            TactilePane.setOnInProximity(circle, event -> {
//                if (!TactilePane.isInUse(circle)) {
//                    TactilePane.moveAwayFrom(circle, event.getOther(), 20);
//                }
//            });
          
            
            Scene scene = new Scene(root);
//            DebugParent debug = new DebugParent(root);
//            debug.registerTactilePane(root);
//            scene = new Scene(debug);
            
            stage.setFullScreen(false);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
            });
            stage.setTitle("ReasonFX");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Group root = new Group();
//
        WantedView goal   = new WantedView(new Wanted(termP));
//        RuleView   rAndEL = new RuleView(RuleInstance.instantiate(andEL));
//        RuleView   rAndI  = new RuleView(RuleInstance.instantiate(andI));
//        GivenView  premis = new GivenView(new Given(pAndQ));
//        
//        // Yuck!
//        goal.relocate(  500, 600);
//        rAndEL.relocate(500, 400);
//        rAndI.relocate( 600, 400);
//        premis.relocate(500, 300);
//
//        root.getChildren().addAll(goal, rAndEL.getWrapGroup(), rAndI.getWrapGroup(), premis.getWrapGroup());
//        
//        stage.setTitle("NatDuctGUI");
//        stage.setScene(new Scene(root, 1200, 800));
//        stage.show();
    }

    public static void main(String args[]) {
        Application.launch(args);
    }
}
