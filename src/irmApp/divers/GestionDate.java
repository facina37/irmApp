/*Student App IHM Gphy
 * Version : 07/03/2019
 * Author : Bidois, Andreo, Baudoin
 */
package irmApp.divers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *Classe Gestion date,
 * Permet de gerer et d'utiliser les dates
 * @author group7.1
 */
public class GestionDate {
    
    /** format de date */
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /** transforme en format voulu */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);
    
    
    /**
     * transforme une date en string
     * @param date date en entrée de type LocalDate à formater en string
     * @return Srting la date reformatée en string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }
    
    /**
     * transforme un string en date
     * 
     * @param dateString la date en string a changer en LocalDate
     * @return localdate la date reformatée en LocalDate
     */
    public static LocalDate parse(String dateString) {
        
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
        
     /**
     * verifie que la date en string est une date valide
     *  
     * @param dateString date in string format 
     * @return true si date valide
     */
    public static boolean validDate(String dateString) {
        return GestionDate.parse(dateString) != null;
    }
    
}
