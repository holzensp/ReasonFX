/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import reasonfx.rule.Given;
import reasonfx.rule.RuleInstance;
import reasonfx.rule.Wanted;
import reasonfx.util.Draggable;

/**
 *
 * @author holzensp
 */
public class RuleView extends Satisfier {
    private final HBox         premisses  = new HBox();
    private final StackPane    conclusion = new StackPane();
    private final RuleInstance rule;
    private final Group        embedding;
    private final Given        given;
    
    public RuleView(RuleInstance ri) {
        super();
        rule = ri;
        
        Draggable d = new Draggable(this);
        embedding = d.getWrapGroup();

        /* Premisses part */
        premisses.setSpacing(10);
        ObservableList<Node> ps = premisses.getChildren();
        rule.getPremisses().stream()
            .map(Wanted::new)
            .map(WantedView::new)
            .forEach(ps::add);

        /* Conclusion part */
        Rectangle r = new Rectangle(100, 20, Color.GOLD);
        Text      t = new Text();
        t.textProperty().bind(rule.getConclusion().asStringExpression(-1));
        r.widthProperty().bind(Bindings.createDoubleBinding(() -> t.getBoundsInParent().getWidth(), t.boundsInParentProperty()));
        conclusion.getChildren().addAll(r,t);
        conclusion.setOnDragDetected(this::handle);
        given = new Given(rule.getConclusion());

        final Separator s = new Separator();
        s.maxWidthProperty().bind(Bindings.max
                ( premisses.widthProperty()
                , conclusion.widthProperty()
                ));
        
        getChildren().add(new VBox(premisses, s, conclusion));
    }
    
    public void handle(MouseEvent event) {
        EventType<? extends MouseEvent> ty = event.getEventType();
        
        if(ty.equals(MouseEvent.DRAG_DETECTED) && event.getButton() == MouseButton.SECONDARY) {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("myRule");
            db.setContent(content);
        }
        
        event.consume();
    }

    @Override public Group getWrapGroup() { return embedding; }
    @Override public Given getGiven()     { return given; }
    
}
