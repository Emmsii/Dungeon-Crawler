package com.egs.dungeon.util;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLHandler {

	private String location;
	private File file;
	private Document xmlDoc;
	
	public XMLHandler(String location){
		this.location = location;
		
		try {
			init();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		xmlDoc.normalize();
	}
	
	private void init() throws ParserConfigurationException, SAXException, IOException{
		file = new File("res/" + location);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		xmlDoc = builder.parse(file);
	}
	
	public String getPath(){
		return location;
	}
	
	public Document asDocument(){
		return xmlDoc;
	}
}
