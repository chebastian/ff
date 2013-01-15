import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;


public class TileEditorCamera {
	
	protected Point position;
	protected int width, height;
	protected float zoom;
	
	public TileEditorCamera(Point p, int w, int h)
	{
		position = p;
		width = w; height = h;
		zoom = 1.0f;
	}
	
	public void setPosition(Point p)
	{
		position.x = p.x;
		position.y = p.y;
	}
	
	public void CenterOn(Point p)
	{
		position.x = p.x - width/2;
		position.y = p.y - height/2;
	}
	
	public Point getCenter()
	{
		Point center = new Point();
		center.x = position.x + width/2;
		center.y = position.y + height/2;
		return center;
	}
	
	public void setSize(int w, int h)
	{
		width = w; height = h;
	}
	
	public void Translate(int x, int y)
	{
		position.x += x; position.y += y;
	}
	
	public void Zoom(float z)
	{
		zoom = z;
	}
	
	public boolean RectIsInView(Point p, int w, int h)
	{
		Rectangle dst = new Rectangle(p.x, p.y, w, h);
		Rectangle cam = new Rectangle(position.x, position.y,width,height);
		
		return cam.intersects(dst);
	}
	
	public Point ScreenToWorld(Point p)
	{
		Point newpos = new Point();
		
		/*newpos.x = position.x - p.x;
		newpos.y = position.y - p.y;*/

		newpos.x = p.x - position.x;
		newpos.y = p.y - position.y;
		
		return newpos;
	}
	
	public Point WorldToScreen(Point p)
	{
		Point newpos = new Point();
		
		newpos.x = p.x + position.x;
		newpos.y = p.y + position.y;
		
		return newpos;
	}
	
	public void TranslateCam(Point dir)
	{
		position.x += dir.x;
		position.y += dir.y;
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public float getZoom()
	{
		return zoom;
	}

}
