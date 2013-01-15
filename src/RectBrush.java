import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;


public class RectBrush extends Brush {

	Point startPos, endPos, currentPos;
	int rectW, rectH;
	int tile_w, tile_h;
	boolean dragging;
	
	public RectBrush(TileEdit game, int tilew, int tileh) {
		super(game);
		startPos = new Point();
		endPos = new Point();
		rectH = 1;
		rectW = 1;
		tile_w = tilew; tile_h = tileh;
		dragging = false;
		BrushName = "fillRect";
	}
	
	public void render(Graphics2D g)
	{
		Point start = map.MouseToTileIndex(startPos);
		Point end = map.MouseToTileIndex(currentPos);
		
		if(dragging)
			g.drawRect(startPos.x, startPos.y, currentPos.x - startPos.x, currentPos.y - startPos.y);
	}
	
	void FillTiles()
	{
		int iX = rectW / tile_w;
		int iY = rectH / tile_h;
		
		Point start = map.MouseToTileIndex(startPos);
		Point end = map.MouseToTileIndex(endPos);
		
		int xdir = (end.x > start.x) ? 1 : -1;
		int ydir = (end.y > start.y) ? 1 : -1;
		
		int xlen = Math.abs(end.x - start.x);
		int ylen = Math.abs(end.y - start.y);
		
		for(int i = start.x; i <= end.x; i++)
		{
			for(int j = start.y; j <= end.y; j++)
			{
				map.SetTileValue(i, j, ActiveValue);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		startPos = GetPos();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		endPos = GetPos();
		
		rectH = endPos. x - startPos.x;
		rectW = endPos.y - startPos.y;
		FillTiles();
		dragging = false;
	}
	
	public void mouseDragged(MouseEvent e)
	{
		mPosition.x = e.getX();
		mPosition.y = e.getY();
		currentPos = GetPos();
		dragging = true;
	}
}
