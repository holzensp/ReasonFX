/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.SATests;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import reasonfx.gui.GivenView;
import reasonfx.gui.WantedView;

import reasonfx.parsers.LogicParser;
import reasonfx.rule.Rule;
import reasonfx.rule.RuleInstance;

/**
 *
 * @author holzensp
 */
public class GUITest extends Application {
    private static GivenView  activeGiven  = null;
    private static WantedView activeWanted = null;
    private static final Effect fx;
    static {
        DropShadow s = new DropShadow();
        s.setRadius(25.0);
        s.setOffsetX(8.0);
        s.setOffsetY(8.0);
        s.setColor(Color.color(0.1,0.7,0.4));
        fx = s;
    }
    
    private static final Rule andEL = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r;
    private static final RuleInstance rs[] = {
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL) };

    public static void setActiveGivenView(GivenView view) {
        if(null != activeGiven) {
            activeGiven.setEffect(null);
        }
        
        if(null != activeWanted && activeWanted.satisfy(view)) {
            activeWanted = null;
            activeWanted.setEffect(fx);
        } else {
            activeGiven = view;
            view.setEffect(fx);
        }
    }
    
    public static void setActiveWantedView(WantedView view) {
        /*
        if(null != activeWanted) {
            activeWanted.setEffect(null);
        }
        
        if(null != activeGiven && view.satisfy(activeGiven)) {
            activeGiven = null;
            activeGiven.setEffect(fx);
        } else {
            activeWanted = view;
            view.setEffect(fx);
        }
        */
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new AnchorPane();
        
        WantedView goal = new WantedView(LogicParser.parse(LogicParser::proposition,"q").t);
        goal.relocate(450, 700);

        for(int i = 0; i < rs.length; i++){
            GivenView rule = new GivenView(rs[i]);
            rule.relocate(100 + i * 200, 50 + i * 100);
            root.getChildren().add(makeDraggable(rule));
        }
            
        root.getChildren().add(goal);
        root.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            primaryStage.close();
        });
        
        primaryStage.setTitle("First Order Logic Parser Result");
        primaryStage.setScene(new Scene(root, 900, 800));
        primaryStage.show();
    }
    
    private Node makeDraggable(final Node node) {
        final DragContext dragContext = new DragContext();
        final Group       wrapGroup   = new Group(node);
        wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            dragContext.setFrom(event, node);
            event.consume();
        });
        wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            dragContext.assign(event, node);
        });
        
        return wrapGroup;
    }
    
    private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;
        public double baseX, baseY;
        
        public void setFrom(MouseEvent e, Node n) {
            mouseAnchorX = e.getX();
            mouseAnchorY = e.getY();
            initialTranslateX = n.getTranslateX();
            initialTranslateY = n.getTranslateY();
            baseX = e.getX() - n.getTranslateX();
            baseY = e.getY() - n.getTranslateY();
        }
        
        public void assign(MouseEvent e, Node n) {
            n.setTranslateX(initialTranslateX + e.getX() - mouseAnchorX);
            n.setTranslateY(initialTranslateY + e.getY() - mouseAnchorY);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
