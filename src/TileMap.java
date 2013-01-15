import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;


public class TileMap {
	
	protected ArrayList<ArrayList<Tile>> mTiles;
	protected int mWidth, mHeight;
	int TileWidth, TileHeight;
	Point PositionOffset;
	float Scale;
	
	public TileMap(int w, int h)
	{
		mTiles = new ArrayList<ArrayList<Tile>>();
		mWidth = w; mHeight = h;
		PositionOffset = new Point();
		TileWidth = 1; TileHeight = 1;
		Scale = 1.0f;
		FillMap(-1);
	}
	
	public TileMap(int w, int h, int[] tiles)
	{
		mTiles = new ArrayList<ArrayList<Tile>>();
		mWidth = w; mHeight = h;
		PositionOffset = new Point();
		TileWidth = 1; TileHeight = 1;
		Scale = 1.0f;
		
		SetTiles(w, h , tiles);
	}
	
	public TileMap(int w, int h, LinkedList<Integer> tiles)
	{
		mTiles = new ArrayList<ArrayList<Tile>>();
		mWidth = w; mHeight = h;
		PositionOffset = new Point();
		TileWidth = 1; TileHeight = 1;
		Scale = 1.0f;

		
		SetTiles(w, h , tiles);
	}
	
	public TileMap(TileMap map)
	{
		mTiles = map.mTiles;
		this.mHeight = map.mHeight;
		this.mWidth = map.mWidth;
		this.PositionOffset = map.PositionOffset;
		this.Scale = map.Scale;
		this.TileHeight = map.TileHeight;
		this.TileWidth = map.TileWidth;
	}
	
	protected void FillMap(int val)
	{
		mTiles.clear();
		for(int i = 0; i < mWidth; i++)
		{
			mTiles.add(new ArrayList<Tile>());
			for(int j = 0; j < mHeight; j++)
			{
					mTiles.get(i).add( new Tile(i,j,val));
			}
		}
	}
	
	public TileMap ShallowCopy()
	{
		TileMap map = new TileMap(this);
		map.ClearMap();
		return map;
	}
	
	public void SetTiles(int w, int h, int[] arr)
	{
		
		mTiles.clear();
		for(int i = 0; i < w; i++)
		{
			mTiles.add(new ArrayList<Tile>());
			for(int j = 0; j < h; j++)
			{
				mTiles.get(i).add(new Tile(i,j,arr[i+j*w]));
				System.out.print(arr[i+j*w]);
				System.out.print(" ");
			}
		}
	}
	public void SetTiles(int w, int h, LinkedList<Integer> tiles)
	{
		int[] arr = new int[tiles.size()];
		for(int i = 0; i < tiles.size(); i++)
			arr[i] = tiles.get(i);
		
		mTiles.clear();
		for(int i = 0; i < w; i++)
		{
			mTiles.add(new ArrayList<Tile>());
			for(int j = 0; j < h; j++)
			{
				mTiles.get(i).add(new Tile(i,j,arr[i+j*w]));
				System.out.print(arr[i+j*w]);
				System.out.print(" ");
			}
			System.out.print("\n");
			int done = 0;
		}
		/*for(int i = 0; i < h; i++)
		{
			mTiles.add(new ArrayList<Tile>());
		}*/
	}
	
	public void SetTileColDataFromList(LinkedList<Integer> tiles)
	{
		for(int i = 0; i < mWidth; i++)
		{
			//mTiles.add(new ArrayList<Tile>());
			for(int j = 0; j < mHeight; j++)
			{
				mTiles.get(i).get(j).solid = (tiles.get(i+j*mWidth) == 1);
			}
		}
	}
	
	public static TileMap CreateEmptyMap(int w, int h, int baseVal)
	{
		TileMap map = new TileMap(w, h);
		
		for(int i = 0; i < w; i++)
		{
			map.mTiles.add(new ArrayList<Tile>());
			for(int j = 0; j < h; j++)
			{
				map.mTiles.get(i).add(new Tile(i,j,baseVal));
			}
		}
		return map;
	}
	
	public void CopyMapTo(TileMap new_map)
	{
		for(int x = 0; ((x < Width()) && (x < new_map.Width())); x++)
		{
			for(int y = 0;((y < Height()) && (y < new_map.Height() )); y++)
			{
				new_map.SetTileValue(x, y, mTiles.get(x).get(y).TileID);
				new_map.GetTile(x, y).solid = GetTile(x, y).solid;
			}
		}
	}
	
	public Tile GetTile(int x, int y)
	{
		Tile empty = new Tile(x, y, 0);
		
		try
		{
			empty = mTiles.get(x).get(y);
		}
		catch(Exception e)
		{
			System.out.print("Tried to get a tile out of bounds \n X: " + Integer.toString(x) + " Y: " + Integer.toString(y));
			System.out.print(e.getMessage());
		}
		
		return empty;
	}
	
	public Tile GetTile(int index)
	{
		Tile empty = new Tile(0,0,0);
		
		Point pos = IndexToArrayIndex(index);
		try
		{
			empty = mTiles.get(pos.x).get(pos.y);
		}
		catch(Exception e)
		{
			System.out.print("Tried to get a tile out of bounds FUUUUCK \n");
			System.out.print(e.getMessage());
		}
		
		return empty;
	}
	
	public void SetTileValue(int x, int y, int val)
	{
		Tile t = new Tile(0,0,0);
		try
		{
			t = GetTile(x, y);
			t.TileID = val;
		}
		catch(Exception e){
			System.out.print(e.getMessage());
		}
		//mTiles.get(x).get(y).TileID = val;
	}
	
	public void SetTileSolid(int x, int y, boolean val)
	{
		Tile t = new Tile(0, 0, 0);
		try
		{
			t = GetTile(x,y);
			t.solid = val;
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void ClearMap()
	{
		for(int i = 0; i < Width(); i++)
		{
			mTiles.get(i).clear();
			for(int j = 0; j < Height(); j++)
			{
				mTiles.get(i).add(new Tile(i, j, 0));
			}
		}
	}
	
	public int Width()
	{
		return mWidth;
	}
	
	public int Height()
	{
		return mHeight;
	}
	
	public Point MouseToTileIndex(Point p)
	{
		Point pos = new Point(0,0);
		pos.x =  (p.x - PositionOffset.x) / (int)(TileWidth*Scale);
		pos.y = (p.y - PositionOffset.y) / (int)(TileHeight*Scale);
		return pos;
	}
	
	public Point IndexToArrayIndex(int index)
	{
		Point arr_index = new Point(0,0);
		
		arr_index.y = index / mHeight;
		arr_index.x = (mWidth * (arr_index.y+1)) - index;
		
		return arr_index;
	}
	
	public boolean IndexIsInMap(Point index)
	{
		if(index.x >= mWidth || index.y >= mHeight)
			return false;
		else if (index.x < 0 || index.y < 0)
			return false;
		
		return true;
	}
	
	public void SetMapOffset(Point offset)
	{
		this.PositionOffset = offset;
	}
	
	public int MapWidthInPixels()
	{
		return (int)((mWidth * TileWidth)*Scale);
	}
	
	public int MapHeightInPixels()
	{
		return (int)((mHeight * TileHeight)*Scale);
	}
	
	public ArrayList<Tile> GetSurroundingTiles(Point index)
	{
		Point[] dirs = {new Point(0,-1),new Point(1,0),new Point(0,1),new Point(-1,0)};
		ArrayList<Tile> result = new ArrayList<Tile>();

		for(int dirIter = 0; dirIter < dirs.length; dirIter++)
		{
			Point newIndex = new Point(index.x + dirs[dirIter].x,
					index.y + dirs[dirIter].y);
			if(IndexIsInMap(newIndex))
			{
				result.add(GetTile(newIndex.x, newIndex.y));
			}
		}
		
		return result;
	}
	
	public ArrayList<Tile> GetSurroundingTilesOfType(Point index, int type)
	{
		Point[] dirs = {new Point(0,-1),new Point(1,0),new Point(0,1),new Point(-1,0)};
		ArrayList<Tile> result = new ArrayList<Tile>();
		//check if toCHeck tiles are of same type
		for(int dirIter = 0; dirIter < dirs.length; dirIter++)
		{
			Point newIndex = new Point(index.x + dirs[dirIter].x,
					index.y + dirs[dirIter].y);
			if(IndexIsInMap(newIndex))
			{
				if(GetTile(newIndex.x, newIndex.y).TileID == type)
					result.add(GetTile(newIndex.x, newIndex.y));
			}
		}
		
		return result;
	}
	
	public String MapAsString()
	{
			String level = new String();
			
			for(int i = 0; i < mHeight; i++)
			{
				for(int j = 0; j < mWidth; j++)
				{
					Tile t = this.GetTile(j, i);
					level += t.TileID;
					level += ", ";
				}
				level += System.getProperty("line.separator");
			}
			
			return level;
	}
	
	
	public String ColMapAsString()
	{
		String level = new String();
		
		for(int i = 0; i < this.Height(); i++)
		{
			for(int j = 0; j < this.Width(); j++)
			{
				Tile t = this.GetTile(j, i);
				level += t.solid ? 1 : 0;
				level += ", ";
			}
			level += System.getProperty("line.separator");
		}
		
		return level;
	}
	

}
