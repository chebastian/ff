import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TileMapPanel extends TileMap  implements MouseListener, KeyListener{
	
	TileSheet ActiveSheet;
	int highlighted_num; 
	Point render_position;
	boolean active;
	TileEdit mGame;
	int row_width;
	
	TileMap easeOfAccessMap;
	
	protected TileMapPanel(TileEdit game)
	{
		super(0,0);
		highlighted_num = 0;
		render_position = new Point();
		row_width = 32;
		
		mGame = game;
		active = false;
		easeOfAccessMap = new TileMap(3, 3);
	}
	
	public static TileMapPanel CreateMapPanelFromFile(TileEdit game, String filePath)
	{
		TileMapPanel panel = new TileMapPanel(game);
		
		try
		{
			System.out.println("Working Directory = " +
			           System.getProperty("user.dir"));
			
			File xmlFile = new File("./xml/spritesheets.xml");
			if(xmlFile.exists())
			{
				boolean bulle = false;
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docbuilder.parse(xmlFile);
			
			TileMapPanel.GetSpritesFromDocument(doc, panel);

		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
			
		return panel;
	}
	
	public static String GetElementAttribute(org.w3c.dom.Document doc, String elem, String attribute, int index)
	{
		NodeList nodes = doc.getElementsByTagName(elem);
		
		if(index >= nodes.getLength())
			return "";
		
		int i = index;
		Node node = nodes.item(i);
		if(node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element)node;
			return element.getAttribute(attribute);
		}
		
		return "";
	}
	
	public static String GetElementAttribute(NodeList elem, String attribute, int index)
	{	
		if(index >= elem.getLength())
			return "";
		
		int i = index;
		Node node = elem.item(i);
		if(node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element)node;
			return element.getAttribute(attribute);
		}
		
		return "";
	}
	
	public static void GetSpritesFromDocument(org.w3c.dom.Document doc, TileMapPanel panel)
	{
		NodeList nodes = doc.getElementsByTagName("sprite");
		
		for(int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				Element element = (Element)node;
				int srcW = Integer.parseInt(element.getAttribute("srcW"));
				int srcH = Integer.parseInt(element.getAttribute("srcH"));
				String path = element.getAttribute("file");
				int frameOffsetX = 0;
				int frameOffsetY = 0;
				if(element.hasAttribute("frameOffsetX"))
				{
					frameOffsetX = Integer.parseInt(element.getAttribute("frameOffsetX"));
				}
				if(element.hasAttribute("frameOffsetY"))
				{
					frameOffsetY = Integer.parseInt(element.getAttribute("frameOffsetY"));
				}
				
				TileSheet ts = new TileSheet();
				ts.FrameOffsetX = frameOffsetX;
				ts.FrameOffsetY = frameOffsetY;
				ts.LoadSheetFromFile(path, srcW, srcH);
				
				panel.ActiveSheet = ts;
			}
		}
	}
	
	public void ToggleActive(TileEdit game, boolean val)
	{
		if(!active && val == true)
		{
			int ix = GetHightlightedNum() % ActiveSheet.numy;
			int iy = GetHightlightedNum() / ActiveSheet.numx;
			
			Point center = mGame.getCamera().ScreenToWorld(mGame.getCamera().getCenter());
			render_position.x = ActiveSheet.frameW;
			render_position.y = ActiveSheet.frameH;
			
		}
		
		active = val;
	}
	
	public Point GetScreenPosOfHightlighetTile()
	{
		Point p = new Point();
		
		int ix = GetHightlightedNum() % ActiveSheet.numy;
		int iy = GetHightlightedNum() / ActiveSheet.numx;
		
		return p;
	}
	
	public void Render(Graphics2D g)
	{
		if(active)
		{
			//Point renderp = mGame.getCamera().WorldToScreen(render_position);
			RenderPanel(g, render_position.x, render_position.y);
			//RenderPanel(g, renderp.x, renderp.y);
		}
	}
	
	public void RenderPanel(Graphics2D g, int x, int y)
	{
		ArrayList<BufferedImage> imgs = ActiveSheet.GetImagesInSheet();
		Point pos = new Point(x,y);
		int yindex = 0;
		for(int i = 0; i < imgs.size(); i++)
		{
			if(i >= ActiveSheet.numx*yindex)
			{
				yindex++;
				pos.x = x;
			}
			pos.y = y + (imgs.get(i).getHeight()*yindex);
			g.drawImage(imgs.get(i),pos.x, pos.y,null);
			
			g.setColor(Color.white);
			g.drawRect(pos.x, pos.y, ActiveSheet.frameW, ActiveSheet.frameH);
			
			pos.x += (imgs.get(i).getWidth());
		}
	}
			
	
	private static String getTagValue(String tag, Element elem)
	{
		NodeList nodes = elem.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node)nodes.item(0);
		
		return node.getNodeValue();
	}
	
	public void HighlightTileAtPos(Point pos)
	{
		Point position = new Point();
		position.x = (pos.x - render_position.x) / ActiveSheet.frameW;
		position.y = (pos.y - render_position.y) / ActiveSheet.frameH; 
		int index = position.x + (position.y * (ActiveSheet.numx)-1);
		
		index -= (ActiveSheet.numx-1);
		if(index < 0)
			index = 0;
		else if(index >= ActiveSheet.GetImagesInSheet().size())
			index = ActiveSheet.GetImagesInSheet().size()-1;
		
		highlighted_num = index;
		mGame.Brush.ActiveValue = highlighted_num;
	}
	
	private int GetTileIndexAtPos(Point pos)
	{
		Point position = new Point();
		position.x = (pos.x - render_position.x) / ActiveSheet.frameW;
		position.y = (pos.y - render_position.y) / ActiveSheet.frameH; 
		int index = position.x + (position.y * (ActiveSheet.numx)-1);
		
		index -= (ActiveSheet.numx-1);
		if(index < 0)
			index = 0;
		else if(index >= ActiveSheet.GetImagesInSheet().size())
			index = ActiveSheet.GetImagesInSheet().size()-1;
		
		return index;
	}
	
	private void HighlightTileAtIndex(int num)
	{
		highlighted_num = num;
	}
	
	public BufferedImage GetSelectedImg()
	{
		BufferedImage img = ActiveSheet.GetImagesInSheet().get(highlighted_num);
			
		return img;
	}
	
	public int GetHightlightedNum()
	{
		return highlighted_num;
	}

	public void SetHighLightedNum(int num)
	{
		highlighted_num = num;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(active)
			HighlightTileAtPos(mGame.Brush.GetScreenPos());
	
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		if(active)
		{
			String keys = "qweasdzxc";
			for(int i = 0; i < keys.length(); i++)
			{
				if(event.getKeyChar() == keys.charAt(i))
					highlighted_num = easeOfAccessMap.GetTile(i).TileID;
			}
		}
		
		if(event.getKeyCode() == KeyEvent.VK_ALT && !active)
		{
			ToggleActive(mGame, true);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getKeyCode() == KeyEvent.VK_ALT)
		{
			ToggleActive(mGame, false);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_1 && event.isAltDown())
		{
			int index = GetTileIndexAtPos( mGame.Brush.GetPos() );
			int[] highlighted = new int[9];
			int rowW = 3;
			
			try
			{
				for(int i = 0; i < rowW; i++)
				{
					highlighted[i] = index + i;
					highlighted[i+rowW] = index + i+ActiveSheet.numx;
					highlighted[i+rowW*2] = index + i+(ActiveSheet.numx*2);
				}	
				
				System.out.print(highlighted);
				easeOfAccessMap = new TileMap(3,3,highlighted);
			}
			catch(Exception e)
			{
				System.out.print(e.getMessage());	
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
