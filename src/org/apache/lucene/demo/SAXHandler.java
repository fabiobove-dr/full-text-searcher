package org.apache.lucene.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXHandler extends DefaultHandler {
		protected int countElement;
		protected ArrayList<DblpDocument> dblpDoc = new ArrayList<DblpDocument>();
		protected DblpDocument currentDblpDocument;
		protected boolean isTitle, isPublisher, isAuthor, isYear, isCrossref;
		int aut = 0, cont = 0;
		
		class DblpDocument{
			public String  pub_type 	= "null";
	        public String  title		= "null";
			public String  author		= "";
	        public String  year			= "null";
	        public String  crossref		= "null";
	        public String  ven_type 	= "null";
			public String  publisher	= "null";	        
	        public Map<String, String> attributes = new HashMap<>();
	    }
		
	    public SAXHandler() { 
	    	// TODO Auto-generated constructor stub
		}

		@Override
	    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	        if(qName.equals("article")  
	         | qName.equals("incollection") 
	         | qName.equals("inproceedings") 		 
	         | qName.equals("phdthesis")  
	         | qName.equals("mastersthesis")) {
	        	currentDblpDocument = new DblpDocument();
	        	currentDblpDocument.pub_type = qName.toString();
	        	countElement ++;
	        }
	        
	        if(qName.equals("book")  | qName.equals("proceedings")) {
	    	    currentDblpDocument = new DblpDocument();
	        	currentDblpDocument.ven_type = qName.toString();
	    	    countElement ++;
	    	}
	        
	        //child
	    	if(qName.equals("title")) 			 isTitle 		= true; 	
	    	else if(qName.equals("author"))  	 isAuthor 		= true; 
  		 	else if(qName.equals("year"))  	 	 isYear 		= true; 
  		 	else if(qName.equals("publisher")) 	 isPublisher 	= true; 
  		    else if(qName.equals("crossref")) 	 isCrossref 	= true; 
	    		        
	        //il modo in cui vengono scritti gli attributi uguale sia per venue che per publication
	        if(atts.getLength() > 0)
	            for(int i = 0; i < atts.getLength(); i++)  
	            	currentDblpDocument.attributes.put(atts.getQName(i),atts.getValue(i)); 
	    }

	    @Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	    	if(qName.equals("article")  
	    	 | qName.equals("incollection") 
	    	 | qName.equals("inproceedings") 		 
	    	 | qName.equals("phdthesis")  
	    	 | qName.equals("mastersthesis")
	    	 | qName.equals("book")  
	   	     | qName.equals("proceedings")) {
	    		dblpDoc.add(currentDblpDocument);
	   	    }
	    	
	    	if (isTitle) 						isTitle 	= false; 
	    	else if(qName.equals("publisher")) 	isPublisher = false; 
	    	else if (isAuthor)	 				isAuthor 	= false; 
	    	else if (isYear) 	 				isYear 		= false; 
	    	else if (isCrossref)  				isCrossref 	= false; 
	    }

	    @Override
	    public void characters(char[] ch, int start, int length) throws SAXException {
	        
	    	if (isTitle){			
	    		String s = String.copyValueOf(ch);
	    		currentDblpDocument.title =	s.substring(start, (start+length) ); 	
	    	}
	        else if(isPublisher){	
	        	String s = String.copyValueOf(ch);
	        	currentDblpDocument.publisher =	s.substring(start, (start+length) ); 
	        }
	        else if (isAuthor){		
	        	String s = String.copyValueOf(ch);
	        	currentDblpDocument.author += s.substring(start, (start+length)).replaceAll(" ", "%")+",";
	        }
	        else if (isYear){		
	        	String s = String.copyValueOf(ch);
	        	currentDblpDocument.year 		= s.substring(start, (start+length) ); 
	        }
	        else if (isCrossref){	
	        	String s = String.copyValueOf(ch);
	        	currentDblpDocument.crossref 	= s.substring(start, (start+length) ); 
	        }
	    }

	    @Override
	    public void endDocument() throws SAXException {
	    	System.out.println(countElement+" "+aut);
	    }
	    
	    public ArrayList<DblpDocument> parseDocument(String is){
	    	// TODO Auto-generated method stub
	    	SAXParserFactory spf = SAXParserFactory.newInstance();
	    	try{
	    		SAXParser parser = spf.newSAXParser();
	    		parser.parse(is, this);
	    	} catch (Exception e) { e.printStackTrace(); }
	    	return this.dblpDoc;
	    }
	    
	    public int getCountElement(){
	    	return this.countElement;
	    }
	}