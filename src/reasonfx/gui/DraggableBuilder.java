/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author holzensp
 */
public class DraggableBuilder {
    public static Node build(final Node node) {
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
        
        public void setFrom(MouseEvent e, Node n) {
            mouseAnchorX = e.getX();
            mouseAnchorY = e.getY();
            initialTranslateX = n.getTranslateX();
            initialTranslateY = n.getTranslateY();
        }
        
        public void assign(MouseEvent e, Node n) {
            n.setTranslateX(initialTranslateX + e.getX() - mouseAnchorX);
            n.setTranslateY(initialTranslateY + e.getY() - mouseAnchorY);
        }
    }

}
