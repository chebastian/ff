import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class TileEdit implements KeyListener {

	TileEditFrame mFrame;
	Brush Brush;
	StateManager States;
	static final int MAIN_STATE_MGR = 0;
	
	JMenuBar mMenuBar;
	JMenu mMenu;
	Point MousePosition;
	
	protected TileEditorCamera mCamera;
	protected TileSheet mSheet;
	protected Renderer mRenderer;
	
	public final String LEVEL_TEMPLATE_DIR = "C:/Users/Sebastian/Documents/aaaa/templates/";
	public int TILE_WIDTH, TILE_HEIGHT;
	
	public TileEdit(TileEditFrame frame)
	{
		mFrame = frame;
		this.Brush = new Brush(this);
		this.mFrame.addMouseListener(this.Brush);
		this.mFrame.addMouseMotionListener(this.Brush);
		States = new StateManager(MAIN_STATE_MGR,this);
		setupMenuBar();
		mCamera = new TileEditorCamera(new Point(), 1024,720);
		
		mRenderer = new Renderer(this);
		States.ChangeState(new EditState(this,15,10));
	}
	
	public void setupMenuBar()
	{
		mMenuBar = new JMenuBar();
		JMenuItem menuItem;
		
		mMenu = new JMenu("Meny");
		mMenu.setMnemonic(KeyEvent.VK_A);
		mMenuBar.add(mMenu);
		
		menuItem = new JMenuItem("New Level");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewLevel();
			}
		});
		
		mMenu.add(menuItem);
		
		mFrame.setJMenuBar(mMenuBar);
		mFrame.revalidate();
	}
	
	public void createNewLevel()
	{
		States.ChangeState(new CreateLevelState(this,0));
	}
	
	public void Render(Graphics2D g)
	{
		if(mFrame.IsKeyDown('a'))
			g.drawString("Tile Edit", 10.0f, 100.0f);
		
		States.CurrentState().render(g);
			
	}
	
	public void Update(float time)
	{
		States.CurrentState().update(time);
	}
	
	public TileEditorCamera getCamera()
	{
		return mCamera;
	}
	
	public void setTileSize(int w, int h)
	{
		TILE_WIDTH = w; TILE_HEIGHT = h;
	}
	
	public final int TileWidth()
	{
		return TILE_WIDTH;
	}
	
	public final int TileHeight()
	{
		return TILE_HEIGHT;
	}
	
	public final TileSheet GetTileSheet()
	{
		return mSheet;
	}
	
	public void SetTileSheet(TileSheet sheet)
	{
		mSheet = sheet;
	}
	
	public Renderer Renderer()
	{
		return mRenderer;
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
