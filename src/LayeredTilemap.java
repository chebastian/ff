import java.awt.Point;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.LinkedList;


public class LayeredTilemap extends TileMap{
	
	protected LinkedList<TileMap> mLayers;
	protected int mCurrentLayer = 0;
	protected int mNumLayers;
	protected String mName;
	protected TileSheet mCurrentSheet;
	protected boolean mIsLoadedFromFile;
	
	LayeredTilemap(int w, int h, int layers)
	{
		super(w, h);
		mLayers = new LinkedList<TileMap>();
		mNumLayers = layers;
		SetActiveLayer(0);
		
		for(int i = 0; i<layers; i++)
			mLayers.add(new TileMap(w,h));
		
		mName = "";
		mCurrentLayer = 0;
		mCurrentSheet = null;
		mIsLoadedFromFile = false;
	}
	
	public void AddNewLayer(TileMap map)
	{
		mLayers.add(map);
		mNumLayers = mLayers.size();
	}
	
	public void SetTiles(int w, int h, int layer, int[] arr)
	{
		mLayers.get(layer).SetTiles(w, h, arr);
	}
	public void SetTiles(int w, int h, int[] arr)
	{
		CurrentLayer().SetTiles(w, h, arr);
	}
	
	public void SetTiles(int w, int h, int layer, LinkedList<Integer> tiles)
	{
		mLayers.get(layer).SetTiles(w, h, tiles);
	}
	public void SetTiles(int w, int h, LinkedList<Integer> tiles)
	{
		CurrentLayer().SetTiles(w, h, tiles);
	}
	
	public void CopyTileDataFrom(LayeredTilemap map)
	{
		int curLayer = CurrentLayerIndex();
		for(int i = 0; i < LayersSize(); i++)
		{
			map.GetTilesFromLayer(i).CopyMapTo(GetTilesFromLayer(i));
		}
	}
	
	void SetActiveLayer(int id)
	{
		if(id >= mNumLayers)
			id = 0;
		else if(id < 0)
			id = mNumLayers-1;
		
		mCurrentLayer = id;
	}
	
	public void SetName(String name)
	{
		mName = name;
	}
	
	public Tile GetTile(int x, int y)
	{
		return CurrentLayer().GetTile(x,y);
	}
	
	public Tile GetTile(int index)
	{
		return CurrentLayer().GetTile(index);
	}
	
	public void SetTileValue(int x, int y, int val)
	{
		CurrentLayer().SetTileValue(x, y, val);
	}
	
	public void SetTileValue(int x, int y, int layer,int val)
	{
		GetTilesFromLayer(layer).GetTile(x, y).TileID = val;
	}
	
	public void SetTileValueOfAllLayers(int x, int y, int val)
	{
		if(true)
		{
			for(int i = 0; i < LayersSize(); i++)
			{
				SetTileValue(x, y, i, val);
			}
		}
	}
	
	public void SetTileSolid(int x, int y, boolean val)
	{
		for(TileMap map : mLayers)
		{
			map.SetTileSolid(x, y, val);
		}
	}
	
	public void ClearMap()
	{
		CurrentLayer().ClearMap();
	}
	
	public TileMap CurrentLayer()
	{	
		return mLayers.get(mCurrentLayer);
	}
	
	public int CurrentLayerIndex()
	{
		return mCurrentLayer;
	}
	
	public int LayersSize()
	{
		return mNumLayers;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public boolean IsLoadedFromFile()
	{
		return mIsLoadedFromFile;
	}
	
	public void SetLoadedFromFile(boolean status)
	{
		mIsLoadedFromFile = status;
	}
	
	public TileMap GetTilesFromLayer(int layer)
	{
		if(LayersSize() <= layer)
			return null;
		
		return mLayers.get(layer);
	}
	
	public void SetTileSheet(TileSheet sheet)
	{
		mCurrentSheet = sheet;
	}
	
	public boolean SheetIsLinked()
	{
		return mCurrentSheet != null;
	}
	
}
