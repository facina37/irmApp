package irmApp;

import irmApp.model.Patient;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** La classe MainApp permet de gerer le lancement de l'application.
 * 
 * Version : 30/03/2019
 * Author : Laure Baudoin & Marie Bogusz
*/
public class MainApp extends Application {
    
    private Stage primaryStage;
    private AnchorPane root;
    private Patient patient;
       
     @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IRM Care");
        this.primaryStage.setMinHeight(600);
        this.primaryStage.setMinWidth(800);
        this.primaryStage.getIcons().add(new Image("file:ressources/logo.png"));

        showConnexion();
    }
    
    /**
     * showConnexion() permet d'ouvrir la page de connexion, 1ere page de 
     * l'application.
     */
    public void showConnexion(){
         try {
            // nouvelle scene pour pop up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/Connexion.fxml"));
            root = (AnchorPane) loader.load();

            // Creer nouvelle scene et l'ouvre.
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
               
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
   
    /**
     * getPrimaryStage() permet de retourner la scene
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * getPatient() permet de retourner le patient courant
     * @return patient
     */
    public Patient getPatient() {
        return patient;
    }
}
