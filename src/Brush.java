import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Brush implements MouseMotionListener, MouseListener{

	TileEdit mGame;
	Point mPosition;
	int Width = 2;
	int Height = 2;
	int MouseXOffset = 5;
	int MouseYOffset = 50;
	TileMap map;
	int ActiveValue;
	boolean activated;
	boolean PaintSolid;
	
	protected String BrushName;
	protected String BrushKeyCode;
	
	public Brush(TileEdit game)
	{
		mGame = game;
		mPosition = new Point(0,0);
		MouseXOffset = 5;
		MouseYOffset = 50;
		PaintSolid = false;
		System.out.print("Added Mouse Listner");
		BrushKeyCode = "A";
		BrushName = "pen";
	}
	
	public void CleanUp()
	{
		mGame.mFrame.removeMouseListener(this);
		mGame.mFrame.removeMouseMotionListener(this);
		activated = false;
	}
	
	public void SetMapToEdit(TileMap map)
	{
		this.map = map;
	}
	
	public void SetActiveValue(int val)
	{
		ActiveValue = val;
	}
	
	public Point GetPos()
	{
		Point p = new Point(0,0);
		p.x  = mPosition.x - MouseXOffset;
		p.y  = mPosition.y - MouseYOffset;
		
		p = mGame.getCamera().WorldToScreen(p);
		return p;
	}
	
	public Point GetScreenPos()
	{
		Point p = new Point(0,0);
		p.x  = mPosition.x - MouseXOffset;
		p.y  = mPosition.y - MouseYOffset;
		
		return p;
	}
	
	public void render(Graphics2D g)
	{
		g.fillRect(GetPos().x, GetPos().y, Width, Height);
		if(activated == false)
		{
			mGame.mFrame.removeMouseListener(this);
			mGame.mFrame.removeMouseMotionListener(this);
		}
	}
	
	public void PaintTile(Point p)
	{
		map.SetTileValue(p.x, p.y, ActiveValue);
		map.GetTile(p.x,p.y).solid = PaintSolid;
	}
	
	public void GrabTile(Point p)
	{
		Tile t = map.GetTile(p.x,p.y);
		ActiveValue = map.GetTile(p.x, p.y).TileID;
		PaintSolid = map.GetTile(p.x, p.y).solid;
	}
	
	public void handleMouseEvent(MouseEvent e)
	{
		Point mouse = map.MouseToTileIndex(GetPos());
		int mod = e.getModifiers();
		switch(mod)
		{
		case InputEvent.BUTTON1_MASK:{
			PaintTile(mouse);
			break;
		}
		
		case InputEvent.BUTTON2_MASK:{
			GrabTile(mouse);
			break;
		}
		
		case InputEvent.BUTTON3_MASK:{
			GrabTile(mouse);
			break;
		}
	}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//Point mouse = map.MouseToTileIndex(GetPos());
		handleMouseEvent(arg0);
		//PaintTile(mouse);
		//map.SetTileValue(mouse.x, mouse.y, ActiveValue);
		//map.GetTile(mouse.x,mouse.y).solid = PaintSolid;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Point mouse = map.MouseToTileIndex(GetPos());
		//Point mouse = mGame.getCamera().ScreenToWorld(GetPos());
		handleMouseEvent(arg0);
		//PaintTile(mouse);
		//map.SetTileValue(mouse.x, mouse.y, ActiveValue);
		//map.GetTile(mouse.x,mouse.y).solid = PaintSolid;
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mPosition.x = e.getX();
		mPosition.y = e.getY();
		Point mouse = map.MouseToTileIndex(GetPos());
		//PaintTile(mouse);
		handleMouseEvent(e);
		//map.SetTileValue(mouse.x, mouse.y, ActiveValue);
		//map.GetTile(mouse.x,mouse.y).solid = PaintSolid;
		
		System.out.print("DRAWING");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mPosition.x = e.getX();
		mPosition.y = e.getY();
		
	}
	
	public String Name()
	{
		return BrushName;
	}
	
	public String BrushKeyCode()
	{
		return BrushKeyCode;
	}
	
	public boolean BrushEquals(Brush b)
	{
		return b.Name().toLowerCase() == Name().toLowerCase();
	}

}
