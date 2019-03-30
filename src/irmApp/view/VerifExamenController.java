package irmApp.view;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import irmApp.database.ConnexionOracle;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * La classe AjoutExamenController permet de gerer l'ajout des informations 
 * récoltées par le technicien lors d'un examen d'IRM à un patient.
 * 
 * version 25/03/2019
 * @author Laure Baudoin & Marie Bogusz
 */
public class VerifExamenController implements Initializable{
    //données
    @FXML
    private Label gradeMachine, risqueTotal, volCrane, axeCrane, volTumeur;
    @FXML
    private Label mtt, ttp, rcbv, rcbf, cho_cr, naa_cr, naa_cho, lac, lip_cr;
    private boolean valide;
    
    //partie erreur
    @FXML
    private Label messageErreur, titreErreur;
    @FXML
    private RadioButton refaire, suppression;
    @FXML
    private Button valideErreur;
    
    //partie choix du grade
    @FXML
    private ComboBox grade;
    @FXML
    private Button valideGrade;
    @FXML
    private Label titreGrade;
    
    private int idExamen; //récupéré de la page précédente
    private int idPatient;
    private Stage dialogStage;
    
    // connexion à la base de données
    private ConnexionOracle maconnection = new ConnexionOracle();
    //créer une variable de la requête
    private Statement stmt; 
    
    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> data = FXCollections.observableArrayList("I","II","III","IV");
        grade.setPromptText("Faites votre choix");
        grade.setItems(data);
        recuperationInfos();
        
        //Initialisation de l'affichage pour la partie validité de l'examen
        refaire.setVisible(false);
        suppression.setVisible(false);
        valideErreur.setVisible(false);
        grade.setVisible(false);
        valideGrade.setVisible(false);
        titreGrade.setVisible(false);
        titreErreur.setVisible(false);
        
        //test pour l'affichage
        gestionErreurs();
    }
    
    /**
     * handleValideErreur() permet de valider la décision du médecin sur l'examen qui a une erreur d'IRM 
     * et de revenir sur la page d'accueil du médecin.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleValideErreur(ActionEvent event) throws IOException {        
        
        Parent root = FXMLLoader.load(getClass().getResource("AccueilMedecin.fxml"));
        Scene scene = (Scene) ((Node) event.getSource()).getScene();
        scene.setRoot(root);        
        
        //if(isErrorChoiceValid()){
        //    if (refaire.isSelected()){
        //        //==> on doit supprimer les données de cet examen
        //        String requeteSuppr = "delete from examen where idexamen = "+idExamen+";";
        //        try {
        //            stmt = maconnection.ObtenirConnection().createStatement();
        //            stmt.executeQuery(requeteSuppr);
        //            System.out.println("L'examen a bien été supprimé");
        //        }
        //        catch(SQLException e) {
        //            System.out.println("Erreur, l'examen n'a pas été supprimé");
        //        }
        //        //==> on doit en reprogrammer un dans deux jours
        //        String requeteAgenda = "update agenda set PROCHAINEXAMEN = dateJour + 2 where idpatient = "+idPatient+";";
        //        try{
        //            stmt = maconnection.ObtenirConnection().createStatement();
        //            stmt.executeQuery(requeteAgenda);
        //            System.out.println("Un nouvel examen a été programmé dans 2j");
        //        }
        //        catch(SQLException e) {
        //            System.out.println("Un nouvel examen n'a pas pu etre programmé");
        //        }
        //    }
        //    if (suppression.isSelected()){
        //        //==>on supprime l'avant dernier examen effectué par ce patient
        //        //petit pop up
        //        JOptionPane.showMessageDialog(null, "Contactez la personne responsable de la gestion des bases de données pour cela");
        //    }
        //}
    }
    
    /**
     * isErrorChoiceValid() permet de vérifier l'action du médecin lors de la 
     * validation de sa décision.
     * 
     * @return boolean
     */
    private boolean isErrorChoiceValid() {
        String errorMessage = "";
        
        if (refaire.isSelected() == false && suppression.isSelected() == false) {
            errorMessage += "Vous devez faire un choix avant de valider";
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
     * handleValideGrade() valider la décision du médecin sur le grade de l'examen
     * et de revenir sur la page d'accueil du médecin.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleValideGrade(ActionEvent event) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("AccueilMedecin.fxml"));
        Scene scene = (Scene) ((Node) event.getSource()).getScene();
        scene.setRoot(root);        
        
        //if(isGradeChoiceValid()) {
        //    String requeteGrade = "update examen set gradeMedecin = "+grade.getValue()+" where idpatient = "+idPatient+";";
        //    try {
        //        stmt = maconnection.ObtenirConnection().createStatement();
        //        stmt.executeQuery(requeteGrade);
        //        System.out.println("La décision a bien été prise en compte");
        //    }
        //   catch(SQLException e) {
        //       System.out.println("Erreur, décision non prise en compte");
        //    }
        //}
    }

    /**
     * recuperationInfos() permet d'afficher les données de l'examen à vérifier.
     */
    public void recuperationInfos(){
        String requete = "select * from examen where idexamen = "+idExamen+";";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()){
                gradeMachine.setText(gradeMachine.getText()+result.getString("GRADEMACHINE"));
                risqueTotal.setText(risqueTotal.getText()+result.getString("RISQUE"));
                volCrane.setText(volCrane.getText()+result.getString("VOLCRANE"));
                axeCrane.setText(axeCrane.getText()+result.getString("VALMAXAXECRANE"));
                volTumeur.setText(volTumeur.getText()+result.getString("VOLTUMEUR"));
                ttp.setText(ttp.getText()+result.getString("TTP"));
                rcbv.setText(rcbv.getText()+result.getString("RCBV"));
                mtt.setText(mtt.getText()+result.getString("MTT"));
                rcbf.setText(rcbf.getText()+result.getString("RCBF"));
                lac.setText(lac.getText()+result.getString("LAC"));
                naa_cho.setText(naa_cho.getText()+result.getString("NAA_CHO"));
                cho_cr.setText(cho_cr.getText()+result.getString("CHO_CR"));
                lip_cr.setText(lip_cr.getText()+result.getString("LIP_CR"));
                naa_cr.setText(naa_cr.getText()+result.getString("NAA_CR"));
                valide = result.getBoolean("VALIDE");
                //Affiche les bons champs selon la vlidité de l'examen
                gestionErreurs();
                idPatient = result.getInt("IDPATIENT");
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }
    
    /**
     * gestionErreurs() permet d'acceder au bon formulaire selon la validité 
     * (si l'examen à une erreur anatomique ou non) de l'axamen à vérifier.
     */
    public void gestionErreurs(){
        if(valide == false){
            messageErreur.setText("L'IRM a présenté d'erreurs anatomiques");
            refaire.setVisible(true);
            suppression.setVisible(true);
            valideErreur.setVisible(true);
            titreErreur.setVisible(true);
        } else {
            messageErreur.setVisible(false);
            grade.setVisible(true);
            valideGrade.setVisible(true);
            titreGrade.setVisible(true);
        }
    }    
}