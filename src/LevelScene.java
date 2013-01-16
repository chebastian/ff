import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;


public class LevelScene {
	
	TileEdit mGame;
	LevelGrid RoomGrid;
	LinkedList<GameEntity> Doors;
	
	public LevelScene(TileEdit game)
	{
		mGame = game;
		RoomGrid = null;
		Doors = new LinkedList<>();
	}
	
	public void AddNewDoor(GameEntity ent, String name){
		
		if(containsGameEntity(ent))
			return;
		
		Doors.add(ent);
	}
	
	public boolean containsGameEntity(GameEntity ent)
	{
		for(GameEntity e : Doors)
		{
			if(e.getPosition().x == ent.getPosition().x &&
					e.getPosition().y == ent.getPosition().y)
				return true;
		}
		
		return false;
	}
	
	public void renderDoors(Graphics2D g, int x, int y)
	{
		Color c = g.getColor();
		g.setColor(Color.yellow);
		for(GameEntity t : Doors){
			Point p = t.getPosition();
			int px = x + (p.x * mGame.TileWidth());
			int py = y + (p.y * mGame.TileHeight());
			g.drawRect(px,py,mGame.TileWidth(), mGame.TileHeight());
		}
		
		g.setColor(c);
	}
	
	

}
