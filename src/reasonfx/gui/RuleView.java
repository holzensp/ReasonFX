/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.beans.binding.Bindings;
import javafx.event.EventType;
import javafx.scene.control.Separator;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author holzensp
 */
public class RuleView extends VBox {
    private final HBox      premisses  = new HBox();
    private final StackPane conclusion = new StackPane();
    
    public RuleView() {
        super();
        Rectangle r = new Rectangle(100, 20, Color.GOLD);
        conclusion.getChildren().addAll(r,new Text("Given"));
        conclusion.setOnDragDetected(this::handle);
        conclusion.setOnDragDone(this::handle);

        final Separator s = new Separator();
        s.maxWidthProperty().bind(Bindings.max
                ( premisses.widthProperty()
                , conclusion.widthProperty()
                ));
        premisses.getChildren().addAll(new WantedView(), new WantedView());
        
        getChildren().addAll(premisses, s, conclusion);
    }
    
    public void handle(DragEvent event) {
        EventType<DragEvent> ty = event.getEventType();
        System.out.println("DragEvent: " + ty);
        event.consume();
    }
    
    public void handle(MouseEvent event) {
        EventType<? extends MouseEvent> ty = event.getEventType();
        if(ty.equals(MouseEvent.DRAG_DETECTED)) {
            System.out.println("onDragDetected");
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("myRule");
            db.setContent(content);
        } else
            System.out.println("MouseEvent: " + ty);
        
        event.consume();
    }
    
}
