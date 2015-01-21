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
import reasonfx.parsers.LogicParser;
import reasonfx.rule.Rule;
import reasonfx.rule.Wanted;
import reasonfx.term.Term;

/**
 *
 * @author holzensp
 */
public class MainGUI extends Application {
	
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		
		// Init TactilePane TODO: FXML
		TactilePane tactilePane = new TactilePane();
        tactilePane.setPrefSize(WIDTH, HEIGHT);
        tactilePane.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        tactilePane.setBordersCollide(true);
		
		TextItem textPorQ = new TextItem("p v q");
		textPorQ.setLayoutX(50);
		textPorQ.setLayoutY(250);
		tactilePane.getChildren().add(textPorQ);
		
		TextItem textP = new TextItem("p");
		textP.setLayoutX(70);
		textP.setLayoutY(100);
		tactilePane.getChildren().add(textP);
		
        TextItem textQ = new TextItem("q");
        textQ.setLayoutX(150);
        textQ.setLayoutY(50);
        tactilePane.getChildren().add(textQ);
		
		root.setCenter(tactilePane);
		
		Scene scene = new Scene(root);
        primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest(event -> { Platform.exit(); });
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	
    
 

    public static void main(String args[]) {
        Application.launch(args);
    }


}
