/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irmApp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

/**
 *
 * @author Laure
 */
public class Patient {
    private final StringProperty id;
    private final StringProperty groupe;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty age;
    private final StringProperty sexe;
    private final StringProperty grade;
    private final StringProperty statut;
    
    public Patient(int idP, int groupeP, String firstNameP, String lastNameP,
            int ageP, boolean excluP, boolean finiP, char sexeP, int gradeP){
        id = new SimpleStringProperty(Integer.toString(idP));
        groupe = new SimpleStringProperty(Integer.toString(groupeP));
        firstName = new SimpleStringProperty(firstNameP);
        lastName = new SimpleStringProperty(lastNameP);
        age = new SimpleStringProperty(Integer.toString(ageP));
        sexe = new SimpleStringProperty(Character.toString(sexeP));
        grade = new SimpleStringProperty(Integer.toString(gradeP));
        if(finiP == true)
            statut = new SimpleStringProperty("Programme fini");
        else if(excluP == true)
            statut = new SimpleStringProperty("Exclu du programme");
        else statut = new SimpleStringProperty("Dans le programme");
    }
    
    public StringProperty idProperty() {
        return id;
    }
    public StringProperty groupeProperty() {
        return groupe;
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public StringProperty lastNameProperty() {
        return lastName;
    }
    public StringProperty ageProperty() {
        return age;
    }
    public StringProperty sexeProperty() {
        return sexe;
    }
    public StringProperty gradeProperty() {
        return grade;
    }
    public StringProperty statutProperty() {
        return statut;
    }
    
    
    public String getFirstName() {
        return firstName.get();
    }
    public String getLastName() {
        return lastName.get();
    }
    public int getId() {
        return Integer.parseInt(id.get());
    }
}
