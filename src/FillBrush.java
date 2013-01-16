import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;


public class FillBrush extends Brush {

	public FillBrush(TileEdit game) {
		super(game);
		// TODO Auto-generated constructor stub
		BrushName = "fill";
	}
	
	public void OnClick(TileMap map, Point p)
	{
		
	}
	
	public void PaintTile(Point p)
	{
		//get tile at mouse pos
				Point mousePos = map.MouseToTileIndex(GetPos());
				Tile firstTile = map.GetTile(mousePos.x, mousePos.y);
				int valueToChange = firstTile.TileID;
				int newValue = ActiveValue;
				
				//store sorounding tiles in toCheck list,
				ArrayList<Tile> toCheck, toAlter, checkedList;
				toCheck = new ArrayList<Tile>();
				toAlter = new ArrayList<Tile>();
				checkedList = new ArrayList<Tile>();
				
				toCheck = map.GetSurroundingTilesOfType(mousePos,valueToChange);
				
				System.out.print( " active: " + newValue);
				System.out.print( " picked: " + valueToChange);
				System.out.print( " num picked" + toCheck.size());
				System.out.print( " active: " + newValue);
				
				
				//check all tiles surrounding mouseTile
				for(int i = 0; i < toCheck.size(); i++)
				{
					Tile currentTile = toCheck.get(i);
					//currentTile.TileID = newValue;
					
					//if tile is of same type add its surroundings to toCHeck, if it hasent already been checked. 
					 if(currentTile.TileID == valueToChange &&
							!checkedList.contains(currentTile))
					{
						ArrayList<Tile> newToCheck = map.GetSurroundingTilesOfType(new Point(currentTile.x,currentTile.y),valueToChange);
						toCheck.addAll(newToCheck);
						//add tile to alter list
						toAlter.add(currentTile);
					}
					//add this tile to checked liste
					checkedList.add(currentTile);
				}
				
				for(int i = 0; i < toAlter.size(); i++)
				{
					toAlter.get(i).TileID = newValue;
				}
				
				//save those tiles to checkedList and those of type to toChange list
				//continue for all tiles in toCheck list
				//when done do the same for next tile in toCheck list, if its not in checkedList.
	}
	
	public void mousePressed(MouseEvent evt)
	{
		handleMouseEvent(evt);
	}
	
	public void handleMouseEvent(MouseEvent evt)
	{
		super.handleMouseEvent(evt);
	}
	
	public void mouseDragged(MouseEvent e)
	{
		
	}

}
