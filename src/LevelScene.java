import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;


public class LevelScene {
	
	TileEdit mGame;
	LevelGrid RoomGrid;
	LinkedList<Tile> Doors;
	
	public LevelScene(TileEdit game)
	{
		mGame = game;
		RoomGrid = null;
		Doors = new LinkedList<>();
	}
	
	public void AddNewDoor(Point position, String name, boolean open){
		Tile t = new Tile(position.x, position.y, 0);
		t.solid = open;
		Doors.add(t);
	}
	
	public void renderDoors(Graphics2D g, int x, int y)
	{
		Color c = g.getColor();
		g.setColor(Color.yellow);
		for(Tile t : Doors){
			int px = x + (t.x * mGame.TileWidth());
			int py = y + (t.y * mGame.TileHeight());
			int fff = 0;
			g.drawRect(px,py,mGame.TileWidth(), mGame.TileHeight());
		}
		
		g.setColor(c);
	}
	
	

}
