package reasonfx.tactileGui;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

//public class TextItem /*extends Text*/ {
public class TextItem extends Text {

//    private final Text text;//new
    
    public TextItem() {
        super("test");//old
//        this.text = new Text();//new
//        this.setFont(Font.font(null, FontWeight.BOLD, 36));//old
//        this.text.setFont(Font.font(null, FontWeight.BOLD, 36));//new?
//        this.text.setLayoutX(50);//new
//        this.text.setLayoutY(50);//new
    }
    
    public TextItem(String text) {
        super(text);//old
//        this.text = new Text(text);//new
//        this.text.setLayoutX(50);//new
//        this.text.setLayoutY(50);//new
    }
    
    public TextItem(String text, Font font) {
        super(text);//old
//        this.text = new Text();//new
//        this.text.setFont(font);//new
//        this.text.setLayoutX(50);//new
//        this.text.setLayoutY(50);//new
    }
    
    public void setColor(Color color) {
        colorProperty().set(color);
    }
    
    public Paint getColor() {
        return colorProperty().get();
    }
    
    public ObjectProperty<Paint> colorProperty() {
//        return this.text.fillProperty();//new
        return super.fillProperty();//old
    }
    

    
//    public void setText(String text) {
//        textProperty().set(text);
//    }
//    public String getText() {
//        return textProperty().get();
//    }
//    
//    public StringProperty textProperty() {
//        return this.text.textProperty();
//    }
//    
//    
//    
//    public void layoutX(double value) {
//        layoutXProperty().set(value);
//    }
//  
//    public double getlayoutX() {
//        return layoutXProperty().get();    
//    }
//    
//    public DoubleProperty layoutXProperty() {
//        return this.text.layoutXProperty();
//    }
    
    
    
//    public void setFont(Font font) {
//        fontProperty().set(font);
//    }
//    public Font getFont() {
//        return fontProperty().get();
//    }
//    
//    public ObjectProperty<Font> fontProperty() {
//        return this.text.fontProperty();
//    }
//    
    
}
