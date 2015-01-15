/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.tactileGui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import reasonfx.rule.Given;
import reasonfx.rule.Wanted;

/**
 *
 * @author holzensp
 */
public final class WantedView extends VBox {
    private final Text                     wantedText = new Text();
    private final Satisfier            emptySatisfier = new EmptySatisfier(wantedText.boundsInParentProperty());
    private final ObjectProperty<Satisfier> satisfier = new SimpleObjectProperty();
    
    private final Wanted wanted;
    
    public WantedView(Wanted w) {
        super();
        
        wanted = w;
        
        // The satisfier in the GUI sits above the separator. We need a place-
        // holder to easily substitute 'Node's without changing their vertical
        // order in 'this'.
        final StackPane p = new StackPane();
        
        p.getChildren().add(emptySatisfier);
        satisfier.addListener((obs, old, newValue) -> {
            p.getChildren().clear();
            if(newValue != emptySatisfier) {
                newValue.getWrapGroup().getChildren().clear();
                newValue.relocate(0,0);
            }
            p.getChildren().add(newValue);
        });
                
        wantedText.textProperty().bind(wanted.asTerm().asStringExpression(-1));
        wantedText.setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY)
             && event.getClickCount() >= 2
             && satisfier.get() != emptySatisfier)
                disconnect();                
        });

        setAlignment(Pos.CENTER);
        getChildren().addAll(p,wantedText);
    }
    
    private class EmptySatisfier extends Satisfier {
        private final Effect fx = new Glow();
        private Satisfier source = null;
        
        public EmptySatisfier(ReadOnlyObjectProperty<Bounds> bs) {
            super();
            Rectangle r = new Rectangle(100, 10, Color.LAVENDER);
            r.widthProperty().bind(Bindings.createDoubleBinding(() -> bs.get().getWidth(), bs));
            getChildren().add(r);
            addEventFilter(DragEvent.ANY, this::handle);
        }

        public void handle(DragEvent event) {
            EventType<DragEvent> ty = event.getEventType();
            if(ty.equals(DragEvent.DRAG_ENTERED)) {
                if(Satisfier.class.isAssignableFrom(event.getGestureSource().getClass())) {
                    Satisfier s = (Satisfier) event.getGestureSource();
                    if(source != null) {
                        throw new UnsupportedOperationException("Multiple Gesture Sources");
                    } else if(s.getGiven().unify(wanted)) {
                        this.setEffect(fx);
                        source = s;
                    }
                }
            } else if(ty.equals(DragEvent.DRAG_OVER)) {
                if(event.getGestureSource() == source)
                    event.acceptTransferModes(TransferMode.ANY);
            } else if(ty.equals(DragEvent.DRAG_EXITED)) {
                if(null != source) {
                    source.getGiven().disconnect();
                    source = null;
                    this.setEffect(null);
                }
            } else if(ty.equals(DragEvent.DRAG_DROPPED)) {
                WantedView.this.satisfy(source);
                source = null;
            } else if(ty.equals(DragEvent.DRAG_ENTERED_TARGET)) { // do nothing
            } else if(ty.equals(DragEvent.DRAG_EXITED_TARGET)) { // do nothing
            } else
                throw new UnsupportedOperationException("DragEventType: " + ty + " from " + event);
            event.consume();
        }

        @Override public void popOut() {}
        @Override public Group getWrapGroup() { throw new UnsupportedOperationException("Not wrapped in Group"); }
        @Override public Given getGiven() { throw new UnsupportedOperationException("Does not contain a given"); }
    }
    
    public void satisfy(Satisfier s) {
        satisfier.set(s);
    }
    
    public void disconnect() {
        Satisfier n = satisfier.get();
        if(null == n || emptySatisfier == n) return;
        n.popOut();
        
        satisfier.set(emptySatisfier);
    }
}
