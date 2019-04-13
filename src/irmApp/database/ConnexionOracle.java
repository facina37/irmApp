package irmApp.database;

import java.sql.*;


/**
 * 
 *
 * IRM Care App IHM Gphy
 * Version : 24/03/2019
 * @author Laure Baudoin & Marie Bogusz
 */
public class ConnexionOracle {
    

    String urlPilote = "oracle.jdbc.driver.OracleDriver";
    //voir le nom exact à gauche
    //String urlBase = "jdbc:oracle:thin:@192.168.254.3:1521:PFPBS";
    String urlBase = "jdbc:oracle:thin:@localhost:1521:ORCL";
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
