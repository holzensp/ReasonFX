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
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nl.utwente.cs.caes.tactile.control.TactilePane;
import nl.utwente.cs.caes.tactile.debug.DebugParent;
import reasonfx.gui.GivenView;
import reasonfx.gui.RuleView;
import reasonfx.gui.WantedView;
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
	
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    
    private static final Rule
    andEL = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r,
    andI  = LogicParser.parse(LogicParser::dedrule,"@x, @y |- @x^@y").r;
    private static final Term
    pAndQ = LogicParser.parse(LogicParser::proposition,"p ^ q").t,
    termP = LogicParser.parse(LogicParser::proposition,"p").t;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		
		// Init TactilePane TODO: FXML
		TactilePane tactilePane = new TactilePane();
        tactilePane.setPrefSize(WIDTH, HEIGHT);
        tactilePane.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        tactilePane.setBordersCollide(true);
		
//		TextItem textPorQ = new TextItem("p v q");
//		textPorQ.setLayoutX(50);
//		textPorQ.setLayoutY(250);
//		tactilePane.getChildren().add(textPorQ);
//		
//		TextItem textP = new TextItem("p");
//		textP.setLayoutX(70);
//		textP.setLayoutY(100);
//		tactilePane.getChildren().add(textP);
//		
//        TextItem textQ = new TextItem("q");
//        textQ.setLayoutX(150);
//        textQ.setLayoutY(50);
//        tactilePane.getChildren().add(textQ);
        
        //copy pasted from gui.MainGUI
        WantedView goal   = new WantedView(new Wanted(termP));
        RuleView   rAndEL = new RuleView(RuleInstance.instantiate(andEL));
        RuleView   rAndI  = new RuleView(RuleInstance.instantiate(andI));
        GivenView  premis = new GivenView(new Given(pAndQ));
        
        // Yuck!
        goal.relocate(  500, 600);
        rAndEL.relocate(500, 400);
        rAndI.relocate( 600, 400);
        premis.relocate(500, 300);

        tactilePane.getChildren().addAll(goal, rAndEL.getWrapGroup(), rAndI.getWrapGroup(), premis.getWrapGroup());
		
        
        // Key bindings
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        	@Override
        	public void handle(KeyEvent keyEvent){
                if (keyEvent.getCode() == KeyCode.F11) {
                	if (primaryStage.isFullScreen()){
                		primaryStage.setFullScreen(false);
                	} else {
                		primaryStage.setFullScreen(true);
                	}
                	keyEvent.consume();
                }
            }
        	
        });
        
        
		root.setCenter(tactilePane);
		
		Scene scene = new Scene(root);
        primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest(event -> { Platform.exit(); });
        primaryStage.setScene(scene);
        primaryStage.setTitle("NatDuctGUI");
        primaryStage.show();
		
	}
	
	
    
 

    public static void main(String args[]) {
        Application.launch(args);
    }


}
