package france.alsace.fl.fileencryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 *
 * @author Florent
 */
public class FXMLFileEncryptorController implements Initializable {

    @FXML
    private ToggleGroup radioButtonModeGroup;
    @FXML
    private RadioButton radioButtonCipher;
    @FXML
    private RadioButton radioButtonDecipher;
    @FXML
    private Button buttonChooseFiles;
    @FXML
    private ListView<File> listViewFiles;
    @FXML
    private TextField textFieldChooseDestinationFolder;
    @FXML
    private PasswordField textfieldPassword;
    @FXML
    private Button buttonCipherOrDecipher;
    @FXML
    private Label labelDestinationFolder;
    @FXML
    private Button buttonClearListview;
    @FXML
    private Button buttonDeleteSelection;
    @FXML
    private Label labelInfo;
    @FXML
    private Label labelError;
    @FXML
    private Label labelCalcul;
    @FXML
    private ProgressIndicator progress;
    
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private ObservableList<File> obsListFiles = FXCollections.observableArrayList();
    
    private static final String BTN_FILE_TO_CIPHER = "Sélectionner les fichiers à chiffrer";
    private static final String BTN_FILE_TO_DECIPHER = "Sélectionner les fichiers à déchiffrer";
    private static final String BTN_CIPHER = "Chiffrer";
    private static final String BTN_DECIPHER = "Déchiffrer";
    private static final String FILE_CHOOSER_CIPHER = "Sélectionner les fichiers à chiffrer";
    private static final String FILE_CHOOSER_DECIPHER = "Sélectionner les fichiers à déchiffrer";
    private static final String DIRECTORY_CHOOSER_TITLE = "Sélectionner le dossier de destination";
    private static final int PASSWORD_ITERATIONS = 65536;
    private static final int KEY_SIZE = 256;
    private static final String ALPHANUMERIC_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int NAME_SIZE = 20;
    private static final int THREAD_POOL = 10;
    private static final String ERROR_MISSING_DESTINATION_FOLDER = "Veuillez choisir le dossier de destination des fichiers";
    private static final String ERROR_MISSING_PASSWORD = "Veuillez entrer le mot de passe";
    private static final String ERROR_NOT_DECIPHERING_FILES = "Certains fichiers ne sont pas des fichiers à déchiffrer";
    private static final String ERROR_EMPTY_LIST = "Veuillez sélectionner au moins un fichier";
    private static final String ERROR_DURING_CIPHERING_OR_DECIPHERING = "Des erreurs sont survenues durant le traitement";
    private static final String FILE_OK = " fichier(s) traité(s)";
    private static final String ERROR = " erreur(s) avérée(s)";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewFiles.setItems(obsListFiles);
        this.fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        this.directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        this.listViewFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Image img = new Image(getClass().getResourceAsStream("/img/red-cross.gif"));
        listViewFiles.setCellFactory(new Callback<ListView<File>,ListCell<File>>() {  
                @Override  
                public ListCell<File> call(ListView<File> list) {  
                    return new CustomCell(img);  
                }  
            });
        progress.setVisible(false);
        labelInfo.textProperty().bind(Bindings.size(obsListFiles).asString().concat(" élément(s) dans la liste"));
    }    
    
    @FXML
    private void chooseFiles(ActionEvent event) {
        if(radioButtonCipher.isSelected()) {
            this.fileChooser.setTitle(FILE_CHOOSER_CIPHER);
            this.fileChooser.getExtensionFilters().clear();
            this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
            );
        } else {
            this.fileChooser.setTitle(FILE_CHOOSER_DECIPHER);
            this.fileChooser.getExtensionFilters().clear();
            this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers chiffrés", "*.ffe"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );
        }
        List<File> listFiles = this.fileChooser.showOpenMultipleDialog(this.labelInfo.getScene().getWindow());
        if(listFiles != null) {
            obsListFiles.addAll(listFiles);
        }
    }

    @FXML
    private void cipherOrDecipher(ActionEvent event) {
        this.labelError.textProperty().set("");
        this.labelError.setText("");
        
        if(this.obsListFiles.size() == 0) {
            this.labelError.setText(ERROR_EMPTY_LIST);
        } else if(this.textFieldChooseDestinationFolder.getText().equals("")) {
            this.labelError.setText(ERROR_MISSING_DESTINATION_FOLDER);
        } else if(this.textfieldPassword.getText().equals("")) {
            this.labelError.setText(ERROR_MISSING_PASSWORD);
        } else {
            
            Counter cntSucess = new Counter();
            Counter cntError = new Counter();
            labelCalcul.textProperty().bind(cntSucess.getCounter().asString()
                    .concat(FILE_OK)
                    .concat("/")
                    .concat(cntError.getCounter().asString())
                    .concat(ERROR));

            ExecutorService threadExecutor = Executors.newFixedThreadPool(THREAD_POOL);

            progress.setVisible(true);
            buttonCipherOrDecipher.setDisable(true);
            
            if(this.radioButtonCipher.isSelected()) {
                Encryptor encryptor = new Encryptor(PASSWORD_ITERATIONS, KEY_SIZE);
                List<String> names = new ArrayList<>();
                
                for(File f : this.obsListFiles) {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                //transform the file to a byte array
                                byte[] data = Files.readAllBytes(Paths.get(f.getPath()));

                                //cipher MyFile to MyCipherFile
                                MyCipherFile mcf = encryptor.cipher(new MyFile(data, f.getName()), textfieldPassword.getText());

                                if(mcf!=null) {
                                    //generate a file name and put it in the list of already-generate names file
                                    String name = generateName(NAME_SIZE, names);
                                    names.add(name);

                                    //serialize MyCipherFile
                                    FileOutputStream fos = new FileOutputStream(textFieldChooseDestinationFolder.getText() + "\\" + name + "." + MyCipherFile.CIPHER_FILE_EXTENSION);
                                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                                    oos.writeObject(mcf);
                                    oos.close();
                                }

                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        if(mcf!=null) {
                                            cntSucess.increment();
                                        } else {
                                            cntError.increment();
                                            if(cntError.getValue()==1) {
                                                labelError.setText(ERROR_DURING_CIPHERING_OR_DECIPHERING);
                                            }
                                        }
                                    }
                                });

                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLFileEncryptorController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLFileEncryptorController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    threadExecutor.execute(t);
                }
                
            } else {
                Decryptor decryptor = new Decryptor(PASSWORD_ITERATIONS, KEY_SIZE);
                for(File f : this.obsListFiles) {
                    if(FileUtils.getExtention(f.getName()).equals(MyCipherFile.CIPHER_FILE_EXTENSION)) {
                        Thread t = new Thread() {
                            public void run() {
                                try {
                                    //deserialize MyCipherFile
                                    FileInputStream fis = new FileInputStream(f);
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                                    MyCipherFile mcf = (MyCipherFile) ois.readObject();
                                    ois.close();

                                    //decipher MyCipherFile to MyFile
                                    MyFile mf = decryptor.decipher(mcf, textfieldPassword.getText());
                                    
                                    //write the deciphered file with his right name and extension
                                    if(mf!=null) {
                                        File outputFile = new File(textFieldChooseDestinationFolder.getText() + "\\" + mf.getName());
                                        FileOutputStream fos = new FileOutputStream(outputFile);
                                        fos.write(mf.getData());
                                        fos.close();
                                    }
                                    
                                    Platform.runLater(new Runnable() {
                                        public void run() {
                                            if(mf!=null) {
                                                cntSucess.increment();
                                            } else {
                                                cntError.increment();
                                                if(cntError.getValue()==1) {
                                                    labelError.setText(ERROR_DURING_CIPHERING_OR_DECIPHERING);
                                                }
                                            }
                                        }
                                    });
                                    
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(FXMLFileEncryptorController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException | ClassNotFoundException ex) {
                                    Logger.getLogger(FXMLFileEncryptorController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        };
                        threadExecutor.execute(t);
                        
                    } else {
                        this.labelError.setText(ERROR_NOT_DECIPHERING_FILES);
                    }
                }
            }
            
            threadExecutor.shutdown();
                
            Thread t = new Thread() {
                public void run() {
                    while(!threadExecutor.isTerminated()) {
                    }

                    Platform.runLater(new Runnable() {
                        public void run() {
                            progress.setVisible(false);
                            buttonCipherOrDecipher.setDisable(false);
                        }
                    });
                }
            };
            t.start();
        }
    }

    @FXML
    private void changeModeCipher(ActionEvent event) {
        this.buttonChooseFiles.setText(BTN_FILE_TO_CIPHER);
        this.buttonCipherOrDecipher.setText(BTN_CIPHER);
    }

    @FXML
    private void changeModeDecipher(ActionEvent event) {
        this.buttonChooseFiles.setText(BTN_FILE_TO_DECIPHER);
        this.buttonCipherOrDecipher.setText(BTN_DECIPHER);
    }

    @FXML
    private void clearListView(ActionEvent event) {
        this.obsListFiles.clear();
    }

    @FXML
    private void chooseDestinationFolder(MouseEvent event) {
        this.fileChooser.setTitle(DIRECTORY_CHOOSER_TITLE);
        File file = this.directoryChooser.showDialog(this.labelInfo.getScene().getWindow());
        if (file != null) {
            this.textFieldChooseDestinationFolder.setText(file.getPath());
        }
    }

    @FXML
    private void deleteSelection(ActionEvent event) {
        deleteSelection();
    }
    
    /**
     * To delete the selection in the list
     */
    private void deleteSelection() {
        ObservableList<File> tmp = this.listViewFiles.getSelectionModel().getSelectedItems();
        obsListFiles.removeAll(tmp);
    }
    
    /**
     * To generate a String which isn't in the list
     * @param size size of the String
     * @param names already generate String
     * @return the generate String
     */
    private String generateName(int size, List<String> names) {
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<size; i++) {
            double idx = Math.random() * ALPHANUMERIC_CHARACTERS.length();
            sb.append(ALPHANUMERIC_CHARACTERS.charAt((int)idx));
        }
        
        if(names.contains(sb.toString()) && names!=null) {
            return generateName(size, names);
        } else {
            return sb.toString();
        }
    }

    @FXML
    private void deleteSelectionOnSupprPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.DELETE)) {
            deleteSelection();
        }
    }
}
