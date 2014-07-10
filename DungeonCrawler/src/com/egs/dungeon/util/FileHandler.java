package com.egs.dungeon.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.egs.dungeon.level.Tile;

public class FileHandler {

	private String location = System.getenv("APPDATA") + "/.dungeon";
	private File file;
	
	public FileHandler(){
		init();
	}
	
	private void init(){
		file = new File(location + "/saves/");
		if(!file.exists()) file.mkdirs();
		
		file = new File(location + "/res/");
		if(!file.exists()) file.mkdirs();
	}
		
	public void newWorldFolder(String name){
		file = new File(location + "/saves/" + name + "/data");
		if(!file.exists()) file.mkdirs();
	}	
	
	public void loadTiles(){
		XMLHandler xml = new XMLHandler("tiles.xml");
		Document doc = xml.asDocument();
		NodeList tileNodes = doc.getElementsByTagName("tiles");
		for(int i = 0; i < tileNodes.getLength(); i++){
			Node n = tileNodes.item(i);
			NodeList tiles = ((Element) n).getElementsByTagName("tile");
			for(int j = 0; j < tiles.getLength(); j++){
				Element e = (Element) tiles.item(j);
				new Tile(Integer.parseInt(e.getAttribute("id")), e.getAttribute("name"), e.getAttribute("solid").equals("1") ? true : false, e.getAttribute("liquid").equals("1") ? true : false, e.getAttribute("rgb"));
			}
		}
	}

	public int getRoomNumber(){
		return new File("res/rooms/").listFiles().length;
	}
	
	public String[] loadRoom(String name){
		file = new File("res/rooms/" + name + ".txt");
		if(!file.exists()) return null;
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				lines.add(line);
			}
			reader.close();
			return lines.toArray(new String[lines.size()]);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
