import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class TileSheet {
	
	public String filePath;
	int w, h, numx, numy, frameW, frameH;
	public int FrameOffsetX, FrameOffsetY;
	BufferedImage img;
	ArrayList<BufferedImage> mImages;
	
	private boolean mLoaded;
	
	public TileSheet()
	{
		filePath = "Not loaded";
		w = 0; h = 0; numx = 0; numy = 0;
		FrameOffsetX = 0; FrameOffsetY = 0;
		img = null;
	}
	
	public void LoadSheetFromFile(String file, int srcW, int srcH)
	{
		filePath = file;
		mLoaded = false;
		
		try
		{
			img = ImageIO.read(new File(file));
			w = img.getWidth();
			h = img.getHeight();
			frameW = srcW; 
			frameH = srcH;
			
			numx = w / (srcW+FrameOffsetX); 
			numy = h / (srcH+FrameOffsetY);
			
			mLoaded = true;
			mImages = CreateImagesFromSheet();
		}
		catch(IOException e)
		{
			System.out.print(e.getMessage() +  " " + file);
		}
	}
	
	public ArrayList<BufferedImage> CreateImagesFromSheet()
	{
		int srcW = (w / numx)-FrameOffsetX;
		int srcH = (h / numy)-FrameOffsetY;
		ImageManipulator imgManip = new ImageManipulator();
		return imgManip.CreateImagesFromSheet(img, frameW, frameH, FrameOffsetX, FrameOffsetY);
	}
	
	public ArrayList<BufferedImage> GetImagesInSheet()
	{
		if(mImages == null){
			mImages = CreateImagesFromSheet();
		}
		return mImages;
	}
	
	public BufferedImage AsOneDimensionalImage()
	{
		/*int srcW = w / numx;
		int srcH = h / numy;
		*/
		ImageManipulator imgManip = new ImageManipulator();
		return imgManip.MakeImageStrip(this.img, frameW,frameH);
	}
	
	public void RenderTile(Graphics2D g, int num, int x, int y)
	{
		if(num < 0)
			return;
		
		BufferedImage img =  GetImagesInSheet().get(num);
		g.drawImage(img, x, y, null);
	}
	
	public void RenderTile(Graphics2D g, int num, int x, int y, int w, int h)
	{
		BufferedImage img =  GetImagesInSheet().get(num);
		//g.drawImage(img, x, y, null);
		
		g.drawImage(img, x, y, w , h, null);
	}
	
	public static void RenderTile(Graphics2D g, TileSheet sheet, Tile t, int x, int y, int w, int h)
	{
		sheet.RenderTile(g, t.TileID, x, y, w, h);
	}
	
	public void ScaleSheet(float scale)
	{
		for(int i = 0; i < GetImagesInSheet().size(); i++)
		{
			BufferedImage img = GetImagesInSheet().get(i);
			
		}
	}
	
	public boolean ImageIsLoaded()
	{
		return mLoaded;
	}
	
	public final BufferedImage getImg()
	{
		return img;
	}
	
	public void SetImage(BufferedImage _img){
		img = _img;
	}
	
	public void RevalidateSheet()
	{
		w = img.getWidth();
		h = img.getHeight();
		
		numx = w / (frameW+FrameOffsetX);
		numy = h / (frameH+FrameOffsetY);
		
		mImages = CreateImagesFromSheet();
	}
	
	

}
