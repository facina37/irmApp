package irmApp.view;

import irmApp.database.ConnexionOracle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * La classe AjoutVisite Controller permet de gerer l'ajout des informations 
 * récoltées par le médecin lors d'une prévisite à un patient.
 * 
 * version 25/03/2019
 * @author Laure Baudoin & Marie Bogusz
 */
public class AjoutVisiteController implements Initializable {
    
    //Partie Ajout Prévisite
    @FXML
    private DatePicker dateVisite;
    @FXML
    private TextField poids;
    @FXML
    private TextField numLot;
    @FXML
    private TextField freqCardiaque;
    @FXML
    private ComboBox<String> typeLot;//Choix entre DiOrZen ou placebo
    @FXML
    private TextField tension;
    @FXML
    private TextField leucocytes;
    @FXML
    private TextField hemoglobine;
    @FXML
    private TextField idMedecin;
    @FXML
    private Label messageSucces;
    @FXML
    private Button ajoutVisite;    
    
    //Partie Ajout Médicament
    @FXML
    private TextField medicament;
    @FXML
    private TextArea raisonPrise;
    
    private int visiteActuelle = -1;
    
    private int idPatient = -1; //récupéré depuis la page précédente
    
    ConnexionOracle maconnection = new ConnexionOracle();
    Statement stmt; //créer une variable de la requête
    
    /**
     * Initialise le controller.
     * Permet d'indiquer ce qui est attendu dans les champs du formulaire d'ajout prévisite.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateVisite.setPromptText("jj/mm/aaaa");
        poids.setPromptText("Poids (kg)");
        numLot.setPromptText("Numéro du lot");
        freqCardiaque.setPromptText("Fréquence cardiaque");
        
        ObservableList<String> list = FXCollections.observableArrayList(
            "DiOrZen", "Placebo"
        );
        
        typeLot.setItems(list);
        typeLot.setPromptText("Type de lot");
        tension.setPromptText("Tension");
        leucocytes.setPromptText("Taux de leucocytes");
        hemoglobine.setPromptText("Taux d'hémoglobine");
        idMedecin.setPromptText("Identifiant médecin");
        medicament.setPromptText("Principe actif");
        raisonPrise.setPromptText("Raison de la prise");
        messageSucces.setText("");
    }    
    
    @FXML
    private void handleAjoutPrevisite(ActionEvent event) throws IOException {
        if (isInputVisiteValid()) {
            AjoutPrevisite(event);
            messageSucces.setText("Vous avez ajouté une prévisite à la base de données");

            dateVisite.setDisable(true);
            poids.setDisable(true);
            numLot.setDisable(true);
            freqCardiaque.setDisable(true);     
            typeLot.setDisable(true);
            tension.setDisable(true);
            leucocytes.setDisable(true);
            hemoglobine.setDisable(true);
            idMedecin.setDisable(true);
            ajoutVisite.setDisable(true);
        }
    } 
    
    private void AjoutPrevisite(ActionEvent event)throws IOException 
    {
        String requeteAjout = "Insert into Previsite (idPatient, idMedecin, dateVisie,"
                   + " nomLot, poids, freqcardiaque, tension, tauxleuco, tauxhemoglo) values ("+idPatient+","+idMedecin+","+dateVisite+","
                   + numLot+","+poids+","+freqCardiaque+","+tension+","+leucocytes+","+hemoglobine+");";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            stmt.executeQuery(requeteAjout);
            //petit pop up
            JOptionPane.showMessageDialog(null, "Enregistré avec succès");
            System.out.println("Enregistré");
            //Revenir à la page d'accueil technicien
            Parent root = FXMLLoader.load(getClass().getResource("AccueilTechnicien.fxml"));
            Scene scene = (Scene) ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");  
        }
    }
    
    
    @FXML
    private void handleAjoutMedicament(ActionEvent event) throws IOException {
        if(ajoutVisite.isDisable() == true){
            if (isInputMedicamentValid()) {
                
                int idMedicament;
                int idPrevisite;
                
                idPrevisite = recupIdPrevisite();
                ajoutMedicament();
                
                idMedicament = recupIdMedicament();
                if(idMedicament != -1 && idPrevisite != -1)
                {
                    ajoutIngerer(idMedicament, idPrevisite);
                }
                 
                //Message juste pour la version demo
                messageSucces.setText("Vous avez ajouté un médicament à la base de données");
            }
        }
        else {
            messageSucces.setText("Vous devez ajouter une prévisite auparavant");
        }
    }
    
    private int recupIdPrevisite(){
        String requeteVerif = "select * from Previsite ORDER BY idVisite DESC";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerif);
            return result.getInt("idvisite");
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
        return -1;
    }
    
    private void ajoutMedicament() 
    {
        //verifie si il y a deja un medoc avec le mm nom
        String requeteVerif = "select * from Medicament";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerif);
            boolean dejaExistant = false;
            while(result.next()){
                if(result.getString("nommedic").equals(medicament.getText()))
                {
                    dejaExistant = true;
                }
            }
            System.out.println("Enregistré");
                    
            if(dejaExistant)
            {
                String AjoutMedoc = "insert into Medicament values (1, "+medicament.getText()+");";
                stmt.executeQuery(AjoutMedoc);
                messageSucces.setText("Vous avez ajouté un médicament à la base de données");
            }
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
    }
    
    private int recupIdMedicament()
    {
        String requeteVerif = "select * from Medicament where nommedic = "+medicament.getText()+");";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerif);
            return result.getInt("idmedicament");
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
        return -1;
    }
    
    private void ajoutIngerer(int idMedicament, int idPrevisite){
        String requeteAjout = "insert into Ingerer values ("+medicament.getText()+","+idPrevisite+","+raisonPrise.getText()+");";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            stmt.executeQuery(requeteAjout);
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
    }
        
    /**
    * handleTerlmine() est appelé lorsque le médecin à fini d'ajouter une prévisite.
    * Permet de revenir à la page d'accueil médecin.
    */
    @FXML
    private void handleTermine(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AccueilMedecin.fxml"));
        Scene scene = (Scene) ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
    
    /**
    * isInputVisiteValid() est appelé lorsque le boutton ajouter un médicament à 
    * une prévisite est utilisé.
    * Permet de tester la validité des champs du formulaire d'ajout d'une prévisite.
    */
    private boolean isInputVisiteValid() {
        String errorMessage = "";
        
        if (dateVisite.getValue() == null) {
            errorMessage += "Date invalide !\n";
        } 
        if (poids.getText() == null || poids.getText().length() == 0) {
            errorMessage += "Poids invalide !\n";
        }
        if (numLot.getText() == null || numLot.getText().length() == 0) {
            errorMessage += "Numéro de lot invalide !\n";
        }
        if (freqCardiaque.getText() == null || freqCardiaque.getText().length() == 0) {
            errorMessage += "Fréquence cardiaque invalide !\n";
        }
        if(typeLot.getValue() == null)
            errorMessage += "Type de lot invalide !\n";
        else
            if (!typeLot.getValue().equals("DiOrZen") && !typeLot.getValue().equals("Placebo"))
                errorMessage += "Type de lot invalide !\n";
        if (tension.getText() == null || tension.getText().length() == 0) {
            errorMessage += "Tension invalide !\n";
        }
        if (leucocytes.getText() == null || leucocytes.getText().length() == 0) {
            errorMessage += "Taux de leucocytes invalide !\n";
        }
        if (hemoglobine.getText() == null || hemoglobine.getText().length() == 0) {
            errorMessage += "Taux d'hémoglobine invalide !\n";
        }
        if (idMedecin.getText() == null || idMedecin.getText().length() == 0) {
            errorMessage += "ID médecin invalide !\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(null);
            alert.setTitle("Attention !");
            alert.setHeaderText("Veuillez corriger.");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
    
    /**
    * isInputMedicamentValid() est appelé lorsque le boutton ajouter une prévisite est utilisé.
    * Permet de tester la validité des champs du formulaire d'ajout d'un médicament
    * lors d'une prévisite.
    */
    private boolean isInputMedicamentValid() {
        String errorMessage = "";
        if (medicament.getText() == null || medicament.getText().length() == 0) {
            errorMessage += "Principe actif invalide!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(null);
            alert.setTitle("Attention!");
            alert.setHeaderText("Veuillez corriger.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }   
}
