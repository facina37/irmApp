package irmApp.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import irmApp.database.ConnexionOracle;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class permet de gerer la connexion soit d'un médecin soit d'un technicien. 
 * Un médecin et un technicien n'a pas accés aux mêmes données.
 *
 * IRM Care App IHM Gphy
 * Version : 24/03/2019
 * @author Laure Baudoin & Marie Bogusz
 */
public class ConnexionController implements Initializable{
    
    @FXML
    private TextField login;
    @FXML
    private PasswordField pwd;
    @FXML
    private Label message;
    
    // connexion à la base de données
    private ConnexionOracle maconnection = new ConnexionOracle();
    //créer une variable de la requêteperche jaunepercha 
    private Statement stmt; 
        
     /**
     * Initializes the controller class.
     * Permet d'indiquer ce qui est attendu dans les champs de la page connexion
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        login.setPromptText("Identifiant");
        pwd.setPromptText("Mot de passe");
        message.setText("");
    }
           
    /**
    * handleConnexion() est appelé lorsque le boutton connexion est utilisé
    * Permet de se connecter à l'interface médecin ou technicien selon les identifiants.
    * Affiche un message d'erreur si l'identifiant ou le mdp n'existe pas.
    * 
    * @param event
    */
    @FXML
    private void handleConnexion(ActionEvent event) throws IOException {
        String requeteVerifLog = "select * from Medecin where login = '"+login.getText()+"' and pwd = '"+pwd.getText()+"'";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();            
            ResultSet result = stmt.executeQuery(requeteVerifLog);
            while(result.next()){
                Parent root = FXMLLoader.load(getClass().getResource("AccueilMedecin.fxml"));
                Scene scene = (Scene) ((Node) event.getSource()).getScene();
                scene.setRoot(root);
                break;
            }            
        }
        catch(SQLException e){
            System.out.println(e);
        }
        requeteVerifLog = "select * from Technicien where login = '"+login.getText()+"' and pwd = '"+pwd.getText()+"'";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerifLog);
            while(result.next()){
                Parent root = FXMLLoader.load(getClass().getResource("AccueilTechnicien.fxml"));
                Scene scene = (Scene) ((Node) event.getSource()).getScene();
                scene.setRoot(root);
                break;
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }        
        
        //si on ne se connecte pas à une des deux pages
        message.setText("Identifiant et mot de passe incorrects");
    }
    
    /**
    * handleReset() est appelé lorsque le boutton reset est utilisé
    * Permet de vider les champs de la page connexion
    * 
    * @param event
    */
    @FXML
    private void handleReset(ActionEvent event) {
        login.setText("");
        pwd.setText("");
        message.setText("");
    }  
}
