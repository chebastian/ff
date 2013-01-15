import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;


public class Renderer {
	
	TileEdit mGame;
	LinkedList<RenderState> mStates;
	
	public Renderer(TileEdit game)
	{
		mGame = game;
		mStates = new LinkedList();
	}
	
	public void AddRenderState(RenderState state)
	{
		mStates.push(state);
	}
	
	public void RemoveRenderState(RenderState state)
	{
		mStates.contains(state);
		mStates.remove(state);
	}
	
	public boolean RemoveRenderStateByIndex(int stateIndex)
	{
		boolean found = false;
		RenderState toRemove = null;
		for(RenderState state : mStates)
		{
			if(state.getID() == stateIndex)
			{
				found = true;
				toRemove = state;
				mStates.remove(state);
				return true;
			}
		}
		
		return false;
	}
	
	public void renderStates(Graphics2D g)
	{
		for(RenderState state : mStates)
		{
			state.render(g, mGame.getCamera());
		}
	}
	public void renderTile(Graphics2D g, int x, int y, float scale, Tile toDraw)
	{
		float zoom = scale;
		
		if(toDraw.TileID < 0)
			toDraw.TileID = 0;
		int Tile_Width = mGame.TileWidth();
		int Tile_Height = mGame.TileHeight();
		int tileX = toDraw.x * (int)(Tile_Width*zoom);
		int tileY = toDraw.y * (int)(Tile_Height*zoom);
		
		mGame.GetTileSheet().RenderTile(g, toDraw.TileID, x + tileX, y +  tileY,(int)(Tile_Width*zoom),(int)(Tile_Height*zoom));
		if(toDraw.solid)
		{
			g.setColor(Color.red);
			g.drawRect(x + tileX+2, y + tileY+2, (int)(Tile_Width*zoom)-4,(int)(Tile_Height*zoom)-4);
		}
	}
	
	public void renderTileMap(Graphics2D g, int x, int y,float scale, TileMap map)
	{
		for(int  row = 0; row < map.Width(); row++)
		{
			for(int hor = 0; hor < map.Height(); hor++)
			{
				Tile toDraw = map.GetTile(row, hor);
				renderTile(g,x,y,scale,toDraw);
			}
		}
	}
	
	public void renderAllLayers(Graphics2D g, int x, int y, float scale,LayeredTilemap map)
	{
		int currentLayer = map.CurrentLayerIndex();
		
		Composite original = g.getComposite();
		for(int i = map.LayersSize(); i > 0; i--)
		{
			float editingLayerA = 0.5f;
			float alpha =  1.0f;
			int offset = 0;
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			map.SetActiveLayer(i);
			renderTileMap(g, x+offset, y+offset, scale, map);
		}		
		g.setComposite(original);
		map.SetActiveLayer(currentLayer);
		renderTileMap(g, x, y, scale,map.CurrentLayer());
		
		//g.drawString(Integer.toString(map.CurrentLayerIndex()), x, y);
		
	}
	
	public void RenderLevelGrid(Graphics2D g, int x, int y, float scale,LevelGrid grid)
	{
		Set<Entry<Point,LayeredTilemap>> sete = grid.GetRooms().entrySet();
		Iterator it = sete.iterator();
		
		while(it.hasNext())
		{
			Entry<Point,LayeredTilemap> entry = (Entry) it.next();
			
			LayeredTilemap map = entry.getValue();
			int mapscaledW = (int)((((map.Width())*mGame.TileWidth()))*mGame.getCamera().getZoom());
			int mapscaledH = (int)((((map.Height())*mGame.TileHeight()))*mGame.getCamera().getZoom());
			int offsetX = entry.getKey().x * mapscaledW;
			int offsetY = entry.getKey().y * mapscaledH;
			
			renderAllLayers(g, x + offsetX, y + offsetY,scale, map);
			
		}
		
	}
	
	public void renderLayerPreview(Graphics2D g, int x, int y, LayeredTilemap map)
	{
		int currentLayer = map.CurrentLayerIndex();
		
		Composite original = g.getComposite();
		for(int i = 0; i < map.LayersSize(); i++)
		{
			float zoomLevel = 0;
			float curScale = zoomLevel;
			zoomLevel = 0.3f;
			int mapscaledW = (int)((((map.Width())*mGame.TileWidth()))*zoomLevel);
			int mapscaledH = (int)((((map.Height())*mGame.TileHeight()))*zoomLevel);
			int offsetX = mapscaledW*i;
			int offsetY = (map.Height()*mGame.TileWidth()) + mGame.TileHeight();
			map.SetActiveLayer(i);
			
			Point toworld = mGame.getCamera().ScreenToWorld(new Point(offsetX,offsetY));
			Point screenPos = new Point(mGame.getCamera().getWidth()-mapscaledW,mGame.getCamera().getHeight()-mapscaledH*2);
			int bkgoffset = 3;
			//render bkg of preview
			Color currentCol = g.getColor();
			g.setColor(Color.gray);
			g.fillRect(screenPos.x + offsetX - bkgoffset, screenPos.y + offsetY - bkgoffset, mapscaledW + bkgoffset *2, mapscaledH + bkgoffset*2);
			g.setColor(currentCol);
			mGame.Renderer().renderTileMap(g, screenPos.x + offsetX, screenPos.y + offsetY, zoomLevel,map);
			//mGame.Renderer().renderTileMap(g, offsetX, offsetY, scale, map)

			if(i == currentLayer)
			{
				Color prev = g.getColor();
				g.setColor(Color.red);
				g.drawString(map.getName(), screenPos.x+offsetX, screenPos.y+offsetY);
				g.setColor(prev);
			}
			
			zoomLevel = curScale;
		}		
		map.SetActiveLayer(currentLayer);
	}
	
	public void RenderTileSheet(Graphics2D g,TileSheet sheet, int x, int y){
		
		int xpos = x;
		int offset = 2;
		int index = 0;
		int iy = 0;
		for(int i = 0; i <	 sheet.GetImagesInSheet().size(); i++)
		{
			index++;
			g.drawImage(sheet.GetImagesInSheet().get(i),null, (xpos), y + (iy*sheet.frameH));
			xpos = index * sheet.frameW;
			
			if(index >= sheet.numx)
			{
				index = 0;
				iy++;
			}
		}
	}
	
	public void RenderTileSheet(Graphics2D g,TileSheet sheet, int x, int y,int frameoffset){
		
		int xpos = x;
		int ypos = y;
		int offset = 2;
		int index = 0;
		int iy = 0;
		for(int i = 0; i <	 sheet.GetImagesInSheet().size(); i++)
		{
			index++;
			xpos = (index * sheet.frameW) + frameoffset*index;
			ypos = (y + (iy*sheet.frameH)) + frameoffset*iy;
			
			g.drawImage(sheet.GetImagesInSheet().get(i),null, (xpos), ypos);
			if(index >= sheet.numx)
			{
				index = 0;
				iy++;
			}
		}
	}
	

}
