/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irmApp.view;

import irmApp.database.ConnexionOracle;
import irmApp.MainApp;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import irmApp.database.ConnexionOracle;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author Laure
 */
public class AjoutExamenController implements Initializable {

    @FXML
    private DatePicker dateExamen;
    @FXML
    private TextField idMachine;
    @FXML
    private TextField volCrane;
    @FXML
    private TextField axeCrane;
    @FXML
    private TextField volTumeur;
    @FXML
    private TextField ttp;
    @FXML
    private TextField rcbv;
    @FXML
    private TextField mtt;
    @FXML
    private TextField rcbf;
    @FXML
    private TextField lac;
    @FXML
    private TextField idMedecin;
    @FXML
    private TextField naa_cho;
    @FXML
    private TextField cho_cr;
    @FXML
    private TextField lip_cr;
    @FXML
    private TextField naa_cr;
    
    
    
    ConnexionOracle maconnection = new ConnexionOracle();
    Statement stmt; //créer une variable de la requête
    
    /**
     * Initializes the controller class.
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
