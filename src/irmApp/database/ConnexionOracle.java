/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irmApp.database;

import java.sql.*;


/**
 *
 * @author Laure
 */
public class ConnexionOracle {
    
    String urlPilote = "oracle.jdbc.driver.OracleDriver";
    //voir le nom exact à gauche
    String urlBase = "jdbc:oracle:thin:@192.168.254.3:1521:PFPBS";
    Connection con;
    
    public ConnexionOracle(){
        
        //charger le pilote de connexion
        try{
            Class.forName(urlPilote);
            System.out.println("pilote chargé");
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
        
        //connection a la bdd
        try{
            //utilisateur = login + pwd
            con = DriverManager.getConnection(urlBase,"GROUPE_54","GROUPE_54");
            System.out.println("Connexion bdd reussie");
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public Connection ObtenirConnection(){
        return con;
    }
}
