package irmApp.view;

import java.io.IOException;
import java.net.URL;
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
    
    //Partie dialogue
    private Stage dialogStage;
    private boolean okVisite;
    private boolean okMedicament;
    
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
    private void handleAjoutPrevisite(ActionEvent event) {
        if (isInputVisiteValid()) {
            messageSucces.setText("Vous avez ajouté une prévisite à la base de données");
            okVisite = true;
            dateVisite.setDisable(true);
            poids.setDisable(true);
            numLot.setDisable(true);
            freqCardiaque.setDisable(true);     
            typeLot.setDisable(true);
            typeLot.setDisable(true);
            tension.setDisable(true);
            leucocytes.setDisable(true);
            hemoglobine.setDisable(true);
            idMedecin.setDisable(true);
            ajoutVisite.setDisable(true);
            //dialogStage.close();
        }
    }  
    
    
    @FXML
    private void handleAjoutMedicament(ActionEvent event) {
        if(ajoutVisite.isDisable() == true){
            if (isInputMedicamentValid()) {
                messageSucces.setText("Vous avez ajouté un médicament à la base de données");
                okMedicament = true;
                //dialogStage.close();
            }
        }
        else {
            messageSucces.setText("Vous devez ajouter une prévisite auparavant");
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
        if (typeLot.getValue() != "DiOrZen" && typeLot.getValue() != "Placebo") {
            errorMessage += "Type de lot invalide !\n";
        }
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
            alert.initOwner(dialogStage);
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
            alert.initOwner(dialogStage);
            alert.setTitle("Attention!");
            alert.setHeaderText("Veuillez corriger.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }   
}
