import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class State implements KeyListener, MouseListener{
	
	protected int mStateId;
	protected TileEdit mGame;
	public State(int stateId, TileEdit game)
	{
		mStateId = stateId;
		mGame = game;
	}
	
	public void onEnter()
	{
		
	}
	
	public void render(Graphics2D g)
	{
		
	}
	
	public void update(float time)
	{
		
	}
	
	public void onPause()
	{
		
	}
	
	public void onResume()
	{
		
	}
	
	public void onExit()
	{
		
	}
	
	public boolean EqualsState(State state)
	{
		return state.mStateId == mStateId;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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

}
