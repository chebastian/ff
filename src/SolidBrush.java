import java.awt.Point;
import java.awt.event.MouseEvent;


public class SolidBrush extends Brush{

	public SolidBrush(TileEdit game) {
		super(game);
		// TODO Auto-generated constructor stub
		PaintSolid = true;
		BrushName = "solid";
	}

	public void PaintTile(Point p)
	{
		//map.GetTile(p.x, p.y).solid = PaintSolid;
		map.SetTileSolid(p.x, p.y,PaintSolid);
	}
	
	public void mousePressed(MouseEvent e)
	{
		Point mouse = map.MouseToTileIndex(GetPos());
		PaintSolid = !map.GetTile(mouse.x, mouse.y).solid;
		PaintTile(mouse);
	}
}
