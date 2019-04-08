/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irmApp.model;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Laure
 */
public class Examen {
    private final StringProperty id;
    private final StringProperty date;
    private final StringProperty firstName;
    private final StringProperty lastName;
    
    public Examen(int idP, Date dateP, String firstNameP, String lastNameP){
        id = new SimpleStringProperty(Integer.toString(idP));
        //dateP doit être sous la forme 
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        date = new SimpleStringProperty(df.format(dateP));
        firstName = new SimpleStringProperty(firstNameP);
        lastName = new SimpleStringProperty(lastNameP);
    }
    
    public StringProperty idProperty() {
        return id;
    }
    public StringProperty dateProperty() {
        return date;
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public StringProperty lastNameProperty() {
        return lastName;
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
