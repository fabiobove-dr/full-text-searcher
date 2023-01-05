/*
 * Main.java
 */

package org.apache.lucene.demo;

import java.io.IOException;

/**
 * Full Text Search System, per il dump di DBLP.
 * Gestione Avanzata dell'informazione 2019/2020.
 * @author Fabio, Valentino, Simone
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {}
    
    /** @param args the command line arguments 
     * @throws IOException */
    public static void main(String[] args) throws IOException {
    	/* Inizializzazione e visualizzazione dell'interfaccia grafica */
    	ProjectGUI userGUI = new ProjectGUI();
    	userGUI.setVisible(true);
    }
    
}
