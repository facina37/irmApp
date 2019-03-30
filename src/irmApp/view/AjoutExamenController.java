package irmApp.view;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import irmApp.database.ConnexionOracle;
import java.io.IOException;

/**
 * La classe AjoutExamenController permet de gerer l'ajout d'un examen d'IRM à un patient.
 *
 * version 30/03/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class AjoutExamenController implements Initializable {
    
    //Champs du formulaire d'ajout d'un examen
    @FXML
    private DatePicker dateExamen;
    @FXML
    private TextField idMachine, volCrane, axeCrane, volTumeur, ttp, rcbv, mtt;
    @FXML
    private TextField rcbf, lac, idMedecin, naa_cho, cho_cr, lip_cr, naa_cr;    
    
    // connexion à la base de données
    ConnexionOracle maconnection = new ConnexionOracle();
    //créer une variable de la requête
    Statement stmt; 
    
    /**
     * Initializes the controller class.
     * Permet d'indiquer l'information attendue dans les champs du formulaire.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateExamen.setPromptText("jj/mm/aaaa");
        idMachine.setPromptText("ID Machine");
        volCrane.setPromptText("Volume du crane");
        axeCrane.setPromptText("Valeur max axe cranien");
        volTumeur.setPromptText("Volume tumeur");
        ttp.setPromptText("TTP");
        rcbv.setPromptText("rCBV");
        mtt.setPromptText("MTT");
        rcbf.setPromptText("rCBF");
        lac.setPromptText("Lac");
        idMedecin.setPromptText("ID Médecin");
        naa_cho.setPromptText("NAA/Cho");
        cho_cr.setPromptText("Cho/Cr");
        lip_cr.setPromptText("Lip/Cr");
        naa_cr.setPromptText("NAA/Cr");
    }
    
    /**
     * isInputExamenValid() permet de vérifier si les champs du formulaire ne sont pas vides.
     * 
     * @return boolean
     */
    private boolean isInputExamenValid() {
        String errorMessage = "";
        
        if (dateExamen.getValue() == null) {
            errorMessage += "Date invalide !\n";
        } 
        if (idMachine.getText() == null || idMachine.getText().length() == 0) {
            errorMessage += "ID machine invalide !\n";
        }
        if (volCrane.getText() == null || volCrane.getText().length() == 0) {
            errorMessage += "Volume du crâne invalide !\n";
        }
        if (axeCrane.getText() == null || axeCrane.getText().length() == 0) {
            errorMessage += "Valeur maximum de l'axe du crâne invalide !\n";
        }
        if (volTumeur.getText() == null || volTumeur.getText().length() == 0) {
            errorMessage += "Volume de la tumeur invalide !\n";
        }
        if (ttp.getText() == null || ttp.getText().length() == 0) {
            errorMessage += "TTP invalide !\n";
        }
        if (rcbv.getText() == null || rcbv.getText().length() == 0) {
            errorMessage += "rCBV invalide !\n";
        }
        if (mtt.getText() == null || mtt.getText().length() == 0) {
            errorMessage += "MTT invalide !\n";
        }
        if (rcbf.getText() == null || rcbf.getText().length() == 0) {
            errorMessage += "rCBF invalide !\n";
        }
        if (lac.getText() == null || lac.getText().length() == 0) {
            errorMessage += "Lac invalide !\n";
        }
        if(idMedecin.getText() == null || idMedecin.getText().length() == 0){
            errorMessage += "ID Médecin invalide !\n";
        }
        if (naa_cho.getText() == null || naa_cho.getText().length() == 0) {
            errorMessage += "NAA/Cho invalide !\n";
        }
        if (cho_cr.getText() == null || cho_cr.getText().length() == 0) {
            errorMessage += "Cho/Cr invalide !\n";
        }
        if (lip_cr.getText() == null || lip_cr.getText().length() == 0) {
            errorMessage += "Lip/Cr invalide !\n";
        }
        if (naa_cr.getText() == null || naa_cr.getText().length() == 0) {
            errorMessage += "NAA/Cr invalide !\n";
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
     * handleAjoutExamen() permet d'ajouter l'examen remplie dans le fomrulaire 
     * à un patient dans la base de données.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleAjoutExamen(ActionEvent event) throws IOException {
        if (isInputExamenValid()) {
            /*String requeteAjout = "Insert into Examen (idMachine, idPatient, idMedecin, dateExam,"
                   + " volCrane, valMaxAxeCrane, volTumeur, Cho_Cr, Naa_Cr, Naa_Cho, lip_cr, mtt,"
                   + " ttp, rcbv, rcbf, lac) values ("+idMachine+","+idPatient().getId()+","+idMedecin+","
                   + dateExamen+","+volCrane+","+axeCrane+","+volTumeur+","+cho_cr+","+naa_cr+","
                   + naa_cho+","+lip_cr+","+mtt+","+ttp+","+rcbv+","+rcbf+","+lac+");";
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
            }*/
        }
    }        
}
