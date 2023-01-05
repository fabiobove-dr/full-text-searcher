package org.apache.lucene.demo;
 
import org.apache.lucene.demo.SearchEngine.matchingElement;
import org.apache.lucene.queryparser.classic.ParseException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.text.*;
import java.awt.*;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

 

/**
 * Interfaccia grafica.
 * L'utente attraverso questa pagina pu� laciare query e visualizzare il risultato.
 * Pu� inoltre modificare il modello di ranking utilizzato da Lucene.
 * @author Fabio, Simone, Valentino
 */
public class ProjectGUI extends GUI implements ActionListener{
	//Default Serial Version ID
	private static final long serialVersionUID = 1L;
	//Pannello contenente gli elementi per l'aggiunta 
	protected JPanel mainContainer;
	//Componenti per l'aggiunta
	protected JComboBox<String> rankingList;
	protected JTextField queryField;
	protected JButton startQueryBtn, buildIndexBtn, indietroBtn, avantiBtn;
	protected JFileChooser fileChooser;
	protected JTextPane messageField;
	protected JScrollPane messageScrollPane;
	protected JPanel resultPanel;
	
	protected SearchEngine se;
	protected int start = 0, end = 20;
    long Tempo1;
	long Tempo2;
	double Tempo;
	
	/**
	 * Costruttore del frame per l'aggiunta del prodotto.
	 */
	public ProjectGUI() throws IOException {
		/* Chiamo il costruttore */
		super(630, 630, "PROJ GAVI", 240, 0,"img/header.png");
		
		/* Gestisco la chiusura con un form di conferma */
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		/* Pannello per la gestione del file su cui
		 * sono presenti tutti i prodotti */
		mainContainer = new JPanel();
		mainContainer.setBackground(Color.white);
		mainContainer.setBounds(0, getAltezzaHeader(),getLarghezza(),getAltezza());
		mainContainer.setLayout(null);
		mainContainer.setFocusable(true);		
		
		/* Campo qeury utente */
		queryField = new JTextField(15);
		queryField.setBounds(0, 0, 500, 40);
		queryField.setText("Enter Query");
		queryField.setBackground(Color.white);
		queryField.setBorder(null);
		queryField.setForeground(temaColorOrange);
		queryField.setHorizontalAlignment(JTextField.CENTER);

		/* Bottone per esecuzione query */
		startQueryBtn= new JButton("start query");
		startQueryBtn.setBounds(500, 0, 130 , 40);
		startQueryBtn.setBackground(temaColorBlue);
		startQueryBtn.setForeground(Color.white);	
		startQueryBtn.setOpaque(true);
		startQueryBtn.setBorderPainted(false);
		startQueryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		/* Campo risultato query */
		resultPanel = new JPanel ();
		resultPanel.setBounds(0,0,630,400);
		resultPanel.setAlignmentX(CENTER_ALIGNMENT);
		resultPanel.setBackground(temaColorGray  );
		resultPanel.setBorder(null);
				
		/* Bottone scorri risultati query indietro*/
		indietroBtn= new JButton("<< Back");
		indietroBtn.setBounds(0, 282, 315, 40);
		indietroBtn.setBackground(Color.white);	
		indietroBtn.setForeground(temaColorBlue);
		indietroBtn.setOpaque(true);
		indietroBtn.setBorderPainted(false);
		indietroBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		/* Bottone scorri risultati query avanti*/
		avantiBtn= new JButton("Next >>");
		avantiBtn.setBounds(315, 282, 315, 40);
		avantiBtn.setBackground(Color.white);
		avantiBtn.setForeground(temaColorOrange);
		avantiBtn.setOpaque(true);
		avantiBtn.setBorderPainted(false);
		avantiBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		/* Campo selezione modello di ranking */ 
		String[] rankingTyper = {  "BM25Similarity", "BooleanSimilarity" };
		rankingList = new JComboBox<String>(rankingTyper);
		rankingList.setBounds(0, 322, 315, 40);
		rankingList.setSelectedIndex(0);
		rankingList.addActionListener(this);
		((JLabel)rankingList.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		/* Bottone per creare indice e parsare */
		buildIndexBtn = new JButton("build index");
		buildIndexBtn.setBounds(315, 322, 315, 40);
		buildIndexBtn.setBackground(temaColorBlue);
		buildIndexBtn.setForeground(Color.white);
		buildIndexBtn.setOpaque(true);
		buildIndexBtn.setBorderPainted(false);	
		buildIndexBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		/* Campo risultato query */
		messageField = new JTextPane ();
		messageField.setBounds(00, 110, 550, 150);
		messageField.setText("");
		messageField.setBackground(temaColorGray);
		messageField.setForeground(Color.gray);
		messageField.setBorder(null);
		messageField.setAlignmentX(CENTER_ALIGNMENT);
		messageField.setEditable(false);
		messageScrollPane = new JScrollPane (messageField);
		messageScrollPane.setBounds(0,41,625,235);
		messageScrollPane.setAlignmentX(CENTER_ALIGNMENT);
		messageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		messageField.setMargin(null);
		messageScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 1 , 0));
		
		KeyListener listener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {	if ( e.getKeyCode() == KeyEvent.VK_ENTER){	startQueryBtn.doClick();	}	}			 
			@Override	public void keyReleased(KeyEvent e){}	@Override	public void keyTyped(KeyEvent e) {}
		};
		queryField.addKeyListener(listener);
			 
		/* Aggiungo i componenti al pannello per la gestione del prodotto */
		mainContainer.add(startQueryBtn);
		mainContainer.add(indietroBtn);
		mainContainer.add(avantiBtn);
		mainContainer.add(queryField);
		//mainContainer.add(messageField);
		mainContainer.add(rankingList);
		mainContainer.add(buildIndexBtn);
		//mainContainer.add(resultPanel);
		mainContainer.add(messageScrollPane);
		add(mainContainer);
	    
		/* Aggiungo listener per gestione dell'effetto text-holder */
		queryField.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	if(queryField.getText().equals("Enter Query"))
		    		queryField.setText("");
		    }
		    public void focusLost(FocusEvent e) {
		    	if(queryField.getText().equals(""))
		    		queryField.setText("Enter Query");
		    }
		});
		
		/* Gestione dell'evento click sul bottone buildIndex */
		buildIndexBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	if(JOptionPane.showConfirmDialog(null,"Vuoi continuare?", "Scegli una opzione",
		    	JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		    	{	
		    		try {
			    		JOptionPane.showMessageDialog(getParent(),
						"Cancellazione del vecchio indice, Parsing del documento e Creazione dell'indice.", 
						"Avvio operazione.", JOptionPane.INFORMATION_MESSAGE  );
			    		
			    		//Costruzione del nuovo indice
			            Indexer  indexer = new Indexer();
			            indexer.rebuildIndexes(); 
			            
			            JOptionPane.showMessageDialog(getParent(),"Costruzione dell'indice avvenuta correttamente.", 
						"Operazione completata.",JOptionPane.INFORMATION_MESSAGE);
			    	} catch (Exception e1) {
			    		JOptionPane.showMessageDialog(getParent(),e1,
			    		"Errore durante la creazione dell'indice",JOptionPane.ERROR_MESSAGE );
			        }
		    	}
		    }
		});
		
		/**
		 * Gestione dell'evento click sul bottone Ricerca query 
		 */
		startQueryBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	start = 0; end = 20;
		    	// Se la query � valida
				if (checkValidity().equals("OK")){
					try {	
						//Inizializzazione del Search Enginer
			    		se = new SearchEngine();
			    		//Selezione del modello di Ranking
			    		se.rankingMethod = rankingList.getSelectedItem().toString();
						se.changeRankingMethod(rankingList.getSelectedItem().toString());
						
						//Inizializzazione del timer per il calcolo del tempo di esecuzione 
						Tempo1 = System.currentTimeMillis();
						//Esecuzione della query
						se.executeQuery(queryField.getText());
					
						//Calcolo del tempo di esecuzione e stampa del risultato
						Tempo2 = System.currentTimeMillis();
					    Tempo = (Tempo2 - Tempo1) / 1000; 
					    
					    //Messaggio di successo
					    JOptionPane.showMessageDialog( null, " Tempo di esecuzione: "+Tempo+"s.", " Query Eseguita ;)" , JOptionPane.PLAIN_MESSAGE );
					    
					    //Stampa dei risultati
					    se.printList();
					    stampaRisultati(start, end);
					    
					  //Messaggio di errore 
					} catch (IOException | ParseException | BadLocationException e1) { 
						JOptionPane.showMessageDialog( null, "La query inserita non � valida, consultare il manuale per la sintassi corretta.","Query non valida" , JOptionPane.ERROR_MESSAGE );
					}	
				} else JOptionPane.showMessageDialog( null, checkValidity(), "Dati non validi.", JOptionPane.ERROR_MESSAGE ); 
		    }
		});		
		
		/* Gestione dell'evento click sul bottone indietro */
		indietroBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
		    	System.out.println(messageField.getText().length());
		    	if ( !(messageField.getText().length() < 10) ){
		    		if ((start!=0)){
		    			start-=20;
		    			end-=20;
		    			try {	stampaRisultati(start, end);	} 
		    			catch (IOException | BadLocationException e1) {	e1.printStackTrace();	}
		    		}
		    	}
		    }
		});
		
		/* Gestione dell'evento click sul bottone avanti */
		avantiBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	System.out.println(messageField.getText().length());
		    	if ( !(messageField.getText().length() < 10) ){
		    		try{
						if (!((start+20) >= se.allDocList.size())){
							start+=20;
							end+=20;
							try {	stampaRisultati(start, end);	} 	catch (IOException | BadLocationException e1) {	e1.printStackTrace();	}
						}else{	JOptionPane.showMessageDialog(getParent(), "Questa � l'ultima pagina dei documenti visualizzati", "",
								JOptionPane.ERROR_MESSAGE);		}
					}catch (HeadlessException e1) {	e1.printStackTrace();	}
		    	}
		    }
		});	
		
		/* Gestisco la chiusura della pagina */
	    addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(getParent(), 
		            "Sicuro di volere uscire?", "termirare l'esecuzione?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION){
		        	return;
		        } else dispose();
		    }
		});
	}
	
	/** Metodo che ritorna se i campi di testo possono essere considerati validi per 
	 *  l'inserimento dei dati un nuovo prodotto, o se il codice inserito � gi� esistente.
	 *  @return il messaggio in caso di errore, "OK" per successo */
	protected String checkValidity(){
		if(queryField.getText().equals("")) return "La query inserita non � valida.";
		if(queryField.getText().equals("")) return "La query inserita non � valida.";
		return "OK";	
	}
	
	/** 
	 * Metodo per la stampa dei risultati della query dell'utente
	 * @throws IOException
	 * @throws BadLocationException 
	 */
	public void stampaRisultati(int start, int end) throws IOException, BadLocationException{ 
		StyleContext sc = new StyleContext();
	    final StyledDocument docx = new DefaultStyledDocument(sc);	    
	
	    for(int i = start; i < end && i < se.allDocList.size(); i++)
	    	if ( se.allDocList.get(i).pubDoc != -1 )
	    		//Se l'elemento da stampare � uno di quelli che ha metchato stampo il crossref 
	    		if(se.allDocList.get(i).venDoc != -1) stampaRisultato(se.getDocument(se.allDocList.get(i).pubDoc),docx, sc, true, false);
	    		else stampaRisultato(se.getDocument(se.allDocList.get(i).pubDoc),docx, sc, false, false);	
	    	else stampaRisultato(se.getDocument(se.allDocList.get(i).venDoc),docx, sc, false,false);	
	    
	     if(se.allDocList.size() == 0) 
	    		JOptionPane.showMessageDialog( null, "Nessun Risultato disponibile", "Nessun Risultato da mostrare.", JOptionPane.INFORMATION_MESSAGE );
	    
		messageField.setDocument(docx);
		messageField.setCaretPosition(0);
	}

	public void stampaRisultato( org.apache.lucene.document.Document el, StyledDocument docx, StyleContext sc, boolean printCrossref, boolean printReturn) throws IOException, BadLocationException{
		String queryResult	= "";
		String pubTitle 	= "";
		String subTitle 	= "";
		String linkReturn 	= "<< Torna ai risultati ";
		String labelLink    = "";
		String linkCross 	= "";
		String word[] = se.getWordToHighlightArray(); 
		
		final javax.swing.text.Style blueStyle = sc.addStyle("blueStyle", null);
	    final javax.swing.text.Style grayStyle = sc.addStyle("grayStyle", null);
	    final javax.swing.text.Style blueBoldStyle = sc.addStyle("blueBoldStyle", null);
	    final javax.swing.text.Style grayBoldStyle = sc.addStyle("grayBoldStyle", null);
		grayStyle.addAttribute(StyleConstants.Foreground, Color.black);
	    grayStyle.addAttribute(StyleConstants.FontSize, new Integer(13));
	    blueStyle.addAttribute(StyleConstants.Foreground, temaColorBlue);
	    blueStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
	    blueBoldStyle.addAttribute(StyleConstants.Foreground, temaColorBlue);
	    blueBoldStyle.addAttribute(StyleConstants.FontSize, new Integer(19));
	    blueBoldStyle.addAttribute(StyleConstants.Bold, true);
	    grayBoldStyle.addAttribute(StyleConstants.Foreground, Color.black);
	    grayBoldStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
	    grayBoldStyle.addAttribute(StyleConstants.Bold, true);
        javax.swing.text.Style style = null;
   
        style = grayStyle;
        
        if (el.get("pub_type") != null ){	            	 
        	queryResult = queryResult + "" +
        			"" 	+ el.get("pub_type")								+ "" + "," +
        			""	  	+ el.get("author")
        									.replaceAll(",", " ")
        									.trim()
        									.replaceAll(" ", " - ")
        									.replaceAll("%", " ")
        																		+ "" + "," +
        			
        			"" 	+ el.get("year")									+ "" + "\n\n";	
        	
        	for(matchingElement elem : se.getMatchingResultsList())
         		if(elem.venDoc != -1 && elem.pubDoc != -1)
         			if( el.get("key").equals(se.getDocument(elem.pubDoc).get("key")))
         				if (se.getDocument(elem.venDoc).get("title").length() < 50){
         					labelLink=se.getDocument(elem.venDoc).get("title");
         					linkCross = linkCross + "Vedi anche: " + se.getDocument(elem.venDoc).get("title") +"... " + "\n\n";}
         				else {linkCross = linkCross + "Vedi anche: " + (se.getDocument(elem.venDoc).get("title")).substring(0,50)+"... " + "\n\n";
         					labelLink=se.getDocument(elem.venDoc).get("title");
         				}
        }
        else queryResult = queryResult +
        			""  + el.get("ven_type")  + "" + "," +
        			""  + el.get("publisher") + "" + "\n\n";  
        
        pubTitle = pubTitle + el.get("title")	+ "" + "\n";
        subTitle = subTitle + 
        				"#" 	+ el.get("key")	+ "" + "\n" + 
        				"" 	+ el.get("mdate") + "" + ",";
        
        /*Inserimento titolo*/
        stampaCampoRisultato(pubTitle,docx, blueStyle, blueBoldStyle, word, queryResult);
        
        
        /*Inserimento label crossref */
        if(printCrossref){
	        JLabel l = new JLabel(linkCross);
	        l.setBorder(new EmptyBorder(0,0,0,0));
	        l.setToolTipText(labelLink);
	        l.setFont(messageField.getFont());
	        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        l.setForeground(temaColorOrange);
	        l.addMouseListener(new MouseAdapter(){
	            public void mouseClicked(MouseEvent me){
	                try{
	                	final StyledDocument docx = new DefaultStyledDocument(sc);
	             	
		             	for(matchingElement elem : se.getMatchingResultsList())
		             		if(elem.venDoc != -1 && elem.pubDoc != -1)
		             			if( el.get("key").equals(se.getDocument(elem.pubDoc).get("key")))
		             				stampaRisultato(se.getDocument(elem.venDoc),docx, sc, false, true);
		             	
		 				messageField.setDocument(docx);
		 			} catch (IOException | BadLocationException e) {
		 				e.printStackTrace();
		 			}
	            }
	         });
	        
	        linkCross = "";

	        messageField.setDocument(docx);
	        messageField.setCaretPosition(docx.getLength());
	        messageField.insertComponent(l);
	        
	        docx.insertString(docx.getLength(), "\n", style);
        }
	    
        /*Inserimento label return */
	    if(printReturn){
	    	JLabel l = new JLabel(linkReturn);
		    l.setBorder(new EmptyBorder(0,0,0,0));
		    l.setFont(messageField.getFont());
		    l.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    l.setForeground(temaColorOrange);
		    l.addMouseListener(new MouseAdapter(){
		    	public void mouseClicked(MouseEvent me){
		    		try{ stampaRisultati(start, end); } 
		    		catch (IOException | BadLocationException e) { e.printStackTrace(); }
		        }
		    });
	        
	        messageField.setDocument(docx);
	        messageField.setCaretPosition(docx.getLength());
	        messageField.insertComponent(l);
	        
	        docx.insertString(docx.getLength(), "\n", style);
        }
        
        /*Inserimento Subtitle*/
        stampaCampoRisultato(subTitle,docx, grayStyle, grayBoldStyle, word, queryResult);
       
        /*Inserimento ultime informazioni*/
        stampaCampoRisultato(queryResult,docx, grayStyle, grayBoldStyle, word, queryResult);
	}   
	
    /**
     * @throws BadLocationException 
     */
    public void stampaCampoRisultato(String ms, StyledDocument docx, javax.swing.text.Style style, javax.swing.text.Style boldStyle, String word[], String queryResult) throws BadLocationException{
        Map<Integer, String> substringIndices = new TreeMap<Integer, String>();
        ArrayList<String> substrings = new ArrayList<String>();
        
        for(int i=0; i < word.length; i++)
        	if(word[i].indexOf('*') >= 0)
        		if(word[i].indexOf('*') == 0) substrings.add(word[i].substring(1, word[i].length()));
        		else{
        			substrings.add(word[i].substring(0, word[i].indexOf('*')));
        			substrings.add(word[i].substring(word[i].indexOf('*') + 1, word[i].length()));
        		}
        	else substrings.add(word[i]);

        for (String substring : substrings) {
          int index = ms.indexOf(substring);
          if (index != -1) substringIndices.put(index, substring);
          
        }
        
        int closerIndex = 0;
        for (Integer index : substringIndices.keySet()) {
          String s = substringIndices.get(index);
          if(index >= closerIndex){
        	  docx.insertString(docx.getLength(), ms.substring(closerIndex, index), style);
      	  	  docx.insertString(docx.getLength(), ms.substring(index, index+s.length()), boldStyle);
      	  	  closerIndex = index + s.length();
          }
        }
        
        docx.insertString(docx.getLength(), ms.substring(closerIndex, ms.length()), style);
    }

	/** Gestione dell'evento generato durante la selezione di
	 * una voce in uno dei combobox per impostare il tipo di ranking. */
	@Override
	public void actionPerformed(ActionEvent e) {
		/* Selezione tipo di ranking */
		if(e.getSource() == rankingList){
			String rankingType = (String)this.rankingList.getSelectedItem();
			rankingList.setSelectedItem(rankingType);
		}
	}
}