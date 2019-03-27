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
import javax.swing.JOptionPane;
import irmApp.database.ConnexionOracle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Laure
 */
public class VerifExamenController implements Initializable{
    //données
    @FXML
    private Label gradeMachine;
    @FXML
    private Label risqueTotal;
    @FXML
    private Label volCrane;
    @FXML
    private Label axeCrane;
    @FXML
    private Label volTumeur;
    @FXML
    private Label mtt;
    @FXML
    private Label ttp;
    @FXML
    private Label rcbv;
    @FXML
    private Label rcbf;
    @FXML
    private Label cho_cr;
    @FXML
    private Label naa_cr;
    @FXML
    private Label naa_cho;
    @FXML
    private Label lac;
    @FXML
    private Label lip_cr;
    private boolean valide;
    
    //partie erreur
    @FXML
    private Label messageErreur;
    @FXML
    private RadioButton refaire;
    @FXML
    private RadioButton suppression;
    @FXML
    private Button valideErreur;
    
    //partie choix du grade
    @FXML
    private ComboBox grade;
    @FXML
    private Button valideGrade;
    
    private int idExamen; //récupéré de la page précédente
    private int idPatient;
    private Stage dialogStage;
    
    private ConnexionOracle maconnection = new ConnexionOracle();
    private Statement stmt; //créer une variable de la requête
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> data = FXCollections.observableArrayList("I","II","III","IV");
        grade.setPromptText("Faites votre vhoix");
        grade.setItems(data);
        recuperationInfos();
        gestionErreurs();
    }
    
    /*Qd on valide une des deux decisions lors d'une erreur d'IRM*/
    @FXML
    private void handleValideErreur(ActionEvent event) {
        if(isErrorChoiceValid()){
            if (refaire.isSelected()){
                //==> on doit supprimer les données de cet examen
                String requeteSuppr = "delete from examen where idexamen = "+idExamen+";";
                try {
                    stmt = maconnection.ObtenirConnection().createStatement();
                    stmt.executeQuery(requeteSuppr);
                    System.out.println("L'examen a bien été supprimé");
                }
                catch(SQLException e) {
                    System.out.println("Erreur, l'examen n'a pas été supprimé");
                }
                //==> on doit en reprogrammer un dans deux jours
                String requeteAgenda = "update agenda set PROCHAINEXAMEN = dateJour + 2 where idpatient = "+idPatient+";";
                try{
                    stmt = maconnection.ObtenirConnection().createStatement();
                    stmt.executeQuery(requeteAgenda);
                    System.out.println("Un nouvel examen a été programmé dans 2j");
                }
                catch(SQLException e) {
                    System.out.println("Un nouvel examen n'a pas pu etre programmé");
                }
            }
            if (suppression.isSelected()){
                //==>on supprime l'avant dernier examen effectué par ce patient
                //petit pop up
                JOptionPane.showMessageDialog(null, "Contactez la personne responsable de la gestion des bases de données pour cela");
            }
        }
    }
    
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
    
    /*Qd on valide une des deux decisions lors d'une erreur d'IRM*/
    @FXML
    private void handleValideGrade(ActionEvent event) {
        if(isGradeChoiceValid()) {
            String requeteGrade = "update examen set gradeMedecin = "+grade.getValue()+" where idpatient = "+idPatient+";";
            try {
                stmt = maconnection.ObtenirConnection().createStatement();
                stmt.executeQuery(requeteGrade);
                System.out.println("La décision a bien été prise en compte");
            }
            catch(SQLException e) {
                System.out.println("Erreur, décision non prise en compte");
            }
        }
    }
    
    private boolean isGradeChoiceValid() {
        String errorMessage = "";
        
        if (grade.getValue() != "I" && grade.getValue() != "II" && grade.getValue() != "III" && grade.getValue() != "IV") {
            errorMessage += "Grade invalide!\n";
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

    /*Affiche les données de l'examen*/
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
    
    /*Si on n'a pas d'erreurs anatomiques on ne peut pas accéder à la zone d'erreur,
    sinon on ne peut pas laisser le médecin décider d'un grade*/
    public void gestionErreurs(){
        if(valide == true){
            messageErreur.setText("L'IRM n'a pas présenté d'erreurs anatomiques");
            refaire.setDisable(true);
            suppression.setDisable(true);
            valideErreur.setDisable(true);
        } else {
            messageErreur.setText("L'IRM a présenté des erreurs anatomiques");
            grade.setDisable(true);
            valideGrade.setDisable(true);
        }
    }    
}
