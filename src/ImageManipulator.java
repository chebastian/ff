import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.LinkedList;


public class ImageManipulator {
	
	public ImageManipulator()
	{
		
	}
	
	public ArrayList<BufferedImage> CreateImagesFromSheet(BufferedImage srcImg, int srcW, int srcH, int frameOffsetX, int frameOffsetY)
	{
		int w = srcImg.getWidth();
		int h = srcImg.getHeight();
		int numy = srcImg.getHeight() / (srcH + frameOffsetX);
		int numx = srcImg.getWidth() / (srcW + frameOffsetY);
		
		ArrayList<BufferedImage> imgArr = new ArrayList<>();
		
		int lastx = 0;
		int lasty = 0;
		
		try{
			for(int x = 0; x < numy; x++)
			{
				for(int y = 0; y < numx; y++){
				
					BufferedImage tmpImg = srcImg.getSubimage(lastx , lasty, srcW, srcH);
					imgArr.add(tmpImg);
					
					lastx += srcW + frameOffsetX;
				}
				lasty += srcH + frameOffsetY;
				lastx = 0;
				boolean kuken = true;
			}
		
		}
		catch(Exception e){
			System.out.print(e.getMessage());
		}
	
		return imgArr;
	}
	
	public BufferedImage MakeImageStrip(BufferedImage image, int srcW, int srcH)
	{
		int sy = image.getHeight() / srcH;
		int sx = image.getWidth() / srcW;
		int numtiles = sx*sy;
		
		BufferedImage resultImg = new BufferedImage(numtiles*srcW, srcH,BufferedImage.TYPE_INT_ARGB);
		ArrayList<BufferedImage> imgList = CreateImagesFromSheet(image, srcW, srcH, 0, 0);
		
		for(int i = 0; i < imgList.size(); i++)
		{
			BufferedImage tile = imgList.get(i);
			
			int[] rgbData = new int[tile.getWidth()*tile.getHeight()];
			tile.getRGB(0, 0, tile.getWidth(), tile.getHeight(), rgbData, 0, srcW);
			resultImg.setRGB(i*srcW, 0, srcW, srcH, rgbData, 0, srcW);
		}
		
		ReplaceColorInImage(resultImg, Color.black, Color.magenta);
		
		return resultImg;
	}
	
	public static BufferedImage MakeSheetWithNoSpaces(TileSheet sheet)
	{
		BufferedImage img = new BufferedImage(sheet.w -(sheet.FrameOffsetX*sheet.numx), sheet.h-(sheet.FrameOffsetY*sheet.numy), BufferedImage.TYPE_INT_ARGB);
		ArrayList<BufferedImage> imgList = sheet.GetImagesInSheet();
		
		int srcW = sheet.frameW;
		int srcH = sheet.frameH;
		
		Point index = new Point();
		
		for(int i = 0; i < imgList.size(); i++)
		{
			BufferedImage tile = imgList.get(i);
			
			int[] rgbData = new int[tile.getWidth()*tile.getHeight()];
			tile.getRGB(0, 0, tile.getWidth(), tile.getHeight(), rgbData, 0, srcW);
			img.setRGB(index.x*srcW, index.y*srcH, srcW, srcH, rgbData, 0, srcW);
			
			index.x++;
			
			if(index.x > sheet.numx)
			{
				index.y++;
				index.x = 0;
			}
		}
		
		return img;
	}
	
	public void ReplaceColorInImage(BufferedImage img, Color src, Color newColor)
	{
		int[] rgbData = new int[img.getWidth() * img.getHeight()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgbData, 0, img.getWidth());
		
		for(int i = 0; i < rgbData.length; i++)
		{
			if(rgbData[i] == src.getRGB())
				rgbData[i] = newColor.getRGB();
		}
		
		img.setRGB(0, 0, img.getWidth(), img.getHeight(), rgbData, 0, img.getWidth());
	}
	

}
