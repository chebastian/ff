import java.awt.Graphics2D;


public class RenderState {
	
	protected TileEdit mGame;
	protected int mId;
	
	public RenderState(TileEdit game)
	{
		mGame = game;
		mId = 0;
	}
	
	public void onEnter()
	{
		
		
	}
	
	public void update(float time)
	{
		
	}
	
	public void render(Graphics2D g, TileEditorCamera cam)
	{
		
	}
	
	public void onExit()
	{
		
	}
	
	public final int getID()
	{
		return mId;
	}

}
