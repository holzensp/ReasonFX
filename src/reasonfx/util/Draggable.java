/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.util;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author holzensp
 */
public final class Draggable {
    private final Node  mobile;
    private final Group wrapGroup;
    
    private final SimpleDoubleProperty
        baseX = new SimpleDoubleProperty(),
        baseY = new SimpleDoubleProperty(),
        currX = new SimpleDoubleProperty(),
        currY = new SimpleDoubleProperty();
    private final SimpleBooleanProperty moving = new SimpleBooleanProperty();

    public Draggable(Node n) {
        mobile = n;
        mobile.translateXProperty().bind(baseX.add(currX));
        mobile.translateYProperty().bind(baseY.add(currY));
        wrapGroup = new Group(mobile);
        wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handle);
        wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::handle);
        wrapGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, this::handle);
    }
    
    public Draggable(Node n, ObservableObjectValue<Bounds> maxBound) {
        this(n);
        currY.addListener((observable, oldValue, newValue) -> {
            double limit = maxBound.get().getMaxY() +
                Draggable.this.mobile.getBoundsInParent().getHeight() / 2;
            if(newValue.doubleValue() < limit) { currY.set(limit); }
        });
    }
    
    public Draggable(ObservableObjectValue<Bounds> minBound, Node n) {
        this(n);
        currY.addListener((observable, oldValue, newValue) -> {
            double limit = minBound.get().getMinY() -
                Draggable.this.mobile.getBoundsInParent().getHeight() / 2;
            if(newValue.doubleValue() > limit) { currY.set(limit); }
        });
    }

    public void handle(MouseEvent event) {
        if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton() == MouseButton.PRIMARY) {
            baseX.set(mobile.getTranslateX() - event.getX());
            baseY.set(mobile.getTranslateY() - event.getY());
            currX.set(event.getX());
            currY.set(event.getY());
            moving.set(true);
            event.consume();
        } else if(event.getEventType().equals(MouseEvent.MOUSE_DRAGGED) && moving.get()) {
            currX.set(event.getX());
            currY.set(event.getY());
        } else if(event.getEventType().equals(MouseEvent.MOUSE_RELEASED) && moving.get()) {
            moving.set(false);
        }
    }

    public static Node mkWithLowerBound(final Node mobile, final Node bound) {
        return new Draggable(bound.boundsInParentProperty(), mobile).wrapGroup;
    }
    
    public static Node mkWithUpperBound(final Node mobile, final Node bound) {
        return new Draggable(mobile, bound.boundsInParentProperty()).wrapGroup;
    }
    
    public static Node mk(final Node mobile) {
        return new Draggable(mobile).wrapGroup;
    }
    
    public ReadOnlyBooleanProperty movingProperty() { return moving; }
    public Group getWrapGroup() { return wrapGroup; }
}
