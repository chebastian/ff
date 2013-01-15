import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class EditState extends State implements ActionListener, MouseMotionListener, MouseWheelListener{

	public static final int EDIT_STATE_ID= 2;
	//TileEdit mGame;
	LayeredTilemap mMap;
	Point mTileMapPositionOffset;
	int Tile_Width, Tile_Height;
	int Level_Width, Level_Height;
	float zoomLevel;
	JButton mTestButton;
	GridLayout grid;
	Container mButtons;
	TileMapPanel mSpritePanel;
	JMenu mEditMenu;
	BrushManager BrushMgr;
	int CameraMoveSpeed;
	float CameraMoveDelta;
	
	LevelGrid mGrid;
	LevelScene mScene;
	private Boolean mViewOutline;
	private Boolean mViewLayerPreview;
	JMenu ViewMenu;
	
	static final int MENU_LEFT = 800;
	static final int MENU_TOP = 30;
	static final int MENU_OFFSET = 5;
	static final int BUTTON_W = 70;
	static final int BUTTON_H = 40;
	
	public EditState(TileEdit game, int w, int h) {
		super(EDIT_STATE_ID,game);	
		
		mGame = game;
		Level_Width = w; Level_Height = h;
		zoomLevel = 1.0f;
		BrushMgr = new BrushManager(mGame,mGame.Brush);
		mViewOutline = true;
		mViewLayerPreview = true;
		CameraMoveSpeed = 500;
		CameraMoveDelta = 1.0f;
	}
	
	public void onEnter()
	{
		mButtons = new Container();
		
		mTestButton = new JButton();
		mTestButton.setBounds(MENU_LEFT, 10, BUTTON_W, BUTTON_H);
		mTestButton.setText("Save");
		mTestButton.setActionCommand("Save");
		mTestButton.addActionListener(this);
		mButtons.add(mTestButton);
		mGame.mFrame.add(mTestButton);
		
		//mGame.mFrame.addMouseListener(this);
		//mGame.mFrame.addKeyListener(this);
		//mGame.mFrame.addMouseWheelListener(this);
		//mGame.mFrame.addMouseMotionListener(this);
		
		
		mSpritePanel = TileMapPanel.CreateMapPanelFromFile(mGame,"../xml/sprites.xml");
		Tile_Width = mSpritePanel.ActiveSheet.frameW;
		Tile_Height = mSpritePanel.ActiveSheet.frameH;
		//mGame.mFrame.addKeyListener(mSpritePanel);
		//mGame.mFrame.addMouseListener(mSpritePanel);
		
		//mMap = TileMap.CreateEmptyMap(Level_Width, Level_Height, 0);
		mMap = new LayeredTilemap(Level_Width, Level_Height, 3);
		mTileMapPositionOffset = new Point(0,0);
		mMap.PositionOffset = new Point(0,0);
		mMap.TileHeight = Tile_Height;
		mMap.TileWidth = Tile_Width;
		
		AddMenuItem();
		
		BrushMgr.getActiveBrush().SetMapToEdit(mMap);
		LoadUtils();
		
		mGame.getCamera().setPosition(mTileMapPositionOffset);
		mGame.getCamera().setSize(Tile_Width*Level_Width,Tile_Height*Level_Height);
		mGame.mFrame.requestFocusInWindow();
		
		mScene = new LevelScene(mGame);
		mScene.RoomGrid = new LevelGrid(mGame);
		mScene.RoomGrid.AddNewRoom("first",mMap, new Point());
		mMap = mScene.RoomGrid.getRoomLastAdded();
		mGame.getCamera().CenterOn(mMap.PositionOffset);
		BrushMgr.getActiveBrush().SetMapToEdit(mMap);
		
		
		mGame.setTileSize(Tile_Width, Tile_Height);
		mGame.SetTileSheet(mSpritePanel.ActiveSheet);
		
		addInputListeners();
	}
	
	public void addInputListeners()
	{
		mGame.mFrame.addMouseListener(this);
		mGame.mFrame.addKeyListener(this);
		mGame.mFrame.addMouseWheelListener(this);
		mGame.mFrame.addMouseMotionListener(this);
		
		mGame.mFrame.addKeyListener(mSpritePanel);
		mGame.mFrame.addMouseListener(mSpritePanel);
	}
	
	public void removeInputListeners()
	{
		mGame.mFrame.removeMouseMotionListener(this);
		mGame.mFrame.removeMouseWheelListener(this);
		mGame.mFrame.removeMouseListener(this);
		mGame.mFrame.removeKeyListener(this);
		mGame.mFrame.removeMouseListener(mSpritePanel);
		mGame.mFrame.removeKeyListener(mSpritePanel);
	}
	
	public void LoadUtils()
	{	
		try
		{
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			Document doc = domBuilder.parse("./xml/EditorUtils.xml");
			
			NodeList nList = doc.getElementsByTagName("brush");
			
			
			for(int i = 0; i < nList.getLength(); i++)
			{
				String name = "";
				String icon = "";
				String keycode = "";
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					boolean kk = false;
					Element e = (Element)nNode;
					
					if(e.hasAttribute("name"))
						name = e.getAttribute("name");
					
					if(e.hasAttribute("icon"))
						icon = e.getAttribute("icon");
					
					if(e.hasAttribute("keycode"))
						keycode = e.getAttribute("keycode");
					
					BrushMgr.AddBrush(name, icon,keycode);
						
				}
			}
		}
		catch(Exception e)
		{	
			
		}
	}
	
	public void onPause()
	{
		removeInputListeners();
		
		BrushMgr.toggleBrushActive(false);
		BrushMgr.getActiveBrush().CleanUp();
		mGame.mMenuBar.remove(mEditMenu);
		mEditMenu.removeAll();
	}
	
	public void onResume()
	{
		mGame.mFrame.addMouseListener(this);
		mGame.mFrame.addKeyListener(this);
		mGame.mFrame.addMouseWheelListener(this);
		mGame.mFrame.addMouseMotionListener(this);
		
		mGame.mFrame.addKeyListener(mSpritePanel);
		mGame.mFrame.addMouseListener(mSpritePanel);
		
		mSpritePanel.ActiveSheet = mGame.GetTileSheet();
		
		AddMenuItem();
		BrushMgr.toggleBrushActive(true);
	}
	
	public void onExit()
	{
		System.out.print("BYEBYE");
		
		mGame.mFrame.removeMouseMotionListener(this);
		mGame.mFrame.removeMouseWheelListener(this);
		mGame.mFrame.removeMouseListener(this);
		mGame.mFrame.removeKeyListener(this);
		mGame.mFrame.removeMouseListener(mSpritePanel);
		mGame.mFrame.removeKeyListener(mSpritePanel);
		mMap = null;
		

		BrushMgr.Cleanup();
		mGame.mMenuBar.remove(mEditMenu);
		mEditMenu.removeAll();
	}
	
	public void AddMenuItem()
	{
		mEditMenu = new JMenu("Edit Menu");
		mGame.mMenuBar.add(mEditMenu);
		JMenuItem item = new JMenuItem("Load Tiles");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//LoadLevel();
				testLoadLevel();
			}
		});
		mEditMenu.add(item);
		
		AddMenuItem(mEditMenu,"Save Room", KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SaveLevel();
				
			}
		});
		
		item = new JMenuItem("Save TileSheet");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				SaveTileSheet();
			}
		});
		mEditMenu.add(item);
		
		AddMenuItem(mEditMenu,"Load Level", KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				testLoadLevelGrid();
				
			}
		});
		
		AddMenuItem(mEditMenu,"Save Map", KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveMap();
				
			}
		});
		
		item = new JMenuItem("Add Room R");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				AddNewRoom(new Point(1,0));
			}
		});
		mEditMenu.add(item);
		
		item = new JMenuItem("Add Room D");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				AddNewRoom(new Point(0,1));
			}
		});
		mEditMenu.add(item);
		
		item = new JMenuItem("Add Room L");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				AddNewRoom(new Point(-1,0));
			}
		});
		mEditMenu.add(item);
		
		item = new JMenuItem("Add Room U");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				AddNewRoom(new Point(0,-1));
			}
		});
		mEditMenu.add(item);
		
		AddMenuItem(mEditMenu, "Add New Area", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					AddNewArea(GetStringFromJOptionPane("Area name"));
				
			}
		});
		
		AddMenuItem(mGame.mMenu, "Load TileSheet", KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					loadSheet("Load Sheet");
			}
		});
		
		addViewMenu();
		mGame.mFrame.revalidate();
	}
	
	public void AddNewArea(String name)
	{
		//Push area brush into brushmgr, which on click pops back to last used brush.
		
		AreaBrush br = new AreaBrush(mGame, mScene);
		BrushMgr.changeBrush(br);
		BrushMgr.getActiveBrush().SetMapToEdit(mMap);
	}
	
	public String GetStringFromJOptionPane(String name)
	{
		JOptionPane pane = new JOptionPane();
		return pane.showInputDialog(name);
	}
	
	public void addViewMenu()
	{
		ViewMenu = new JMenu("View");
		mGame.mMenuBar.add(ViewMenu);
		
		AddMenuItem(ViewMenu, "View Curor Outline", KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				toggleViewCursorOutline();
				
			}
		});
		
		AddMenuItem(ViewMenu, "View Layer Preview", KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				toggleViewLayerPreview();
				
			}
		});
	}
	
	public void toggleViewLayerPreview()
	{
		mViewLayerPreview = !mViewLayerPreview;
		boolean res = mGame.Renderer().RemoveRenderStateByIndex(LayerPreviewRenderer.LAYER_RENDER_ID);
		if(!res)
		{
			mGame.Renderer().AddRenderState(new LayerPreviewRenderer(mGame,mMap));
		}
	}
	
	public void toggleViewCursorOutline()
	{
		 mViewOutline = !mViewOutline;
	}
	
	private void loadSheet(String str) {
		// TODO Auto-generated method stub
		/*TileSheetIO io = new TileSheetIO(mGame);
		
		mSpritePanel.ActiveSheet =  io.LoadSheet();
		mGame.SetTileSheet(mSpritePanel.ActiveSheet);*/
		
		mGame.States.PushState(new SheetEditState(mGame));
	}
	
	public void testLoadLevelGrid()
	{
		JFileChooser fileChooser = new JFileChooser();
		int status = fileChooser.showOpenDialog(mGame.mFrame);
		String path = fileChooser.getSelectedFile().getAbsolutePath();
		LevelIO levelloader = new LevelIO(mGame);
		levelloader.SetWorkingDirectory(fileChooser.getSelectedFile().getParent());
	
		int layer = mScene.RoomGrid.GetSelectedRoom().CurrentLayerIndex();
		mScene.RoomGrid = new LevelGrid(mGame);
		mScene.RoomGrid =  levelloader.LoadLevelGrid(path);
		mScene.RoomGrid.SetMapTileSize(Tile_Width, Tile_Height);
		mMap = mScene.RoomGrid.getRoomLastAdded();
		mMap.SetActiveLayer(layer);
		mMap.SetMapOffset(this.mTileMapPositionOffset);
		mMap.TileHeight = Tile_Height;
		mMap.TileWidth = Tile_Width;
		BrushMgr.CurrentBrush.SetMapToEdit(mScene.RoomGrid.getRoomLastAdded());
	}
	
	public void AddMenuItem(JMenu menu,String name, KeyStroke accelerator ,ActionListener callback)
	{
		JMenuItem item = new JMenuItem(name);
		item.setAccelerator(accelerator);
		item.addActionListener(callback);
		menu.add(item);
	}
	
	public void AddNewRoom(Point p)
	{
		mScene.RoomGrid.AddAdjecentRoom(mScene.RoomGrid.getRoomLastAdded(), new LayeredTilemap(Level_Width, Level_Height, 3), p);
		mMap = mScene.RoomGrid.getRoomLastAdded();
		
		BrushMgr.getActiveBrush().SetMapToEdit(mMap);
		
		mGame.getCamera().CenterOn(mMap.PositionOffset);
	}
	
	public void SaveLevel()
	{
		System.out.print("SAVED");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Room");
		int stat = fileChooser.showSaveDialog(mGame.mFrame);
		String path = new String();
		
		path = fileChooser.getSelectedFile().getAbsolutePath();
		
		if(!path.endsWith(".xml"))
			path += ".xml";
		
		LevelIO levelwriter = new LevelIO(mGame);
		levelwriter.SaveTileMap(path, mMap);
		//levelwriter.SaveLevelGrid(fileChooser.getSelectedFile().getName(), fileChooser.getSelectedFile().getParentFile().getAbsolutePath(), testGrid);
	
	}
	
	public void SaveMap()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Map");
		int stat = fileChooser.showSaveDialog(mGame.mFrame);
		String path = new String();
		
		path = fileChooser.getSelectedFile().getAbsolutePath();
		
		if(!path.endsWith(".xml"))
			path += ".xml";
		
		LevelIO levelwriter = new LevelIO(mGame);
		levelwriter.SaveLevelGrid(fileChooser.getSelectedFile().getName(), fileChooser.getSelectedFile().getParentFile().getAbsolutePath(), mScene.RoomGrid);
	}
	
	public void LoadLevel()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Load Room");
		int status = fileChooser.showOpenDialog(mGame.mFrame);
		String path = fileChooser.getSelectedFile().getAbsolutePath();
		LevelIO levelloader = new LevelIO(mGame);
		mMap = levelloader.LoadLevel(path);
		mMap.SetMapOffset(this.mTileMapPositionOffset);
		mMap.TileHeight = Tile_Height;
		mMap.TileWidth = Tile_Width;
		BrushMgr.CurrentBrush.SetMapToEdit(mMap);
	}
	
	public void testLoadLevel()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Load Map");
		int status = fileChooser.showOpenDialog(mGame.mFrame);
		String path = fileChooser.getSelectedFile().getAbsolutePath();
		LevelIO levelloader = new LevelIO(mGame);
		LayeredTilemap newlevel = levelloader.LoadLevel(path);
		newlevel.SetMapOffset(mMap.PositionOffset);
		mScene.RoomGrid.ChangeRoomAtIndex(mScene.RoomGrid.GetRoomIndex(mMap), newlevel);
		mMap = newlevel;
		mMap.TileHeight = Tile_Height;
		mMap.TileWidth = Tile_Width;
		BrushMgr.CurrentBrush.SetMapToEdit(mMap);
	}
	
	public void SaveTileSheet()
	{
		JFileChooser fileChooser = new JFileChooser();
		int status = fileChooser.showSaveDialog(mGame.mFrame);
		String path = fileChooser.getSelectedFile().getAbsolutePath();
		SaveTileSheetTo(path);
	}
	
	public void SaveTileSheetTo(String path)
	{
		BufferedImage img = mSpritePanel.ActiveSheet.AsOneDimensionalImage();
		
		try
		{
			ImageIO.write(img, "png", new File(path));
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void clearCurrentLevel()
	{
		mMap.ClearMap();
	}
	
	public void setNewTileSheet()
	{
		JFileChooser fileChooser = new JFileChooser();
		int status = fileChooser.showOpenDialog(mGame.mFrame);
	}
	
	public void update(float time)
	{
		if(mGame.mFrame.IsKeyPressed('a'))
		{
			BrushMgr.changeBrush(new FillBrush(mGame));
			BrushMgr.getActiveBrush().SetMapToEdit(mMap);
		}
		if(mGame.mFrame.IsKeyPressed('s'))
		{
			BrushMgr.changeBrush(new Brush(mGame));
			BrushMgr.getActiveBrush().SetMapToEdit(mMap);
		}
		if(mGame.mFrame.IsKeyPressed('d'))
		{
			BrushMgr.changeBrush(new SolidBrush(mGame));
			BrushMgr.getActiveBrush().SetMapToEdit(mMap);
		}
		
		CameraMoveDelta = CameraMoveSpeed * time;
		
		mSpritePanel.SetHighLightedNum(BrushMgr.getActiveBrush().ActiveValue);
		
		BrushMgr.toggleBrushActive(!mSpritePanel.active);
	}
	
	public void render(Graphics2D g)
	{
		Point newpos = mGame.getCamera().ScreenToWorld(mTileMapPositionOffset);
		//RenderLevelGrid(g, newpos.x, newpos.y, testGrid);
		mGame.Renderer().RenderLevelGrid(g, newpos.x, newpos.y, 1.0f, mScene.RoomGrid);
		if(mViewLayerPreview){
			//RenderLayersPreview(g, newpos.x,newpos.y, mMap);
			mGame.Renderer().renderLayerPreview(g, newpos.x, newpos.y, mMap);
		}
		
		//mGame.Renderer().renderStates(g);
		
		if(mViewOutline)
			RenderHelperGrid(g, 32, 32);
		
		
		mSpritePanel.Render(g);
		g.drawImage(mSpritePanel.GetSelectedImg(),null, mGame.Brush.GetScreenPos().x + (int)(mSpritePanel.ActiveSheet.frameW*0.5),
				mGame.Brush.GetScreenPos().y + (int)(mSpritePanel.ActiveSheet.frameH*0.5));
		
		
		BrushMgr.getActiveBrush().render(g);
		BrushMgr.RenderBrushList(g, 0, 0);
		
		mScene.renderDoors(g, newpos.x, newpos.y);
	
	}
	
	public void RenderHelperGrid(Graphics2D g, int gridW, int gridH)
	{
		Point startX = new Point(BrushMgr.getActiveBrush().GetScreenPos());
		Point camPos = BrushMgr.getActiveBrush().GetPos();
		
		Point worldPos = new Point(BrushMgr.getActiveBrush().GetPos());
		worldPos = mGame.getCamera().ScreenToWorld(worldPos);
		g.drawString(startX.toString(), 10, 100);
		int mod = camPos.x%Tile_Width;
		int modY = camPos.y%Tile_Height;
		startX.x -= mod;
		startX.y -= modY;
		//startX = mGame.getCamera().WorldToScreen(startX);
		g.setColor(Color.red);
		g.drawRect(startX.x, startX.y, Tile_Width, Tile_Height);
	}
	
	
	public void ChangeTileID(Tile t, int newId)
	{
		if(!mSpritePanel.active)
			t.TileID = newId;
	}
	
	public void mousePressed(MouseEvent e)
	{

	}
	
	public void mouseClicked(MouseEvent e)
	{

	}
	
	/*public Tile GetTileAtPos(int x, int y)
	{
		Tile choosen = new Tile(0,0,0);
		Point index = MouseToTileIndex(x, y);
		
		choosen = mMap.GetTile(index.x, index.y);
		
		return choosen;
	}*/
	
	/*public Point MouseToTileIndex(int x, int y)
	{
		Point pos = new Point(0,0);
		pos.x =  (x - mTileMapPositionOffset.x) / (int)(Tile_Width*zoomLevel);
		pos.y = (y - mTileMapPositionOffset.y) / (int)(Tile_Height*zoomLevel);
		return pos;
	}*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if("Test".equals(e.getActionCommand()))
		{
		//	GetTileAtPos(0, 0).TileID = 1;
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		
		if(true)
		{
			int notch = -event.getWheelRotation();
			//zoomLevel += 0.2 * notch;
			
			//mMap.Scale = zoomLevel;
		}
	}
	
	public void keyPressed(KeyEvent event)
	{
		if(event.isShiftDown())
		{
			if(event.getKeyCode() == KeyEvent.VK_LEFT)
			{
				mMap.SetActiveLayer(mMap.CurrentLayerIndex()-1);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				mMap.SetActiveLayer(mMap.CurrentLayerIndex()+1);
			}
		}
		else if(event.isAltDown())
		{
			mSpritePanel.ToggleActive(mGame, true);
		}
		else
		{
			int movespeed = (int)CameraMoveDelta;
			if(event.getKeyCode() == KeyEvent.VK_LEFT)
			{
				mGame.getCamera().Translate(-movespeed, 0);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				mGame.getCamera().Translate(movespeed, 0);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_UP)
			{
				mGame.getCamera().Translate(0, -movespeed);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_DOWN)
			{
				mGame.getCamera().Translate(0, 	movespeed);
			}
		}
	}
	
	public boolean MiniatureMapIsClicked(MouseEvent e)
	{
		Point pos = e.getPoint();
		
		
		return false;
	}

}
