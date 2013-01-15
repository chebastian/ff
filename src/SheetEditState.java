
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class SheetEditState extends State{

	public static final int STATE_ID = 40; 
	protected TileSheet sheet;
	protected JMenu mMenu;
	protected TileSheetIO mSheetIO;
	boolean sheetLoaded;
	
	public SheetEditState(TileEdit game) {
		super(STATE_ID, game);
		sheet = null;
		mSheetIO = new TileSheetIO(mGame);
		sheetLoaded = false;
		
		mMenu = new JMenu("Sheet Menu");
		JMenuItem item = new JMenuItem("Save Sheet To File");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TileSheetIO io = new TileSheetIO(mGame);
				ImageManipulator manip = new ImageManipulator();
				BufferedImage img =  ImageManipulator.MakeSheetWithNoSpaces(sheet);
				JFileChooser chooser = new JFileChooser();
				chooser.showDialog(null, "Save");
				
				String file = chooser.getSelectedFile().getAbsolutePath();
				io.SaveImageToSheet(file, img);
				
			}
		});
		mMenu.add(item);
		
		item = new JMenuItem("Set Sheet Params");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mSheetIO.QueryUserForInput();
				sheetLoaded = true;
			}
		});
		mMenu.add(item);
		
		item = new JMenuItem("Load Sheet Img");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				String str = chooser.getSelectedFile().getAbsolutePath();
				sheet = mSheetIO.LoadSheet(str);
			}
		});
		mMenu.add(item);
		
		mGame.mMenuBar.add(mMenu);
		mGame.mFrame.revalidate();
		// TODO Auto-generated constructor stub
	}
	
	public void onEnter()
	{
		mGame.mFrame.addKeyListener(this);
		
		TileSheetIO io = new TileSheetIO(mGame);
		sheet = io.LoadSheet();
		//mGame.SetTileSheet(sh);
		
	}
	
	public void render(Graphics2D g)
	{
		/*g.drawString("Sheet state", 20, 20);
		if(sheet != null)
			mGame.Renderer().RenderTileSheet(g, sheet, 20, 20);*/
		
		if(sheet != null && sheetLoaded)
		{
			renderSheetPreview(g);
		}
	}
	
	protected void renderSheetPreview(Graphics2D g)
	{
		TileSheet sh =  mSheetIO.GetSheet();
		sh.SetImage(sheet.getImg());
		sh.RevalidateSheet();
		mGame.Renderer().RenderTileSheet(g, sh, 20, 20, 5);
	}
	
	public void update(float time)
	{
		
	}
	
	public void keyPressed(KeyEvent evt)
	{
		if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			mGame.States.PopState();
		}
	}
	
	public void onExit()
	{
		mGame.mFrame.removeKeyListener(this);
	}

}
