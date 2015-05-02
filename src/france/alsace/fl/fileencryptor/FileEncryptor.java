/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Florent
 */
public class FileEncryptor extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLFileEncryptor.fxml"));
        
        Scene scene = new Scene(root);
        Image icon = new Image("/img/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("FX File Encryptor v1.0.0");
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Encryptor enc = new Encryptor(65536,256);
            Decryptor dec = new Decryptor(65536,256);
            
            MyCipherFile mcf = enc.cipher(new MyFile("test2".getBytes("UTF-8"), "test"), "plop12");
            
            System.out.println(new String(mcf.getData()) + "/" + new String(mcf.getSalt()));
            System.out.println(new String((dec.decipher(mcf, "plop12")).getData()));
            
            launch(args);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }
    
}
