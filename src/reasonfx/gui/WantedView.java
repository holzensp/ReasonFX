/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import reasonfx.rule.Wanted;

/**
 *
 * @author holzensp
 */
public final class WantedView extends VBox {
    private final Shape emptySatisfier = new EmptySatisfier();
    private final ObjectProperty<Node> satisfier = new SimpleObjectProperty();
    private final StringProperty content = new SimpleStringProperty("Wanted");
    private final Wanted wanted;
    
    public WantedView(Wanted w) {
        super();
        
        wanted = w;
        
        // The satisfier in the GUI sits above the separator. We need a place-
        // holder to easily substitute 'Node's without changing their vertical
        // order in 'this'.
        final StackPane p = new StackPane();
        
        p.getChildren().add(emptySatisfier);
        satisfier.addListener((observable, oldValue, newValue) -> {
            p.getChildren().clear(); //.remove(oldValue);
            p.getChildren().add(newValue);
        });
                
        final Text t = new Text();
        t.textProperty().bind(content);
        t.setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY)
             && event.getClickCount() >= 2
             && satisfier.get() != emptySatisfier)
                disconnect();                
        });

        final Separator s = new Separator();
        s.maxWidthProperty().bind(Bindings.createDoubleBinding( () ->
            Math.max( t.getBoundsInParent().getWidth()
                    , p.getBoundsInParent().getWidth()),
            t.boundsInParentProperty(),
            p.boundsInParentProperty()));

        setAlignment(Pos.CENTER);
        getChildren().addAll(p,s,t);
    }
    
    private class EmptySatisfier extends Rectangle {
        private final Effect fx = new Glow();
        
        public EmptySatisfier() {
            super(100, 20, Color.LAVENDER);
            addEventFilter(DragEvent.ANY, this::handle);
        }

        public void handle(DragEvent event) {
            EventType<DragEvent> ty = event.getEventType();
            final String msg;
            if(ty.equals(DragEvent.DRAG_ENTERED)) {
                msg = "onDragEntered";
                this.setEffect(fx);
            } else if(ty.equals(DragEvent.DRAG_OVER)) {
                msg = "onDragOver";
                if(isFromGiven(event) || isFromRule(event)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            } else if(ty.equals(DragEvent.DRAG_EXITED)) {
                msg = "onDragExited";
                this.setEffect(null);
            } else if(ty.equals(DragEvent.DRAG_DROPPED)) {
                msg = "onDragDropped";
                if(isFromGiven(event)) {
                    WantedView.this.satisfy((GivenView) event.getGestureSource());
                } else if (isFromRule(event)) {
                    WantedView.this.satisfy((RuleView) event.getGestureSource());
                } else
                    throw new UnsupportedOperationException("WantedView received DRAG_DROPPED from something other than GivenView or RuleView");
            } else
                msg = "unsupportedDragEventType";
            System.out.println(msg);
            event.consume();
        }
    }
    
    private boolean isFromGiven(DragEvent e) {
        return GivenView.class
            .isAssignableFrom(
                e.getGestureSource().getClass()
            );
    }
    
    private boolean isFromRule(DragEvent e) {
        return RuleView.class
            .isAssignableFrom(
                e.getGestureSource().getClass()
            );
    }
    
    public void satisfy(GivenView g) { satisfier.set(g); }
    public void satisfy(RuleView  r) { satisfier.set(r); }
    
    public void disconnect() {
        Node n = satisfier.get();
        if(null == n || emptySatisfier == n) return;
        Bounds nbs = n.getBoundsInParent();
        
        satisfier.set(emptySatisfier);
        assert(Group.class.isAssignableFrom(this.getParent().getClass()));
        Bounds pbs = this.getBoundsInParent();
       
        n.relocate( pbs.getMinX() + (pbs.getWidth() - nbs.getWidth())/2
                  , pbs.getMinY() - nbs.getHeight() - 20);
        ((Group)this.getParent()).getChildren().add(n);
    }
}
