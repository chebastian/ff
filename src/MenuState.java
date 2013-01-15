import java.awt.Graphics2D;


public class MenuState extends State {

	static final int MENU_STATE_ID = 3;
	public MenuState(TileEdit game) {
		// TODO Auto-generated constructor stub
		super(MENU_STATE_ID,game);
		mGame = game;
		mGame.mFrame.addKeyListener(this);
	}
	
	
	public void update(float time)
	{
		if(mGame.mFrame.IsKeyPressed('s'))
			mGame.States.PopState();
	}
	
	public void render(Graphics2D g)
	{
		g.drawString("MENUUU", 30, 30);
	}

}
