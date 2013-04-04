import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;


public class LevelGrid {
	
	Map<Point,LayeredTilemap> mGrid;
	Point mLastInsertedIndex;
	Point mSelectedIndex;
	TileEdit mGame;
	
	public LevelGrid(TileEdit game)
	{
		mGrid = new HashMap<Point,LayeredTilemap>();
		mLastInsertedIndex = new Point();
		mGame = game;
		mSelectedIndex = new Point();
	}
	
	public void testCreateLevelGrid()
	{
		LayeredTilemap map = new LayeredTilemap(2, 2, 3);
		map.TileHeight = 32; map.TileWidth = 32;
		this.AddNewRoom("testCreateLvl",map, new Point(0,0));
	}
	
	public void SetMapTileSize(int w, int h)
	{
		Set<Entry<Point,LayeredTilemap>> sete = mGrid.entrySet();
		Iterator it = sete.iterator();
		
		while(it.hasNext())
		{
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			
			LayeredTilemap m = entry.getValue();
			m.TileHeight = h;
			m.TileWidth = w;
			
			//m.PositionOffset.x = m.TileWidth * entry.getKey().x;
			//m.PositionOffset.y = m.TileWidth * entry.getKey().y;
		}
	}
	
	public void AddNewRoom(String n, LayeredTilemap map, Point p)
	{
		if(mGrid.containsKey(p)){
			mLastInsertedIndex = p;
			SetRoomAsSelected(map);
			return;
		}
		
		JOptionPane pane = new JOptionPane();
		if(map.getName() == "")
		{
			n = JOptionPane.showInputDialog("Room Name: ", "Room"+mGrid.size());
			if(n == null)
				return;
			
			LayeredTilemap parsed = ParseRoomFromString(n);
			map.CopyTileDataFrom(parsed);
			map.SetName(parsed.getName());
			//map.SetName(n);
		}
		map.SetMapOffset(new Point(p.x * map.MapWidthInPixels(), p.y * map.MapHeightInPixels()));
		
		mGrid.put(p, map);
		System.out.print("SIIIZE : " + mGrid.size());
		mLastInsertedIndex = p;
		SetRoomAsSelected(map);
	}
	
	public LayeredTilemap ParseRoomFromString(String str)
	{
		LayeredTilemap map = new LayeredTilemap(2, 2, 3);
		int index = str.indexOf(":");
		String spliced = str.substring(index+1);
		
		if(str.startsWith("Load:"))
		{
			LevelIO levelLoader = new LevelIO(mGame);
			
			String fullPath = mGame.LEVEL_TEMPLATE_DIR + spliced;
			
			map = levelLoader.LoadLevel(fullPath);
		}
		else if(str.startsWith("Name:"))
		{
			map.SetName(spliced);
		}
		else
		{
			map.SetName(str);
		}
		
		return map;
	}
	
	public void AddAdjecentRoom(LayeredTilemap base, LayeredTilemap to_add, Point p)
	{
		Point index = GetRoomIndex(base);
		Point newI = new Point();
		to_add.TileWidth = base.TileWidth;
		to_add.TileHeight = base.TileHeight;
		newI.x = index.x + p.x;	
		newI.y = index.y + p.y;
		AddNewRoom("Name:"+to_add.getName(),to_add, newI);
	}
	
	public LayeredTilemap GetRoomByIndex(Point p) throws Exception
	{
		if(!mGrid.containsKey(p))
		{
			throw new Exception("Tried to get index out of bounds: " + p.toString());
		}
		
		LayeredTilemap map = mGrid.get(p);
		
		return map;
	}
	
	public Map GetRooms()
	{
		return mGrid;
	}
	
	public Point GetRoomIndex(LayeredTilemap map)
	{
		Set<Entry<Point,LayeredTilemap>> sete = mGrid.entrySet();
		Iterator it = sete.iterator();
		
		while(it.hasNext())
		{
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			
			LayeredTilemap m = entry.getValue();
			if(m.equals(map))
				return entry.getKey();
		}
		Point p = new Point();
		
		return p;
	}
	
	public LayeredTilemap getRoomLastAdded()
	{
		LayeredTilemap last = null;
		try
		{	
			last = GetRoomByIndex(mLastInsertedIndex);
		}
		catch(Exception e){
			
		}
		
		return last;
	}
	
	public void ChangeRoomAtIndex(Point p, LayeredTilemap map)
	{
		mGrid.put(p, map);
	}
	
	public final Point GetSelectedIndex()
	{
		return mSelectedIndex;
	}
	
	public void SetSelectedIndex(Point p)
	{
		mSelectedIndex = p;
	}
	
	public void SetRoomAsSelected(LayeredTilemap map)
	{
		Point p = GetRoomIndex(map);
		mSelectedIndex = p;
	}
	
	public LayeredTilemap GetSelectedRoom()
	{
		LayeredTilemap map = null;
		try
		{
			map = GetRoomByIndex(mSelectedIndex);
		}
		catch(Exception e){
			
		}
		
		return map; 
	}
	
	public Point GetGridSize()
	{
		Set<Entry<Point,LayeredTilemap>> sete = mGrid.entrySet();
		Iterator it = sete.iterator();
		Point index = new Point();
		
		while(it.hasNext())
		{
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			
			LayeredTilemap m = entry.getValue();
			Point newindex = GetRoomIndex(m);
			
			if(index.x < newindex.x && newindex.x > 0)
			{
				index.x = newindex.x;
			}
			if(index.y < newindex.y && newindex.y > 0)
			{
				index.y = newindex.y;
			}
		}
		
		return index;
	}
	
	private Point getMinIndex()
	{
		int indexX = 0;
		
		Set<Entry<Point,LayeredTilemap>> sete = mGrid.entrySet();
		Iterator it = sete.iterator();
		Point index = new Point();
		
		while(it.hasNext())
		{
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			
			LayeredTilemap m = entry.getValue();
			Point newindex = GetRoomIndex(m);
			
			if(index.x > newindex.x)
			{
				index.x = newindex.x;
			}
			if(index.y > newindex.y)
			{
				index.y = newindex.y;
			}
		}
		
		return index;
	}
	
	private Point getMaxIndex()
	{
		int indexX = 0;
		
		Set<Entry<Point,LayeredTilemap>> sete = mGrid.entrySet();
		Iterator it = sete.iterator();
		Point index = new Point();
		
		while(it.hasNext())
		{
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			
			LayeredTilemap m = entry.getValue();
			Point newindex = GetRoomIndex(m);
			
			if(index.x < newindex.x)
			{
				index.x = newindex.x;
			}
			if(index.y < newindex.y)
			{
				index.y = newindex.y;
			}
		}
		
		return index;
	}
	
	public Point GetAbsoluteGridSizeOffset()
	{
		Point ab_sz= new Point();
		
		Point minIndex = getMinIndex();
		Point maxIndex = getMaxIndex();
		
		if(minIndex.x >= 0 && minIndex.y >= 0)
			return ab_sz;
		
		ab_sz.x = Math.abs(minIndex.x);
		ab_sz.y = Math.abs(minIndex.y);
		
		return ab_sz;
	}
}