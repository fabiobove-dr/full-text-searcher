package org.apache.lucene.demo;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe che implementa una pagina standard
 * dalla quale ereditano tutti i frame del progetto.
 * Prevede un header,footer, immagine di header e 
 * altri campi per la gestione della grafica.
 * @author Fabio
 *
 */
public class GUI extends JFrame{
		
		//Default Serial ID
		private static final long serialVersionUID = 1L;
		//Dimensioni degli elementi principali della pagina
		protected int larghezza, altezza, altezzaHeader, altezzaFooter;
		//Titolo della pagina, immagine dell'header
		protected String titolo,headerPath;
		//Vari componenti (JPanel, JLabel)
		protected JLabel header, footerText;
		protected JPanel headerContainer;
		protected JPanel footer;
		//Colori base scelti
		protected Color temaColorGray;
		protected Color temaColorBlue;
		protected Color temaColorOrange;
		protected Color temaColorLightBlue;
		protected Dimension screenDimension;
		
		/**
		 * Creazione della pagina principale;
		 * @param larghezza -> larghezza della pagina principale
		 * @param altezza -> altezza della pagina principale
		 * @param titolo -> titolo della pagina principale
		 * @param altezzaHeader -> altezza dell'Header della pagina principale
		 * @param altezzaFooter -> altezza del footer della pagina
		 * @param headerPath -> percorso dell'immagine dell'header
		 */
		public GUI(int larghezza, int altezza, String titolo, int altezzaHeader, int altezzaFooter, String headerPath)
		{
			/* Inizializzazione degli attributi della pagina principale */
			this.larghezza 		= larghezza;
			this.altezza   		= altezza;
			this.titolo			= titolo;
			this.altezzaHeader 	= altezzaHeader;
			this.altezzaFooter  = altezzaFooter;
			this.headerPath     = headerPath;
			
			/* Colori del "tema" */
			temaColorGray       = new Color(245,241,241);
			temaColorBlue  		= new Color(26,153,136);
			temaColorOrange  	= new Color(253,86,0);
			temaColorLightBlue  = new Color(127,203,205);
			
			/* Impostazioni della pagina principale */
			setLayout(null);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setResizable(false);
			setTitle(this.titolo);
			setBounds(0, 0,this.larghezza, this.altezza);
			setBackground(Color.white);
			
			/* Header della pagina */
			headerContainer = new JPanel();
			headerContainer.setLayout(null);
			headerContainer.setBackground(Color.white);
			
			/* Logo contenuto nell'Header */
			header = new JLabel(new ImageIcon(this.headerPath));
			header.setBounds(0, 0, this.larghezza, this.altezzaHeader);
			headerContainer.setBounds(0, 0, this.larghezza, this.altezzaHeader);
			headerContainer.add(header);
			
			/* Footer della pagina */
			footerText = new JLabel();
			footerText.setSize(this.larghezza, 40);
			footerText.setText("Fabio Bove, Progetto P.O.    .");
			footerText.setFont(new Font("Dialog",Font.ITALIC,10));
			footerText.setHorizontalAlignment(JTextField.RIGHT);
			footerText.setBorder(null);
			footerText.setForeground(Color.gray);
			footer = new JPanel();
			footer.add(footerText);
			footer.setLayout(null);
			footer.setBounds(0, this.altezza - 60, this.larghezza, this.altezzaFooter);
			footer.setBackground(Color.white);
			
			/*Aggiungo i vari componenti */
			add(headerContainer);
			add(footer);
			
			/*Imposto il frame al centro dello schermo*/
			this.screenDimension = getToolkit().getScreenSize();
			this.setLocation(screenDimension.width / 2 - this.getWidth() / 2, screenDimension.height / 2 - this.getHeight() / 2);
		}

		/** @return the altezzaFooter */
		public int getAltezzaFooter() {
			return altezzaFooter;
		}

		/** @param altezzaFooter the altezzaFooter to set */
		public void setAltezzaFooter(int altezzaFooter) {
			this.altezzaFooter = altezzaFooter;
		}
		
		/** @return the larghezza */
		public int getLarghezza() {
			return larghezza;
		}

		/** @param larghezza the larghezza to set */
		public void setLarghezza(int larghezza) {
			this.larghezza = larghezza;
		}

		/** @return the altezza */
		public int getAltezza() {
			return altezza;
		}

		/** @param altezza the altezza to set */
		public void setAltezza(int altezza) {
			this.altezza = altezza;
		}
		
		/** @return the altezzaHeader */
		public int getAltezzaHeader() {
			return altezzaHeader;
		}
		
		/** @param altezzaHeader the altezzaHeader to set */
		public void setAltezzaHeader(int altezzaHeader) {
			this.altezzaHeader = altezzaHeader;
		}
	 
}
