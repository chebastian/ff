import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


public class BrushManager {
	
	TileEdit mGame;
	Brush CurrentBrush;
	Brush LastBrush;
	private Map<String, BufferedImage> mBrushIcons;
	public boolean BrushActive;
	
	BrushManager(TileEdit game, Brush active)
	{
		mGame = game;
		LastBrush = null;
		CurrentBrush = active;
		CurrentBrush.activated = true;
		mGame.mFrame.addMouseListener(CurrentBrush);
		mGame.mFrame.addMouseMotionListener(CurrentBrush);
		mBrushIcons = new Hashtable<String, BufferedImage>();
		BrushActive = true;
	}
	
	public Brush getActiveBrush()
	{
		return CurrentBrush;
	}
	
	public void changeBrush(Brush br)
	{
		int currTileVal = CurrentBrush.ActiveValue;
		CurrentBrush.CleanUp();
		mGame.mFrame.removeMouseListener(CurrentBrush);
		mGame.mFrame.removeMouseMotionListener(CurrentBrush);
		
		CurrentBrush = br;
		mGame.Brush = CurrentBrush;
		CurrentBrush.activated = true;
		BrushActive = true;
		CurrentBrush.SetActiveValue(currTileVal);
		mGame.mFrame.addMouseListener(CurrentBrush);
		mGame.mFrame.addMouseMotionListener(CurrentBrush);
	}
	
	public void toggleBrushActive(boolean active)
	{
		if(active && !BrushActive)
		{
			mGame.mFrame.addMouseListener(CurrentBrush);
			BrushActive = true;
		}
		else if(!active && BrushActive)
		{
			mGame.mFrame.removeMouseListener(CurrentBrush);
			this.BrushActive = false;
			this.BrushActive = new Boolean(false);
			if(this.BrushActive == true)
			{
				boolean lololo = false;
				BrushActive = lololo;
			}
		}
	}
	
	public void setBrushActive(boolean active)
	{
		BrushActive = active;
	}
	
	public void RevertToLast()
	{
		//mGame.mFrame.removeMouseListener(LastBrush);
		//mGame.mFrame.removeMouseMotionListener(LastBrush);
		
		//CurrentBrush = next;
	}
	
	public void Cleanup()
	{
		CurrentBrush.CleanUp();
		mGame.mFrame.removeMouseListener(CurrentBrush);
		mGame.mFrame.removeMouseMotionListener(CurrentBrush);
		//CurrentBrush = null;
		//mGame.Brush = null;
	}
	
	public void RenderBrushList(Graphics2D g, int x, int y)
	{
		int counter = 0;
		int nextX = x;
		for(Map.Entry<String, BufferedImage> entry : mBrushIcons.entrySet())
		{
			
			g.drawImage(entry.getValue(), nextX, y, null);
			
			if(entry.getKey().equalsIgnoreCase(CurrentBrush.Name()))
			{
				g.setColor(Color.red);
				g.drawRect(nextX, y, entry.getValue().getWidth(), entry.getValue().getHeight());	
			}
			
			nextX += entry.getValue().getWidth();
			counter++;
		}
		
	}
	
	public void AddBrush(String name, String icon, String keycode)
	{
		if(!mBrushIcons.containsKey(name))
		{
			try
			{
				BufferedImage img = ImageIO.read(new File(icon));
				mBrushIcons.put(name, img);
			}
			catch(Exception e)
			{
				System.out.print(e.getMessage() + icon);
			}
		}
	}
	

}
