/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Florent
 */
public class CustomCell extends ListCell<File> {
    private final Button button = new Button();
    private final Label label = new Label();
    private final Image img;
    
    private static final String BUTTON_STANDBY_STYLE = "-fx-background-color: transparent; -fx-padding: 1,4,1,0;";
    private static final String BUTTON_ON_MOUSE_ENTERED_STYLE = "-fx-background-color: transparent; -fx-padding: 1,4,1,0; -fx-opacity: 0.3;";
    
    public CustomCell(Image img) {
        this.img = img;
        button.setOnAction(new EventHandler<ActionEvent>() {    
            @Override    
            public void handle(ActionEvent event) {    
                getListView().getItems().remove(getItem());

            }
        });
        
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setStyle(BUTTON_ON_MOUSE_ENTERED_STYLE);
            }
        });
        
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setStyle(BUTTON_STANDBY_STYLE);
            }
        });
    }  
    @Override    
    public void updateItem(final File item, boolean empty) {    
        super.updateItem(item, empty);    
        if (item != null) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setMargin(button, new Insets(0,5,0,0));
            
            label.setText(item.toString());
            if(FileUtils.getExtention(item.toString()).equals(MyCipherFile.CIPHER_FILE_EXTENSION)) {
                label.setTextFill(Color.web("#0000FF"));
            } else {
                label.setTextFill(Color.web("#000000"));
            }
            
            ImageView iv = new ImageView(this.img);
            button.setGraphic(iv);
            button.setStyle(BUTTON_STANDBY_STYLE);
            
            
            hbox.getChildren().addAll(button, label);
            setGraphic(hbox);    
        }  else {  
            setGraphic(null); // mandatory
        }  
    }
}
