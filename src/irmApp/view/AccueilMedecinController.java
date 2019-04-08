package irmApp.view;

import irmApp.database.ConnexionOracle;
import irmApp.model.Examen;
import irmApp.model.Patient;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * La classe AccueilMedecinController permet de gerer la page d'accueil des médecins.
 * Permet d'afficher la liste des patients faisant partie de l'étude IRMCare.
 * Permet d'afficher la liste des examens à vérifier.
 * Permet d'acceder à la page d'ajout d'une previsite une fois un patient selectionné dans la liste.
 * Permet d'acceder à la page de vérification d'un examen une fois l'examen selectionné dans la liste.
 * 
 * version 30/03/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class AccueilMedecinController implements Initializable {

    //TableView de la liste des patients
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
    private TextField motclePatient;
    
    //TableView de la liste des examens
    @FXML
    private TableView<Examen> examenTable;
    @FXML
    private TableColumn<Examen, String> idExamColumn, dateColumn, prenomColumn, nomColumn; 
    // mot clé permettant de faire une reherche ciblée dans la TableView des examens
    @FXML
    private TextField motcleExamen;
    
    //Pop up d'erreur si un patient n'est pas séléctionné avant de passer à la page d'ajout d'un examen
    private Stage dialogStage;
    
    // connexion à la base de données
    private ConnexionOracle maconnection = new ConnexionOracle();
    //créer une variable de la requête
    private Statement stmt; 
    
    /**
     * Initializes the controller class.
     * Permet de lier les colonnes des tableView patient et examen avec les données de la base de données.
     * Permet de remplir les tableView patient et examen à partie d'une liste de patient qui
     * correspond à la base de données.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //partie patient
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        groupeColumn.setCellValueFactory(cellData -> cellData.getValue().groupeProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        sexeColumn.setCellValueFactory(cellData -> cellData.getValue().sexeProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        
        patientTable.setItems(recuperationPatients());
        
        //partie examens
        idExamColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        
        examenTable.setItems(recuperationExamens());
        //Adapattion de la taille des colonnes à la taille de la fenetre
        examenTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        idExamColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );
        dateColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        prenomColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35 );
        nomColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35 );        
        patientTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        idColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        groupeColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        firstNameColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35 );
        lastNameColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35 );
        ageColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        statutColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );
        sexeColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        gradeColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
    }    
    
    /**
    *  recuperationPatients() permet d'afficher la liste de tous les patients dans le TableView.
    */
    public ObservableList<Patient> recuperationPatients()
    {
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                System.out.println(result.getInt("GRADEGLIOMEACTUEL"));
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"), result.getInt("AGEPATIENT"), result.getBoolean("EXCLUS"), result.getBoolean("PROGRAMMEFINI"), result.getString("SEXEPATIENT").charAt(0), result.getInt("GRADEGLIOMEACTUEL"));
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
        return data;
    }
    
    /**
    *  handleRechercherPatients() permet d'afficher la liste de tous les patients recherchés à l'aide d'un mot clé.
    */
    @FXML
    public void handleRechercherPatients(){
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient";
        
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            System.out.println(requete);
            while(result.next()) {
                
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"), result.getInt("AGEPATIENT"), result.getBoolean("EXCLUS"), result.getBoolean("PROGRAMMEFINI"), result.getString("SEXEPATIENT").charAt(0), result.getInt("GRADEGLIOMEACTUEL"));
                //on garde le patient si on trouve le mot clé dans son nom ou son prénom
                if(patient.getFirstName().contains(motclePatient.getText()) || patient.getLastName().contains(motclePatient.getText())) {
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
        patientTable.setItems(data);
    }
    
    /**
    * handleAjoutVisite() est appelé lorsque le boutton ajouter une prévisite est utilisé.
    * Permet de passer a la page AjoutVisite quand un patient est sélectionné.
    *
    * @param event
    * @throws IOException 
    */
    @FXML
    private void handleAjoutVisite(ActionEvent event) throws IOException {
        Patient aPatient;
        aPatient = patientTable.getSelectionModel().getSelectedItem();
    
        if (aPatient != null) {
            if (aPatient.getStatut() == "Dans le programme"){
                Parent root = FXMLLoader.load(getClass().getResource("AjoutVisite.fxml"));
                Scene scene = (Scene) ((Node) event.getSource()).getScene();
                scene.setRoot(root);
            }  else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(dialogStage);
                alert.setTitle("Attention !");
                alert.setHeaderText("Patient incorrect !");
                alert.setContentText("Il faut sélectionner un patient qui fait partie du programme.");

                alert.showAndWait();
            }                  
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Attention !");
            alert.setHeaderText("Rien n'a été sélectionné !");
            alert.setContentText("Il faut sélectionner un patient.");

            alert.showAndWait();
        }       
    }    
    
    /**
     *  recuperationExamens() permet de récupère tous les examens à valider.
    */
    public ObservableList<Examen> recuperationExamens()
    {
        ObservableList<Examen> data = FXCollections.observableArrayList();
        Examen examen;
        String requete = "select * from Examen join patient on examen.idpatient = patient.idpatient where examen.gradeMedecin is null";
        System.out.println(requete);
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next())
            {
                examen = new Examen(result.getInt("IDEXAMEN"), result.getDate("DATEEXAM"), result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"));
                data.add(examen);
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

        return data;
    }
    
    /**
     *  handleRechercherExamens() permet d'afficher la liste de tous les examens recherchés.
    */
    @FXML
    public void handleRechercherExamens(){
        ObservableList<Examen> data = FXCollections.observableArrayList();
        Examen examen;
        String requete = "select * from Examen join patient on examen.idpatient = patient.idpatient where examen.gradeMedecin is null";
        
        try{
            
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                examen = new Examen(result.getInt("IDEXAMEN"), result.getDate("DATEEXAM"), result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"));
                
                //on garde le patient si on trouve le mot clé dans son nom ou son prénom
                if(examen.getFirstName().contains(motcleExamen.getText()) || examen.getLastName().contains(motcleExamen.getText()))
                {
                    data.add(examen);
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
        examenTable.setItems(data);
    }
    
    /**
     * handleVerifExam() permet de passer à la page Examen perso quand un patient est sélectionné.
     * 
     * @param event
    */
    @FXML
    public void handleVerifExam(ActionEvent event) throws IOException {
        Examen examen;
        examen = examenTable.getSelectionModel().getSelectedItem();
    
        if (examen != null) {
            Parent root = FXMLLoader.load(getClass().getResource("VerifExamen.fxml"));
            Scene scene = (Scene) ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Attention !");
            alert.setHeaderText("Rien n'a été sélectionné !");
            alert.setContentText("Il faut sélectionner un examen.");

            alert.showAndWait();
        }
    }
}
