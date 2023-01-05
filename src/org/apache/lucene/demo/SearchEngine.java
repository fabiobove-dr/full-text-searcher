

package org.apache.lucene.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Search Engine
 * @author Fabio, Valentino, Simone
 */

public class SearchEngine {
	    private IndexSearcher searcher = null;
	    private QueryParser parser = null;
	    protected ScoreDoc[] hits, searchedDocuments;
	    protected String[] wordToHighlightArray;
	    protected ArrayList<matchingElement> allDocList = new ArrayList<matchingElement>();
	    protected ArrayList<crosKey> connectedDocumentsList = new ArrayList<crosKey>();
	    protected ArrayList<ScoreDoc> venueList = new ArrayList<ScoreDoc>();
		//Struttura che contine publicazioni che metchano con le venue e la somma del loro score
		protected ArrayList<matchingElement> matchingResultList = new ArrayList<matchingElement>();
		//Modello di ranking attivo 
	    protected String rankingMethod;
	    class crosKey{
			String crossref;
			String key;
		}
	    class matchingElement{
			float score;
	    	int venDoc;
	    	int pubDoc;
	    }
	    
	    /** 
	     * Creates a new instance of SearchEngine
	     */
	    public SearchEngine() throws IOException {
	    	final String INDEX_DIRECTORY = "index";
	    	File f = new File(INDEX_DIRECTORY);
	    	Directory indexDir = FSDirectory.open(f.toPath());
	        searcher = new IndexSearcher(DirectoryReader.open(indexDir));
	        parser = new QueryParser("publication", new StandardAnalyzer());
	    }
	    
	    /** 
	     * Cambia il modello di ranking utilizzato da Lucene.
	     * Modelli disponibili su Lucene:
	     * BM25Similarity, BooleanSimilarity, MultiSimilarity, PerFieldSimilarityWrapper, SimilarityBase, TFIDFSimilarity.
	     * @param ranking_method -> modello di ranking da impostare.
	     */
	    public void changeRankingMethod(String ranking_method){
	    	if(ranking_method == "BM25Similarity") 		searcher.setSimilarity(new BM25Similarity());
	    	if(ranking_method == "BooleanSimilarity")	searcher.setSimilarity(new BooleanSimilarity());
	    }
	    
	    /** 
	     * Ritorna il modello di ranking attualmente in uso.
	     * @return -> modello di ranking.
	     */
	    @SuppressWarnings("static-access")
		public String getCurrentRankingModel(){
	    	return searcher.getDefaultSimilarity().toString();
	    }
	    
	    /**
	     * Esegue la quary e ritorna i primi n risultati.
	     * @param queryString
	     * @param n -> risultati da mostrare 
	     * @return -> il risultato della query
	     * @throws IOException
	     * @throws ParseException
	     */
	    public TopDocs performSearch(String queryString, int numberOfResults)
	    throws IOException, ParseException {
	        Query query = parser.parse(queryString);
	       // System.out.println(query);
	        return searcher.search(query, numberOfResults);
	    }
	
	    /**
	     * 
	     * @param docId
	     * @return
	     * @throws IOException
	     */
	    public Document getDocument(int docId)
	    throws IOException {
	        return searcher.doc(docId);
	    }
	    
	
	    /**
	     * Riscrive la query dell'utente in una sintassi eseguibie da Lucene e 
	     * compatibile con i campi della struttura dati con il quale è stato parsato il documento xml.
	     * @param userQuery
	     * @return rewritedQuery -> query con sintassi eseguibile da Lucene
	     */
	    public String rewriteQuery(String userQuery){
			
	    	
	    	String rewritedQuery = userQuery;
			 
			 /*venue*/
			if (userQuery.indexOf("venue.") !=-1)
				rewritedQuery = userQuery.replaceAll("venue.", "(ven_type:book OR ven_type:proceedings) AND ");
			if (userQuery.indexOf("venue:") !=-1)
				rewritedQuery = userQuery.replaceAll("venue:", "(ven_type:book OR ven_type:proceedings) AND ");
			
			/*book*/
			if (userQuery.indexOf("book.") !=-1)
				rewritedQuery = userQuery.replaceAll("book.", "ven_type:book AND ");
			if (userQuery.indexOf("book:") !=-1)
				rewritedQuery = userQuery.replaceAll("book:", "ven_type:book AND ");
			
			/*proceedings*/
			if (userQuery.indexOf("proceedings.") !=-1)
				rewritedQuery = userQuery.replaceAll("proceedings.", "ven_type:proceedings AND ");
			if (userQuery.indexOf("proceedings:") !=-1)
				rewritedQuery = userQuery.replaceAll("proceedings:", "ven_type:proceedings AND ");
			 
			/*publication*/
			if (userQuery.indexOf("publication.") !=-1)
				rewritedQuery = userQuery.replaceAll("publication.", "(pub_type: article OR pub_type: inproceedings OR pub_type: phdthesis OR pub_type: mastersthesis OR pub_type: incollection) AND ");
			if (userQuery.indexOf("publication:") !=-1)
				rewritedQuery = userQuery.replaceAll("publication:", "(pub_type: article OR pub_type: inproceedings OR pub_type: phdthesis OR pub_type: mastersthesis OR pub_type: incollection) AND ");
			
			/*article*/
			if (userQuery.indexOf("article.") !=-1)
				rewritedQuery = userQuery.replaceAll("article.", "pub_type: article AND ");
			if (userQuery.indexOf("article:") !=-1)
				rewritedQuery = userQuery.replaceAll("article:", "pub_type: article AND ");
			
			/*inproceedings*/
			if (userQuery.indexOf("inproceedings.") !=-1)
				rewritedQuery = userQuery.replaceAll("inproceedings.", "pub_type: inproceedings AND ");
			if (userQuery.indexOf("inproceedings:") !=-1)
				rewritedQuery = userQuery.replaceAll("inproceedings:", "pub_type: inproceedings AND ");
			
			/*mastersthesis*/
			if (userQuery.indexOf("mastersthesis.") !=-1)
				rewritedQuery = userQuery.replaceAll("mastersthesis.", "pub_type: mastersthesis AND ");
			if (userQuery.indexOf("mastersthesis:") !=-1)
				rewritedQuery = userQuery.replaceAll("mastersthesis:", "pub_type: mastersthesis AND ");
			
			/*phdthesis*/
			if (userQuery.indexOf("phdthesis.") !=-1)
				rewritedQuery = userQuery.replaceAll("phdthesis.", "pub_type: phdthesis AND ");
			if (userQuery.indexOf("phdthesis:") !=-1)
				rewritedQuery = userQuery.replaceAll("phdthesis:", "pub_type: phdthesis AND ");
			
			/*incollection*/
			if (userQuery.indexOf("incollection.") !=-1)
				rewritedQuery = userQuery.replaceAll("incollection.", "pub_type: incollection AND ");
			if (userQuery.indexOf("incollection:") !=-1)
				rewritedQuery = userQuery.replaceAll("incollection.", "pub_type: incollection AND ");
			
			System.out.println("Rewrited query-> " + rewritedQuery);
	    	return rewritedQuery;
	    }
	    
	    
	    /**
	     * Metodo che splitta la query inserita da utente nel caso vi sia una ricerca 
	     * sia sulle pubblicazioni che sulle venue.
	     * @param query
	     * @return
	     */
	    protected String[] splittedQuery(String query){

	    	String q = query; 
	    	String[] ret = new String[3];
	    	ret[0] = ""; 
	    	ret[1] = "";
	    	
	    	
	    	q = q.replaceAll("  ", " ");	
	    	q = q.replaceAll("   ", " ");
	    	q = q.replaceAll( " :", ":"); 	
	    	q = q.replaceAll( ": ", ":");
	    	ret[2] = q.replaceAll( " and ", " @ ").replaceAll( "and ", " @ ").replaceAll( " and", " @ ").replaceAll( " @ ", " ");
	    	q = q.toLowerCase();
	    	q = q.replaceAll( " and ", " @ "); 	
	    	q = q.replaceAll( "and ", " @ "); 	
	    	q = q.replaceAll( " and", " @ ");
	    	
	    	int indice;
	    	
	    	
	    	while( q.indexOf("venue.") !=-1 ){  		
	    		indice = q.length();	
	        	ret[1] = ret[1] + q.substring( q.indexOf("venue."), indice); 
	    		q = q.replace( ret[1], "");
	    	}
	    	
	    	while( q.indexOf("venue:") !=-1 ){  		
	    		indice = q.length();	
	        	ret[1] = ret[1] + q.substring( q.indexOf("venue:"), indice); 
	    		q = q.replace( ret[1], "");
	    	}
	    	
	    	while( q.indexOf("proceedings:") !=-1 ){	
	    		indice = q.length();	
	    		ret[1] = ret[1] + q.substring( q.indexOf("proceedings:"), indice);
	    		q = q.replace( ret[1], "");
	    	}
	    	
	    	while( q.indexOf("book:") !=-1 ){	
	    		indice = q.length();	
	    		ret[1] = ret[1] + q.substring( q.indexOf("book:"), indice);
	    		q = q.replace( ret[1], "");
	    	}
	    		    	
	    	ret[0] = (ret[0]+ q ).trim();
	    	ret[0] = ret[0].replaceAll("  ", " ");
	    	ret[0] = ret[0].replaceAll("@ @", "and");
	    	ret[0] = ret[0].replaceAll("@", "and");
	    	
	    	if( ret[0].indexOf("and") == 0 ) 
	    		ret[0] = ret[0].replaceFirst("and ", "");
	    	
	    	if( ret[0].indexOf("and") != 1 )
	    		if( ret[0].lastIndexOf("and")==(ret[0].length()-3) ) 
	    			ret[0] = (ret[0].substring( 0, ret[0].length()-3)).trim();
	    		
	    	ret[1] = ret[1].trim();
	    	ret[1] = (ret[1].replaceAll("  ", " ")).replaceAll(" ", " and ");
	    	
	    	if (ret[0].length() == 0){	
	    		ret[0] = query;	
	    		ret[1]= "";	
	    	}
	    	
	    	//ret[2] = ret[0].replaceAll(" and ", " ")+" "+ret[1].replaceAll(" and ", " ");
	    	
	    	return ret;
	    }
	    
	    
	    /** Metodo per l'esecuzione di una ricerca
		 * @param p il nuovo prodottod a aggiungere 
		 * @throws IOException 
		 * @throws ParseException */
		protected void startQuery(String query, int numberOfResults) throws IOException, ParseException{
			String userQuery = rewriteQuery(query); 
			TopDocs topDocs  = performSearch(userQuery, numberOfResults);
			
		    this.hits = new ScoreDoc[topDocs.scoreDocs.length];
		    this.hits = topDocs.scoreDocs.clone();	   
		    
		}
		
		
		/**
		 * Metodo che crea un vettore in cui memorizzare gli score dell'ultima query eseguita 
		 * dall'utente per evitare che vengano sovrascritte dalla query successiva
		 */
		protected void createScoreBackup(){
			this.searchedDocuments = new ScoreDoc[this.hits.length];
			this.searchedDocuments = this.hits.clone();
		}
		
		/**
		 * Metodo che crea un vettore in cui salvare il collegamento (crossref)
		 * fra le publicazioni trovate dalla query dell'utente e le venue ad esse collegate
		 * @throws IOException
		 */
		protected void createCrossreffedPubList() throws IOException{		
			crosKey currentDoc;
			for (int i = 0; i < this.hits.length; i++) 
	            if ( getDocument(this.hits[i].doc).get("crossref") != null )
	            	if ( getDocument(this.hits[i].doc).get("crossref").indexOf("/") !=- 1 ) {
	            		currentDoc = new crosKey();
	            		currentDoc.crossref = getDocument(this.hits[i].doc).get("crossref");
	            		currentDoc.key = getDocument(this.hits[i].doc).get("key");
	            		connectedDocumentsList.add(currentDoc);
	            	}          
		}//key:journals/thipeac/VandeputteE11a and proceedings:journals/thipeac/2011-4
		
		/**
		 * Metodo per la creazione di una struttura che contiene le publicazione e le venue che metchano tra di loro 
		 * e per ogni match salva lo score relativo, ottenuto dalla somma dello score della publicazione e della venue
		 * @param pubDoc
		 * @param venDoc
		 * @return
		 */
		public matchingElement createNewMatchingElement(ScoreDoc pubDoc, ScoreDoc venDoc){
			matchingElement newMetchingElement = new matchingElement();
			newMetchingElement.score  = pubDoc.score + venDoc.score; 
			newMetchingElement.pubDoc = pubDoc.doc; 
			newMetchingElement.venDoc = venDoc.doc; 
			return newMetchingElement;
		}
		
		/**
		 * Ritorna il numero di elementi contenuti in matchingResultList
		 * @return matchingResultList.size();
		 */
		public int getMatchingResultsNumber(){
			return this.matchingResultList.size();
		}
		
		/**
		 * Metodo che riempe una struttura contente le publicazioni che metchano con le venue 
		 * ritornate dalla query eseguite dall'utente, la struttura contiene anche la somma dello score di 
		 * due elementi che metchano
		 * @return matchingResultList
		 * @throws IOException
		 */
		public void findMetchingResults() throws IOException{
			//Booleano che indica se l'i-esima publicazione metcha con la k-esima venue
			boolean isMathcing = false;
			//Pubblicazioni e venue che soddisfano la query dell'utente
			Document pubDoc,venDoc = null;
			int k = 0;
			for(int i = 0; i < searchedDocuments.length; i++){
				pubDoc = getDocument(searchedDocuments[i].doc);
				if ( pubDoc.get("crossref") != null ){ 
					if ( pubDoc.get("crossref").indexOf("/") !=- 1 ) {
						for( k = 0; k < hits.length; k++){
						    venDoc = getDocument(hits[k].doc);
						    //Se la publicazione attuale fa match con la venue 
						    //Aggiungo la venue alla struttura per salvare gli elementi che fanno match 
						    //Interrompo lo scorrimento delle venue  
							if ( pubDoc.get("crossref").equals(venDoc.get("key")) ){
								isMathcing = true;	
								//Rimuoviamo gli elementi che hanni metchato dalle altre strutture per non avere doppioni in stampa
								removeDoubleElements(this.hits,k); 
								break;
							}
						}
						//Se la pubblicazione ha fatto match con una venue la
						//Aggiungo alla matchingResultList
						if(isMathcing){
							//Rimuoviamo gli elementi che hanni metchato dalle altre strutture per non avere doppioni in stampa
							removeDoubleElements(this.searchedDocuments, i);
							matchingResultList.add(createNewMatchingElement(searchedDocuments[i], hits[k]));
							isMathcing = false;	
						}
					}
				}
			}
		}
		
		/**
		 * Rimuove i doppioni ovvero le venue e le pubblicazioni che hanno metchato in seguito 
		 * ad una query dell'utente e sono già state aggiunte alla corrispettiva struttura
		 * in questo modo non vengono ristampate 
		 * @param elems
		 * @param elPos
		 */
		public void removeDoubleElements(ScoreDoc[] elems, int elPos ){
			ArrayList<ScoreDoc> temp = new ArrayList<ScoreDoc>(Arrays.asList(elems));
			temp.remove(elPos);
			elems = temp.toArray(new ScoreDoc[temp.size()]);
		}
		
		/**
		 * Metodo che ritorna la struttura matchingResultList
		 * @return matchingResultList
		 */
		public ArrayList<matchingElement> getMatchingResultsList(){
			return this.matchingResultList;
		}
		
		/**
		 * Metodo che ritorna la struttura searchedDocuments
		 * @return matchingResultList
		 */
		public ScoreDoc[] getSearchedDocuments(){
			return this.searchedDocuments;
		}
		
		/**
		 * Metodo che ritorna la struttura hits
		 * @return hits
		 */
		public ScoreDoc[] getHits(){
			return this.hits;
		}
		
		/**
		 * Metodo per l'ordinamento in base allo score 
		 * della struttura con i risultati matchanti
		 */
		public void ordinaMatchingResults(){
			boolean control = true; 
			matchingElement ap;							
			while(control){
				boolean c = false;
				for(int i = 0; i < getMatchingResultsNumber(); i++){
					if (i + 1 == getMatchingResultsNumber() ) break;	
					if (matchingResultList.get(i).score < matchingResultList.get(i+1).score){
						ap = matchingResultList.get(i);
						matchingResultList.set(i, matchingResultList.get(i+1));
						matchingResultList.set(i + 1, ap);
						c = true;
					}
				}
				if (!c) control = false; 
			}
		}
		
		/**
		 * Si occupa di gestire l'esecuzione della query:
		 * riscrittura in una sintassi compatibile
		 * selezione delle stringhe da evidenziare 
		 * e creazione della lista finale contenente i risultati ordinati in ordine di pertinenza 
		 * @param userQuery
		 * @throws IOException
		 * @throws ParseException
		 */
		public void executeQuery(String userQuery) throws IOException, ParseException{
			//Splitting della query nel caso vi sia da effettuare
			//Query combinata sia sulle venue che sulle publications
			String[] mainQuery = new String[2];		
			mainQuery = splittedQuery(userQuery); 
			String pubQuery = mainQuery[0];			
			String venQuery = mainQuery[1];
			wordToHighlightArray = mainQuery[2].split(" ");
			
			for (int i = 0; i < wordToHighlightArray.length; i++)
			 wordToHighlightArray[i] =  wordToHighlightArray[i].substring(wordToHighlightArray[i].indexOf(":")+1, wordToHighlightArray[i].length());
			
			int numberOfResults = 100;
			//Esecuzione della query sulle publicazioni
			startQuery(pubQuery, numberOfResults);															
			
			//Creazione di una vettore copia contente gli score della query sulle publicazioni
			//In questo modo non verranno persi i valori una volta eseguita anche la query sulle venue
		 	createScoreBackup();
		 	//Crezione di un vettore che contiene le publications ritornate dalla query dell'utente 
		 	//Che hanno un crossref che li lega ad una venue
			createCrossreffedPubList();

			//Se la query contiene una ricerca da effettuare sulle venue			
			if ( venQuery.length() != 0 ){
				//Esecuzione della query sulle venue
				startQuery(venQuery, numberOfResults);
				//Costruzione ed ordinamento della struttura dati contenete i risultati che metchano
				findMetchingResults();
				ordinaMatchingResults();
			}
		}
		
		/**
		 * Ritorna un vettore contenente le stringhe da evidenziare nei risultati mostrati all'utetente
		 * @return
		 */
		public String[] getWordToHighlightArray(){
			return wordToHighlightArray;
		}
		
		/**
		 * Crea una struttura contenente tutti i risultati che sarà utilizzata per la stampa
		 * @throws IOException
		 */
		public void printList() throws IOException{					        	
			ArrayList<matchingElement> docList = getMatchingResultsList();
			
			matchingElement tempDoc = null;
	    	ScoreDoc[] tempArray = getSearchedDocuments();
	    	for(int i = 0; i < tempArray.length; i++){
	    		tempDoc = new matchingElement();
	    		tempDoc.pubDoc = tempArray[i].doc;	
	    		tempDoc.venDoc = -1;
	    		docList.add(tempDoc);
	    	}		    
	    
	    	tempArray = getHits();	
	    	tempDoc = null; 
	    	for(int i = 0; i < tempArray.length; i++){
	    		tempDoc = new matchingElement();
	    		if ( getDocument(tempArray[i].doc).get("pub_type") == null	){
	    			tempDoc.venDoc = tempArray[i].doc; 
	    			tempDoc.pubDoc = -1;
	    		}else {
	    			tempDoc.pubDoc = tempArray[i].doc; 
	    			tempDoc.venDoc =-1;
	    		}	
	    		docList.add(tempDoc);				
	    	}
	    	
	    	this.allDocList = docList;
		}

}