import java.awt.Point;


public class Tile {
	
	public int TileID, x, y;
	public boolean solid;
	
	public Tile(int x, int y, int tileID)
	{
		this.x = x; this.y = y;
		this.TileID = tileID;
		solid = false;
	}

}
