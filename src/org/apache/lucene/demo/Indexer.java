package org.apache.lucene.demo;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.SAXHandler.DblpDocument;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	static ReentrantLock counterLock = new ReentrantLock(true);
	long Tempo1;
	long Tempo2;
	double Tempo;
    private IndexWriter indexWriter = null;
	int Minuti;
	
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

 

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
        	final String INDEX_DIRECTORY = "index";
        	File f = new File(INDEX_DIRECTORY);
        	Directory indexDir = FSDirectory.open(f.toPath());
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer()); //Version.LUCENE_8_0_0
            indexWriter = new IndexWriter(indexDir, config); 
        }
        return indexWriter;
   }
    

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    
   public Document indexArticle(DblpDocument dblpDoc) throws IOException {
    	//Creazione di un nuo
        Document doc = new Document();

        /* Se uno dei campi non � presente c'� da risolvere il problema */
       if (dblpDoc.attributes.get("key") != null) { doc.add(new TextField("key", dblpDoc.attributes.get("key"), Field.Store.YES)); }
       if (dblpDoc.attributes.get("mdate") != null) { doc.add(new TextField("mdate", dblpDoc.attributes.get("mdate"), Field.Store.YES)); }
       
       doc.add(new TextField("title", 		dblpDoc.title, 		Field.Store.YES));	//comune
       
       String fullSearchableText="";
       if (!dblpDoc.pub_type.equals("null")){
    	   doc.add(new TextField("pub_type", 	dblpDoc.pub_type, Field.Store.YES));    	   
    	   doc.add(new TextField("author", 		dblpDoc.author, 	Field.Store.YES));
    	   doc.add(new TextField("year", 		dblpDoc.year,		Field.Store.YES));
    	   doc.add(new TextField("crossref", 	dblpDoc.crossref,	Field.Store.YES));
           fullSearchableText = dblpDoc.attributes.get("key")		+ " " +
    		   					dblpDoc.attributes.get("mdate")	+ " " +
    		   					dblpDoc.title 					+ " " +
          						dblpDoc.author 					+ " " +
								dblpDoc.year 					+ " " +
    		   					dblpDoc.crossref;
       }
       else{
       	   doc.add(new TextField("ven_type", 	dblpDoc.ven_type, 	Field.Store.YES));
    	   doc.add(new TextField("publisher", 	dblpDoc.publisher, 	Field.Store.YES));
           fullSearchableText = dblpDoc.attributes.get("key") 	+ " " +
    		   					dblpDoc.attributes.get("mdate")	+ " " +
    		   					dblpDoc.title 					+ " " +
    		   					dblpDoc.publisher;
       }
       
       doc.add(new TextField("publication", fullSearchableText, Field.Store.NO));
     
       synchronized(this){
       return(doc);
       }
    }

    public void rebuildIndexes() throws IOException {
    	// Cancellazione dell'indice precedente 
    	Tempo1=System.currentTimeMillis();
    	File dirIndex = new File("index");
    	String[] entries = dirIndex.list();
    	for(String s: entries){
    		File currentFile = new File(dirIndex.getPath(),s);
    		currentFile.delete();
    	}
		dirIndex.delete();	
		
        getIndexWriter(true);
        
        // Parsing
        SAXHandler saxParser = new SAXHandler();
        ArrayList <DblpDocument> publications = saxParser.parseDocument("dump/dblp.xml");
        
        int i=0;
        
        // Indicizzazione 
        while(i < saxParser.getCountElement()) {
        	IndexWriter writer = getIndexWriter(false);
        	IndexThread t1 = new IndexThread(indexArticle(publications.get(i)), writer);
        	t1.run(); i++;
        	
        	//Per utilizzare tutti i core del mac di Vale 
        	if(saxParser.getCountElement()-i > 4) {
        		IndexThread t2 = new IndexThread(indexArticle(publications.get(i)), writer);
        		t2.run(); i++;
        		IndexThread t3 = new IndexThread(indexArticle(publications.get(i)), writer);
        		t3.run(); i++;
        		IndexThread t4 = new IndexThread(indexArticle(publications.get(i)), writer);
        		t4.run(); i++;
        	}        	
        }
        
        // Chiusura dell'index writer a fine operazione.
        closeIndexWriter();
	
        // Calcolo del tempo di indicizzazione e parsing
		Tempo2 = System.currentTimeMillis();
	    Tempo  = Tempo2-Tempo1;
	    Tempo  = Tempo/1000;
	    Minuti = (int) (Tempo/60);
	    Tempo  = Tempo-(Minuti*60);
	    System.out.print("Tempo impiegato -> " + Minuti + ":");
	    System.out.print(Tempo);
	    System.out.println(" minuti");
     }
    
    public String checkValue(String value){
    	if(value.equals(null)) return "null";
    	else return value;
    }
}