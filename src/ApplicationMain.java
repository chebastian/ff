import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


 class TileEditFrame extends JFrame implements KeyListener{

	/**
	 * @param args
	 */
	
	public static final int WIN_WIDTH = 1024;
	public static final int WIN_HEIGHT = 720;
	TileEditPanel panel;
	JFrame window;
	boolean mKeyArr[];
	boolean mLastKeyArr[];
	long lastTime = 0;
	float elapsedTime = 0.0f;
	TileEdit game;
	
	int DESIRED_FPS = 60;
	int SKIPPED_TICKS = 1000 / DESIRED_FPS;
	int MAX_SKIPPED = 10;
	float MAX_FPS = 1.0f / 30.0f;
	long nextFrame = 0;
	float lastElapsed = 1.0f / 60.0f;
	
	int frame = 0;
	int fps = 0;
	float totalTime = 0.0f;
	
	public TileEditFrame()
	{
		super("TileMap Editor");
		panel = new TileEditPanel(this);
		setSize(WIN_WIDTH,WIN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panel);
		setVisible(true);
		setBackground(Color.black);
		setFocusable(true);
		addKeyListener(this);
		
		mKeyArr = new boolean[0xFFFF00];
		mLastKeyArr = new boolean[0xFFFF00];
		lastTime = 0;
		lastTime = System.currentTimeMillis();
	}
	
	public boolean IsKeyPressed(char k)
	{	
		boolean now = mKeyArr[k];
		boolean last = mLastKeyArr[k];
		if((now) && (!last))
			return true;
		
		return false;
	}
	
	public boolean IsKeyDown(char k)
	{
		return mKeyArr[k];
	}
	
	public void GameStart()
	{
		game = new TileEdit(this);
		addKeyListener(game);
		
		Thread gameThread = createGameThread();
		gameThread.start();
	}
	
	protected Thread createGameThread()
	{
		Thread gameThread = new Thread()
		{
			@Override
			public void run()
			{
				GameLoop();
			}
		};
		
		return gameThread;
	}
	
	public void GameLoop()
	{
		while(true)
		{
			long time = System.currentTimeMillis();
			float elapsed =  CalculateDeltaTime();
			long toWait = CalculateTimeToWaitFromDelta(elapsed);
			
			GameUpdate(elapsed);
			
			repaint();
			
			WaitForTimeout(toWait);
			
			lastTime = time;
		}
	}
	
	public float CalculateDeltaTime()
	{
		float elapsed = (System.currentTimeMillis() - lastTime )/ 1000.0f;
		
		if(elapsed >= MAX_FPS || elapsed < 0.0f)
		{
			elapsed = lastElapsed;
		}
		
		elapsedTime = elapsed;
		
		return elapsed;
	}
	
	public long CalculateTimeToWaitFromDelta(float delta)
	{
		long toWait = 15;
		
		if(delta >= MAX_FPS || delta < 0.0f)
		{
			float current = (System.currentTimeMillis() - lastTime)/1000.0f;
			float desire = MAX_FPS;
			float wait = desire - current;
			toWait = (long)(wait*1000.0);
			lastElapsed = MAX_FPS;
		}
		
		return toWait;
	}
	
	public void WaitForTimeout(long ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void GameUpdate(float time)
	{
		totalTime += time;
		
		game.Update(time);
		
		
		for(int i = 0; i < mKeyArr.length; i++)
		{
			if(mKeyArr[i])
			{
				if(!mLastKeyArr[i])
				{
					mLastKeyArr[i] = mKeyArr[i];
				}
			}
		}
	}
	
	public void GameDraw(Graphics2D g)
	{
		
		if(game == null)
		{
			
		}
		else
		{
			game.Render(g);
		}
		
		updateFpsCounter();
	}
	
	public void updateFpsCounter()
	{
		frame++;
		
		if(totalTime >= 1.0f)
		{
			totalTime = 0.0f;
			fps = frame;
			frame = 0;
		}
	}
	
	public static void main(String[] args) {
		
		TileEditFrame game = new TileEditFrame();
		game.GameStart();
		game.elapsedTime = 0.0f;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		mKeyArr[e.getKeyChar()] = false;
		mLastKeyArr[e.getKeyChar()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.print(e.getKeyChar());
		mKeyArr[e.getKeyChar()] = true;
		
	}
}



