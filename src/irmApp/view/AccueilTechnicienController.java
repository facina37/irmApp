package irmApp.view;

import irmApp.model.Patient;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import irmApp.database.ConnexionOracle;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * La classe AccueilTechnicienController permet de gerer la page d'accueil des techniciens.
 * Permet d'afficher la liste des patients faisant partie de l'étude IRMCare.
 * Permet d'acceder à la page d'ajout d'un examen une fois un patient selectionné dans la liste.
 *
 * version 30/03/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class AccueilTechnicienController implements Initializable {
    // TableView de la liste des patients
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> idColumn, groupeColumn, firstNameColumn;
    @FXML
    private TableColumn<Patient, String> lastNameColumn, ageColumn, statutColumn;
    @FXML
    private TableColumn<Patient, String> sexeColumn, gradeColumn;
    // mot clé permettant de faire une reherche ciblée dans la TableView des patients
    @FXML
    private TextField motcle;
    
    //Pop up d'erreur si un patient n'est pas séléctionné avant de passer à la page d'ajout d'un examen
    private Stage dialogStage;

    // connexion à la base de données
    private ConnexionOracle maconnection = new ConnexionOracle();
    // créer une variable de la requête  
    private Statement stmt;   
    
    /**
     * Initializes the controller class.
     * Permet de lier les colonnes du tableView patient avec les données de la base de données.
     * Permet de remplir le tableView de patient à partie d'une liste de patient qui
     * correspond à la base de données.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        groupeColumn.setCellValueFactory(cellData -> cellData.getValue().groupeProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        sexeColumn.setCellValueFactory(cellData -> cellData.getValue().sexeProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        
        patientTable.setItems(recuperationPatients());
    }   
    
    /**
     * recuperationPatients() est appelé par l'initialiseur de la calsse.
     * Permet de remplire une liste de patient à partir de la base de données.
     */
    public ObservableList<Patient> recuperationPatients() {
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient;";

        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), result.getString("PRENOM"), result.getString("NOM"), result.getInt("AGE"), result.getBoolean("EXCLUS"), result.getBoolean("PROGRAMMEFINI"), result.getString("SEXE").charAt(0), result.getInt("GRADEGLIOMEACTUEL"));
                data.add(patient);
            }
            System.out.println("Liste remplie par la bdd");
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Liste non remplie par la bdd");
        }
        catch(NullPointerException e){
            System.out.println(e);
            System.out.println("Liste non remplie par la bdd");
        }
        data.add(new Patient(1,1,"uvjb","iugig",45,false,true,'H',2));
        return data;
    }
    
    /**
    * handleRechercher() est appelé lorsque le boutton rechercher est utilisé
    * et permet d'appliquer la recherche ciblée à l'aide d'un mot clé.
    */ 
    @FXML
    public void handleRechercher(){
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient;";
        
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), result.getString("PRENOM"), result.getString("NOM"), result.getInt("AGE"), result.getBoolean("EXCLUS"), result.getBoolean("PROGRAMMEFINI"), result.getString("SEXE").charAt(0), result.getInt("GRADEGLIOMEACTUEL"));
                //on garde le patient si on trouve le mot clé dans son nom ou son prénom
                if(patient.getFirstName().contains(motcle.getText()) || patient.getLastName().contains(motcle.getText())) {
                    data.add(patient);
                }
            }
            System.out.println("Liste remplie par la bdd");
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Liste non remplie par la bdd");
        }
        catch(NullPointerException e){
            System.out.println(e);
            System.out.println("Liste non remplie par la bdd");
        }
        
        //debut test
        patient = new Patient(1,1,"uvjb","iugig",45,false,true,'H',2);
        if(patient.getFirstName().contains(motcle.getText()) || patient.getLastName().contains(motcle.getText())) {
            data.add(patient);
        }
        //fin test
        
        patientTable.setItems(data);
    }
    
    /**
    * handleAjoutIrm() est appelé lorsque le boutton ajouter un examen est utilisé.
    * Permet de passer a la page AjoutExamen quand un patient est sélectionné sinon 
    * affiche un message d'erreur.
    *
    * @param event
    */ 
    @FXML
    public void handleAjoutExamen(ActionEvent event) throws IOException {
        Patient aPatient;
        aPatient = patientTable.getSelectionModel().getSelectedItem();
    
        if (aPatient != null) {
            Parent root = FXMLLoader.load(getClass().getResource("AjoutExamen.fxml"));
            Scene scene = (Scene) ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } 
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Attention !");
            alert.setHeaderText("Rien n'a été sélectionné !");
            alert.setContentText("Il faut sélectionner un patient");

            alert.showAndWait();
        }
    }
}
