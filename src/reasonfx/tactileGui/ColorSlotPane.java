package reasonfx.tactileGui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import nl.utwente.cs.caes.tactile.control.TactilePane;

/**
 * A Pane that has two ColorSlots. The background color depends on the ColorItems
 * that are hosted by the ColorSlots. If both ColorSlots don't have ColorItems,
 * the ColorSlotPane will be grey. If one ColorSlot has a ColorItem, the ColorSlotPane
 * will have that color as background. If the remaining ColorSlot is filled with
 * a ColorItem that has a different color than the ColorSlotPane, the first ColorItem
 * will be pushed out.
 */
public class ColorSlotPane extends Pane {
    Rectangle background;
    ColorSlot leftSlot;
    ColorSlot rightSlot;
    
    public ColorSlotPane() {
        background = new Rectangle(190, 90);
        background.setStrokeWidth(1);
        background.setFill(Color.BLACK);
        background.setStroke(Color.BLACK);
        
        leftSlot = new ColorSlot(this);
        leftSlot.relocate(20, 20);
        leftSlot.colorItemProperty().addListener((ObservableValue<? extends TextItem> observable, TextItem oldValue, TextItem newValue) -> {
            if (newValue == null) {
                // If both slots don't host a ColorItem, the ColorSlotPane will be grey
                if (rightSlot.getColorItem() == null) {
                    setBackgroundColor(Color.GREY);
                }
            } else {
                // If the left slot hosts a ColorItem, but the right does not, ColorSlotPane
                // will get the color of the ColorItem hosted by the left slot.
                // If both slots host a ColorItem, and the right slot has a different color
                // than the left slot, then the right ColorItem will be pushed away from
                // its slot.
                if (rightSlot.getColorItem() == null) {
                    setBackgroundColor(newValue.getColor());
                    setBorderColor(newValue.getColor());
                } else if (!newValue.getColor().equals(rightSlot.getColorItem().getColor())) {
                    TactilePane.setAnchor(rightSlot.getColorItem(), null);
                    TactilePane.moveAwayFrom(rightSlot.getColorItem(), newValue, 500);
                    setBackgroundColor(newValue.getColor());
                    setBorderColor(newValue.getColor());
                }
            }
        });
        
        
        rightSlot = new ColorSlot(this);
        rightSlot.relocate(120, 20);
        rightSlot.colorItemProperty().addListener((ObservableValue<? extends TextItem> observable, TextItem oldValue, TextItem newValue) -> {
            if (newValue == null) {
                if (leftSlot.getColorItem() == null) {
                    setBackgroundColor(Color.GREY);
                }
            } else {
                if (leftSlot.getColorItem() == null) {
                    setBackgroundColor(newValue.getColor());
                    setBorderColor(newValue.getColor());
                } else if (!newValue.getColor().equals(leftSlot.getColorItem().getColor())) {
                    TactilePane.setAnchor(leftSlot.getColorItem(), null);
                    TactilePane.moveAwayFrom(leftSlot.getColorItem(), newValue, 500);
                    setBackgroundColor(newValue.getColor());
                    setBorderColor(newValue.getColor());
                }
            }
        });
        
        getChildren().addAll(background, leftSlot, rightSlot);
    }
    
    public final void setBorderColor(Paint color) {
        borderColorProperty().set(color);
    }
    
    public final Paint getBorderColor() {
        return borderColorProperty().get();
    }
    
    public final ObjectProperty<Paint> borderColorProperty() {
        return background.strokeProperty();
    }
    
    public final void setBackgroundColor(Paint color) {
        backgroundColorProperty().set(color);
    }
    
    public final Paint getBackgroundColor() {
        return backgroundColorProperty().get();
    }
    
    public final ObjectProperty<Paint> backgroundColorProperty() {
        return background.fillProperty();
    }
    
    ObjectProperty<TactilePane> tactilePane = new SimpleObjectProperty<TactilePane>() {
        @Override
        public void set(TactilePane pane) {
            if (pane != null) {
                pane.getActiveNodes().add(leftSlot);
                pane.getActiveNodes().add(rightSlot);
            }
            super.set(pane);
        }
    };
    
    public final void setTactilePane(TactilePane tactilePane) {
        tactilePaneProperty().set(tactilePane);
    }
    
    public final TactilePane getTactilePane() {
        return tactilePaneProperty().get();
    }
    
    public final ObjectProperty<TactilePane> tactilePaneProperty() {
        return tactilePane;
    }
}
