/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.beans.binding.Bindings;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import reasonfx.rule.Given;
import reasonfx.util.Draggable;

/**
 *
 * @author holzensp
 */
public class GivenView extends Satisfier {
    private final Group embedding;
    private final Given given;
    
    public GivenView(Given g) {
        super();
        
        given = g;

        Text t = new Text();
        t.textProperty().bind(g.asTerm().asStringExpression(-1));
        Rectangle r = new Rectangle(100, 20, Color.GOLD);
        r.widthProperty().bind(Bindings.createDoubleBinding(() -> t.getBoundsInParent().getWidth(), t.boundsInParentProperty()));

        getChildren().addAll(r,t);
        setOnDragDetected(this::handle);
        setOnDragDone(this::handle);
        setEffect(new DropShadow());
        
        Draggable d = new Draggable(this);
        embedding = d.getWrapGroup();
    }

    public void handle(DragEvent event) {
        EventType<DragEvent> ty = event.getEventType();
        System.out.println("DragEvent: " + ty);
        event.consume();
    }
    
    public void handle(MouseEvent event) {
        EventType<? extends MouseEvent> ty = event.getEventType();
        if(ty.equals(MouseEvent.DRAG_DETECTED) && event.getButton() == MouseButton.SECONDARY) {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("Given");
            db.setContent(content);
        } else
            System.out.println("MouseEvent: " + ty);
        
        event.consume();
    }
    
    @Override public Group getWrapGroup() { return embedding; }
    @Override public Given getGiven()     { return given; }
}
