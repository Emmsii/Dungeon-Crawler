package com.egs.dungeon.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
	
	public int[][] loadRoomTileData(String name){
		file = new File("res/rooms/" + name + ".json");
		if(!file.exists()) return null;

		int[] tileData;
		int[][] result;
		
		try{
			FileReader reader = new FileReader(file);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			
			long width = (long) jsonObject.get("width");
			long height = (long) jsonObject.get("height");
			JSONArray data = (JSONArray) jsonObject.get("tileData");

			tileData = new int[(int) (width * height)];
			result = new int[(int) width][(int) height];
			
			for(int i = 0; i < data.size(); i++){
				String raw = data.get(i).toString();
				tileData[i] = Integer.parseInt(raw);
			}
			
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					result[x][y] = tileData[(int) (x + y * width)];
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}catch(ParseException e){
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	public int[][] loadConnectData(String name){
		file = new File("res/rooms/" + name +".json");
		if(!file.exists()) return null;
		
		int[] connectData;
		int[][] result;
		
		try{
			FileReader reader = new FileReader(file);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			
			long width = (long) jsonObject.get("width");
			long height = (long) jsonObject.get("height");
			JSONArray data = (JSONArray) jsonObject.get("connectData");
			
			connectData = new int[(int) (width * height)];
			result = new int[(int) width][(int) height];
			
			for(int i = 0; i < data.size(); i++){
				String raw = data.get(i).toString();
				connectData[i] = Integer.parseInt(raw);
			}
			
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					result[x][y] = connectData[(int) (x + y * width)];
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}catch(ParseException e){
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	
//	public String[] loadRoom(String name){
//		file = new File("res/rooms/" + name + ".txt");
//		if(!file.exists()) return null;
//		
//		List<String> lines = new ArrayList<String>();
//		String line = null;
//		try{
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			while((line = reader.readLine()) != null){
//				lines.add(line);
//			}
//			reader.close();
//			return lines.toArray(new String[lines.size()]);
//		}catch(IOException e){
//			e.printStackTrace();
//			return null;
//		}
//	}
}
