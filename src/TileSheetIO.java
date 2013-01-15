import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class TileSheetIO {
	
	protected TileEdit mGame;
	protected TileSheet mSheet;
	
	public TileSheetIO(TileEdit game)
	{
		mGame = game;
		mSheet = new TileSheet();
	}
	
	public TileSheet LoadSheet(String filename)
	{
		TileSheet sh = new TileSheet();
		sh.FrameOffsetX = mSheet.FrameOffsetX;
		sh.FrameOffsetY = mSheet.FrameOffsetY;
		sh.LoadSheetFromFile(filename, mSheet.frameW, mSheet.frameH);
		
		return sh;
	}
	public TileSheet LoadSheet()
	{
		TileSheet sheet = new TileSheet();
		int sw = 0, sh = 0;
		int offsetx = 0; 
		int offsety = 0;
		
		String path = "";
		
		JFileChooser chooser = new JFileChooser("../");
		chooser.showOpenDialog(null);
		path = chooser.getSelectedFile().getAbsolutePath();
		sw = GetIntValueNamed("Tile Width");
		sh = GetIntValueNamed("Tile Height");
		
		int offset = JOptionPane.showConfirmDialog(null, "Set Offset");
		
		if(offset == 0)
		{	
			offsetx = GetIntValueNamed("X: Offset");
			offsety = GetIntValueNamed("Y Offset");
		}
		sheet.FrameOffsetX = offsetx;
		sheet.FrameOffsetY = offsety;
		sheet.LoadSheetFromFile(path, sw, sh);
		
		String msg = "Correct: " + "w: " + Integer.toString(sw);
		msg += " " + "h: " + Integer.toString(sh) + System.lineSeparator();
		msg += " " + "offsetX: " + Integer.toString(offsetx) + System.lineSeparator();
		msg += " " + "offsetY: " + Integer.toString(offsetx) + System.lineSeparator();
		msg += " " + "file: " + path;
		int done = JOptionPane.showConfirmDialog(null, msg, "Sheet", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(done != 0)
		{
			sheet = LoadSheet();
		}
		
		return sheet;
	}
	
	public void QueryUserForInput()
	{
		int sw = 0, sh = 0;
		int offsetx = 0; 
		int offsety = 0;
		sw = GetIntValueNamed("Tile Width");
		sh = GetIntValueNamed("Tile Height");
		
		int offset = JOptionPane.showConfirmDialog(null, "Set Offset");
		
		if(offset == 0)
		{	
			offsetx = GetIntValueNamed("X: Offset");
			offsety = GetIntValueNamed("Y Offset");
		}
		
		String msg = "Correct: " + "w: " + Integer.toString(sw);
		msg += " " + "h: " + Integer.toString(sh) + System.lineSeparator();
		msg += " " + "offsetX: " + Integer.toString(offsetx) + System.lineSeparator();
		msg += " " + "offsetY: " + Integer.toString(offsetx) + System.lineSeparator();
		int done = JOptionPane.showConfirmDialog(null, msg, "Sheet", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(done != 0)
		{
			QueryUserForInput();
			return;
		}
		
		mSheet.frameH = sh;
		mSheet.frameW = sw;
		mSheet.FrameOffsetX = offsetx;
		mSheet.FrameOffsetY = offsety;
	}
	
	public static void SaveImageToSheet(String name, BufferedImage img)
	{
		try{
			ImageIO.write(img, "png", new File(name));
		}
		catch(Exception e){
			
		}
	}
	
	protected int GetIntValueNamed(String name)
	{
		int val = 0;
		
		String str = JOptionPane.showInputDialog(name);
		
		boolean containsNumber = str.matches("[\\d]{1,}");
		while(!containsNumber){
			JOptionPane.showMessageDialog(null, str + " is not a valid number");
			str = JOptionPane.showInputDialog(name);
			containsNumber = str.matches("[\\d]{1,}");
			if(str == null)
				return -1;
		}
		
		val = Integer.parseInt(str);
		return val;
	}
	
	public final TileSheet GetSheet()
	{
		return mSheet;
	}
}
