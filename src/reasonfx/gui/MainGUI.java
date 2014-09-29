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

/**
 *
 * @author holzensp
 */
public class MainGUI extends Application {
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        
        WantedView w = new WantedView();
        RuleView   r = new RuleView();
        GivenView  g = new GivenView();
        w.relocate(500, 600);
        r.relocate(500, 400);
        g.relocate(500, 300);
        root.getChildren().addAll(w, r, g);
        
        stage.setTitle("NatDuctGUI");
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }
    
    public static void main(String args[]) { Application.launch(args); }
}
