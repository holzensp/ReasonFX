/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.SATests;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import reasonfx.gui.DraggableBuilder;
import reasonfx.gui.GivenView;
import reasonfx.gui.RuleInstanceViewBuilder;
import reasonfx.gui.WantedView;

import reasonfx.parsers.LogicParser;
import reasonfx.rule.Rule;
import reasonfx.rule.RuleInstance;

/**
 *
 * @author holzensp
 */
public class GUITest extends Application {
    private static final ObjectProperty<GivenView>  activeGiven  = new SimpleObjectProperty(null);
    private static final ObjectProperty<WantedView> activeWanted = new SimpleObjectProperty(null);
    private static final Effect fx = new DropShadow(25.0, 8.0, 8.0, Color.color(0.1,0.7,0.4));
    
    private static final Rule andEL = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r;
    private static final RuleInstance rs[] = {
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL) };
    
    static {
        activeGiven.addListener((ObservableValue<? extends GivenView> obs, GivenView oldView, GivenView newView) -> {
            if(null != oldView) oldView.setEffect(null);
            if(null != newView) newView.setEffect(fx);
        });
        activeWanted.addListener((ObservableValue<? extends WantedView> obs, WantedView oldView, WantedView newView) -> {
            if(null != oldView) oldView.setEffect(null);
            if(null != newView) newView.setEffect(fx);
        });
    }
    
    
    public static void setActiveGivenView(GivenView view) {
        activeGiven.set(null != activeWanted.get() && activeWanted.get().satisfy(view) ? null : view);
    }
    
    public static void setActiveWantedView(WantedView view) {
        activeWanted.set(null != activeGiven.get() && view.satisfy(activeGiven.get()) ? null : view);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane operationsPane = new Pane();
        
        WantedView goal = new WantedView(LogicParser.parse(LogicParser::proposition,"q").t);
        goal.relocate(450, 700);

        for(int i = 0; i < rs.length; i++){
            RuleInstanceViewBuilder.build("andEL", rs[i], operationsPane);
//            GivenView rule = new GivenView(rs[i]);
            //rule.relocate(100 + i * 200, 50 + i * 100);
            //operationsPane.getChildren().add(rule);
        }
            
        operationsPane.getChildren().add(goal);
        operationsPane.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> primaryStage.close() );
        
        Label labelWanted = new Label(), labelGiven = new Label();
        labelWanted.textProperty().bind(activeWanted.asString());
        labelGiven.textProperty().bind( activeGiven.asString());
        labelWanted.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
        labelGiven.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
        
        GridPane info = new GridPane();
        info.addRow(0, new Label("activeWanted"), labelWanted);
        info.addRow(1, new Label("activeGiven"),  labelGiven);
        
        FlowPane outer = new FlowPane(Orientation.VERTICAL, 8, 8, operationsPane, info);

        primaryStage.setTitle("First Order Logic Parser Result");
        primaryStage.setScene(new Scene(outer, 900, 800));
        primaryStage.show();
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
