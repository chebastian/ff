import java.awt.Point;


public class AreaBrush extends Brush {

	protected String mAreaName;
	protected int mAreaValue;
	protected LevelScene mScene;
	
	public AreaBrush(TileEdit game, LevelScene scene) {
		super(game);
		mScene = scene;
		// TODO Auto-generated constructor stub
		mAreaName  = ""; 
		mAreaValue = 0;
		this.BrushKeyCode = "C";
		this.BrushName = "Area Brush";
	}
	
	public void PaintTile(Point pos)
	{
		mScene.AddNewDoor(pos, mAreaName, PaintSolid);
	}

}
