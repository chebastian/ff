import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicReference;


public class LayerPreviewRenderer  extends RenderState{
	
	int x;
	int y;
	public static final int LAYER_RENDER_ID = 3;
	LayeredTilemap mMap;
	LayeredTilemap[] map;
	
	public LayerPreviewRenderer(TileEdit game, LayeredTilemap map)
	{
		super(game);
		//mMap = map;
		mId = LAYER_RENDER_ID;
		mMap = map;
	}
	
	public void render(Graphics2D g, TileEditorCamera cam)
	{
		int currentLayer = mMap.CurrentLayerIndex();
		
		Composite original = g.getComposite();
		for(int i = 0; i < mMap.LayersSize(); i++)
		{
			float zoomLevel = 0;
			float curScale = zoomLevel;
			zoomLevel = 0.3f;
			int mapscaledW = (int)((((mMap.Width())*mGame.TileWidth()))*zoomLevel);
			int mapscaledH = (int)((((mMap.Height())*mGame.TileHeight()))*zoomLevel);
			int offsetX = mapscaledW*i;
			int offsetY = (mMap.Height()*mGame.TileWidth()) + mGame.TileHeight();
			mMap.SetActiveLayer(i);
			
			Point toworld = mGame.getCamera().ScreenToWorld(new Point(offsetX,offsetY));
			Point screenPos = new Point(mGame.getCamera().getWidth()-mapscaledW,mGame.getCamera().getHeight()-mapscaledH*2);
			int bkgoffset = 3;
			//render bkg of preview
			Color currentCol = g.getColor();
			g.setColor(Color.gray);
			g.fillRect(screenPos.x + offsetX - bkgoffset, screenPos.y + offsetY - bkgoffset, mapscaledW + bkgoffset *2, mapscaledH + bkgoffset*2);
			g.setColor(currentCol);
			mGame.Renderer().renderTileMap(g, screenPos.x + offsetX, screenPos.y + offsetY, zoomLevel,mMap);
			//mGame.Renderer().renderTileMap(g, offsetX, offsetY, scale, map)

			if(i == currentLayer)
			{
				Color prev = g.getColor();
				g.setColor(Color.red);
				g.drawString("Current", offsetX, offsetY);
				g.setColor(prev);
			}
			
			zoomLevel = curScale;
		}		
		mMap.SetActiveLayer(currentLayer);
		
		g.drawString(Integer.toString(mMap.CurrentLayerIndex()), x, y);
	}

}
