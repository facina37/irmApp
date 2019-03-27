package irmApp;

import irmApp.model.Patient;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/*App IRM Care BDD IHM
 * Version : 23/03/2019
 * Author : Baudoin, Bogusz
*/
public class MainApp extends Application {
    
    private Stage primaryStage;
    private AnchorPane root;
    private Patient patient;
       
     @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IRM Care");
        //this.primaryStage.getIcons().add(new Image("file:ressources/logo.jpg"));

        showConnexion();
    }
    
    /**
     * Ouvre la page de connexion
     */
    public void showConnexion(){
         try {
            // nouvelle scene pour pop up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/Connexion.fxml"));
            root = (AnchorPane) loader.load();

            // Creer nouvelle scene.
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
               
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
   
    /**
     * Retourne la scene
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
