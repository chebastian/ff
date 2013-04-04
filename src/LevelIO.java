import java.awt.Point;
import java.io.File;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LevelIO {
	
	TileEdit game;
	TileMap mMap;
	LevelScene mScene;
	int layer_size, level_w, level_h;
	String mColmapReminder;
	String mWorkingDirectory;
	String mLastLoadedName;
	String mLastLoadedPath;
	
	public LevelIO(TileEdit _game)
	{
		game = _game;
		mColmapReminder = "";
		mWorkingDirectory = "";
	}
	
	public boolean isPreviouslyLoaded(LevelScene scene)
	{
		return false;
	}
	
	public boolean isOverwrite(LevelScene scene)
	{
		return false;
	}
	
	public void SaveScene(String name, String filename, LevelScene scene) throws Exception
	{
		/*Document doc = CreateXMLDocument();
		if(doc == null)
			throw new Exception("could not create xmlfile");*/
		
		/*Element root = doc.createElement("scene");
		Element gameObjs = doc.createElement("game_objects");
		gameObjs = appendeGameObjectElement(doc, gameObjs, scene);*/
		Document doc = SaveLevelGrid(name, filename, scene.RoomGrid);
		Element sceneElem = doc.createElement("Scene");
		sceneElem.appendChild(doc.getElementById("level"));
		
		SaveDocumentToXmlFile(filename + "\\" + name + "\\" + name + "X.xml", doc);
		
	}
	
	protected Element appendeGameObjectElement(Document doc, Element rootelem, LevelScene scene)
	{
		for(GameEntity t : scene.Doors)
		{
			Element elem = doc.createElement(t.getName());
			Point p = t.getPosition();
			elem.setAttribute("x", Integer.toString(p.x));
			elem.setAttribute("y", Integer.toString(p.y));
			elem.setAttribute("open", Boolean.toString(!t.isSolid()));
			rootelem.appendChild(elem);
		}
		
		return rootelem;
	}
	
	public Document SaveLevelGrid(String name, String filename, LevelGrid grid)
	{
		
		Document doc = null;
		try
		{
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			doc = domBuilder.newDocument();
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		Element rootElement = doc.createElement("level");
		rootElement.setAttribute("w", Integer.toString(grid.GetAbsoluteGridSizeOffset().x + grid.GetGridSize().x+1));
		rootElement.setAttribute("h", Integer.toString(grid.GetAbsoluteGridSizeOffset().y + grid.GetGridSize().y+1));
		rootElement.setAttribute("id", name);
		
		Element roomhead = doc.createElement("rooms");
		
		Set<Entry<Point,LayeredTilemap>> sete = grid.GetRooms().entrySet();
		Iterator it = sete.iterator();
		
		createNewDirectory(name, filename);
		
		int counter = 0;
		while(it.hasNext())
		{
			Element roomelem = doc.createElement("room");
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			LayeredTilemap m = entry.getValue();
			SaveTileMap(filename + "\\" + name + "\\" + m.getName() + ".xml", m);
			roomelem.setAttribute("id", Integer.toString(counter));
			roomelem.setAttribute("file", m.getName() + ".xml");
			roomelem.setAttribute("indexX", Integer.toString(grid.GetRoomIndex(m).x ));
			roomelem.setAttribute("indexY", Integer.toString(grid.GetRoomIndex(m).y ));
			roomelem.setAttribute("name", m.getName());
			
			rootElement.setAttribute("tile_w",Integer.toString(m.TileWidth));
			rootElement.setAttribute("tile_h",Integer.toString(m.TileHeight));
			counter++;
			roomhead.appendChild(roomelem);
		}
		
		Element tilesheetelem = doc.createElement("tilemap");
		tilesheetelem.setAttribute("file","none.atm");
		
		Element gameObjs = doc.createElement("game_objects");
		Element doors = doc.createElement("doors");
		doors = appendeGameObjectElement(doc, doors, mScene);
		gameObjs.appendChild(doors);
		
		rootElement.appendChild(roomhead);
		rootElement.appendChild(tilesheetelem);
		rootElement.appendChild(gameObjs);
		doc.appendChild(rootElement);
		
		SaveDocumentToXmlFile(filename + "\\" + name + "\\" + name + ".xml", doc);
		
		return doc;
	}
	
	public void overWriteLevel(String name, String filename, LevelGrid grid)
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			doc = domBuilder.newDocument();
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		Element rootElement = doc.createElement("level");
		rootElement.setAttribute("w", Integer.toString(grid.GetAbsoluteGridSizeOffset().x + grid.GetGridSize().x+1));
		rootElement.setAttribute("h", Integer.toString(grid.GetAbsoluteGridSizeOffset().y + grid.GetGridSize().y+1));
		rootElement.setAttribute("id", name);
		
		Element roomhead = doc.createElement("rooms");
		
		Set<Entry<Point,LayeredTilemap>> sete = grid.GetRooms().entrySet();
		Iterator it = sete.iterator();
		
		createNewDirectory(name, filename);
		
		int counter = 0;
		while(it.hasNext())
		{
			Element roomelem = doc.createElement("room");
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			LayeredTilemap m = entry.getValue();
			SaveTileMap(filename + "\\" + name + "\\" + m.getName() + ".xml", m);
			roomelem.setAttribute("id", Integer.toString(counter));
			roomelem.setAttribute("file", m.getName() + ".xml");
			roomelem.setAttribute("indexX", Integer.toString(grid.GetRoomIndex(m).x ));
			roomelem.setAttribute("indexY", Integer.toString(grid.GetRoomIndex(m).y ));
			roomelem.setAttribute("name", m.getName());
			
			rootElement.setAttribute("tile_w",Integer.toString(m.TileWidth));
			rootElement.setAttribute("tile_h",Integer.toString(m.TileHeight));
			counter++;
			roomhead.appendChild(roomelem);
		}
		
		Element tilesheetelem = doc.createElement("tilemap");
		tilesheetelem.setAttribute("file","none.atm");
		rootElement.appendChild(roomhead);
		rootElement.appendChild(tilesheetelem);
		doc.appendChild(rootElement);
		
		SaveDocumentToXmlFile(filename + "\\" + name + "\\" + name + ".xml", doc);
	}
	
	public Document CreateXMLDocument()
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			doc = domBuilder.newDocument();
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
			return null;
		}
		
		return doc;
	}
	
	//Creates a new dir if no current exists
	public boolean createNewDirectory(String name, String path)
	{
		File theDir = new File(path + "/" + name);
		if(!theDir.exists())
		{
			theDir.mkdir();
			return true;
		}
		
		return false;
	}
	
	public boolean SaveDocumentToXmlFile(String fileName, Document doc)
	{
		try
		{
			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();
		    trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(new File(fileName));
	        DOMSource source = new DOMSource(doc);
	        trans.transform(source, result);
	        
		}
		catch(Exception e){
			
		}
        return true;
	}
	
	public void SaveTileMap(String fileName, LayeredTilemap map)
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			doc = domBuilder.newDocument();
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		Element rootElement = doc.createElement("Level");
		
		rootElement.setAttribute("width", Integer.toString(map.Width()));
		rootElement.setAttribute("height", Integer.toString(map.Height()));
		rootElement.setAttribute("layers", Integer.toString(map.LayersSize()));
		rootElement.setAttribute("name", map.getName());
		
		int activeLayer = map.CurrentLayerIndex();
		for(int i = 0; i < map.LayersSize(); i++)
		{
			map.SetActiveLayer(i);
			Element mapElem = CreateTileMapElem(doc,"data", map.CurrentLayer());
			rootElement.appendChild(mapElem);
		}
		map.SetActiveLayer(activeLayer);
		
		try
		{
			Element exitsElem = doc.createElement("exits");
			Element enemyElem = doc.createElement("enemies");
			rootElement.appendChild(exitsElem);
			rootElement.appendChild(enemyElem);
			
			doc.appendChild(rootElement);
			
			
			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();
		    trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(new File(fileName));
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
	}
	public Element CreateTileMapElem(Document doc,String elemName, TileMap map)
	{
		Element dataElem = doc.createElement(elemName);
			
		dataElem.setAttribute("tiles", map.MapAsString());
		
		dataElem.setAttribute("colData", map.ColMapAsString());
			
		mColmapReminder = map.ColMapAsString();
			
		return dataElem;
	}
	
	public LevelGrid LoadLevelGrid(String path)
	{
		Document doc = LoadXmlFile(path);
		
		LevelGrid grid = new LevelGrid(game);
		
		NodeList nodes = doc.getElementsByTagName("Level");
		NodeList roomL = doc.getElementsByTagName("room");
		Element rootElem = doc.getDocumentElement();
		
		int tileW = 1, tileH = 1;
		if(rootElem.hasAttribute("tile_w") && rootElem.hasAttribute("tile_h"))
		{
			tileW = GetIntAttribute("tile_w", rootElem);
			tileH = GetIntAttribute("tile_h", rootElem);
		}
		
		for(int i = 0; i < roomL.getLength(); i++)
		{
			Node roomNode = roomL.item(i);
			if(roomNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element elem = CreateElemFromNode(roomNode);
				String filename = elem.getAttribute("file");
				String name = elem.getAttribute("name");
				int id = GetIntAttribute("id", elem);
				int indexX = GetIntAttribute("indexX", elem);
				int indexY = GetIntAttribute("indexY", elem);
				
				String roompath = mWorkingDirectory + "\\" + filename;
				LayeredTilemap map = LoadLevel(roompath);
				map.TileHeight = tileH;
				map.TileWidth = tileW;
				map.SetName(name + grid.GetRooms().size());
				grid.AddNewRoom(name,map, new Point(indexX,indexY));
			}
		}
		
		mLastLoadedPath = path;
		return grid;
	}
	
	public LayeredTilemap LoadLevel(String path)
	{
		
		Document doc = LoadXmlFile(path);
			
		NodeList nList = doc.getElementsByTagName("Level");
		NodeList dataL = doc.getElementsByTagName("data");
		
		ParseLevelInfo(doc.getDocumentElement());
		LayeredTilemap map = new LayeredTilemap(level_w, level_h, 0);
			
		if(doc.getDocumentElement().hasAttribute("name"))
		{
			String name = doc.getDocumentElement().getAttribute("name");
			map.SetName(name);
		}
		
			for(int i = 0; i < dataL.getLength(); i++)
			{
				Node nNode = dataL.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element elem = (Element)nNode;
					map.AddNewLayer(ParseTileMapFromElement(elem));
					
					if(elem.hasAttribute("name"))
					{
						map.SetName(elem.getAttribute("name"));
					}
				}
			}
		
		return map;
	}
	
	public TileMap ParseTileMapFromElement(Element e)
	{
		TileMap map = new TileMap(level_w,level_h);
		int layer = 0;
		String tileData = "";
		String colData = "";
		
		tileData = e.getAttribute("tiles");
		colData = e.getAttribute("colData");
		layer = GetIntAttribute("layer", e);
		
		LinkedList<Integer> tileL = TileDataToIntArr(tileData);
		map = new TileMap(level_w,level_h,tileL);
		

		if(e.hasAttribute("colData"))
		{
			colData = e.getAttribute("colData");
			LinkedList<Integer> colL = TileDataToIntArr(colData);
			map.SetTileColDataFromList(colL);
		}
		
		
		return map;
	}
	
	public void ParseLevelInfo(Element elem)
	{
		int levelW = GetIntAttribute("width", elem);
		int levelH = GetIntAttribute("height", elem);
		layer_size = GetIntAttribute("layers", elem);
		level_w = levelW; level_h = levelH;
		mMap = new TileMap(levelW, levelH);
	}
	
	public Document LoadXmlFile(String path)
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			doc = domBuilder.parse(path);	
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage() + path);
		}
		
		return doc;
	}
	
	public int GetIntAttribute(String name, Element elem)
	{
		if(elem.hasAttribute(name))
			return Integer.parseInt(elem.getAttribute(name));
		
		return 0;
	}
	
	public LinkedList<Integer> TileDataToIntArr(String data)
	{
		LinkedList<Integer> nums = new LinkedList<Integer>();
		
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(data);
		while(m.find())
			nums.add(Integer.parseInt(m.group()));
		
		
		return nums;
	}

	protected String TileColmapAsString(TileMap map){
		
		String level = new String();
		for(int i = 0; i < map.Height(); i++)
		{
			for(int j = 0; j < map.Width(); j++)
			{
				Tile t = map.GetTile(j, i);
				
				if(t.solid)
					level += 1;
				else
					level += 0;
				
				level += ", ";
			}
			level += System.getProperty("line.separator");
		}
		return level;
	}
	
	Element CreateElemFromNode(Node n)
	{
		return (Element)n;
	}
	
	void SetWorkingDirectory(String dir)
	{
		mWorkingDirectory = dir;
	}
	
	final String GetWorkingDir()
	{
		return mWorkingDirectory;
	}
	
	public void setScene(LevelScene scene)
	{
		mScene = scene;
	}

}
