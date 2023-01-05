package org.apache.lucene.demo;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

public class IndexThread implements Runnable{ 

    private Document resource = null;
    private IndexWriter writer;

    public IndexThread(Document i, IndexWriter writer) { 
    	this.resource = i;
    	this.writer  = writer;
    } 
    @Override
    public void run() {
        try {
			writer.addDocument(resource);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

