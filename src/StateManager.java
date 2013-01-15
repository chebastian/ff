import java.awt.List;
import java.util.ArrayList;


public class StateManager {
	
	ArrayList<State> States;
	State mCurrentState;
	TileEdit mGame;
	public StateManager(int id, TileEdit game)
	{
		States = new ArrayList<>();
		mCurrentState = new State(0,game);
	}
	
	public void PushState(State state)
	{
		mCurrentState.onPause();
		States.add(state);
		
		state.onEnter();
		mCurrentState = state;
		
	}
	
	public void PopState()
	{
		RemoveCurrentState();
		CurrentState().onResume();
	}

	public void ChangeState(State newState)
	{
		RemoveCurrentState();
		States.add(newState);
		mCurrentState = null;
		mCurrentState = newState;
		newState.onEnter();
	}
	
	public void RemoveCurrentState()
	{
		if(States.size() > 0)
		{
			State current = States.get(States.size()-1);
			current.onExit();
			current = null;
			States.remove(States.size()-1);
		
			
		}
	}
	
	public boolean CurrentStateEquals(State state)
	{
		return state.EqualsState(mCurrentState);
	}
	
	public State LastStateIn()
	{
		State state = new State(0,mGame);
		if(States.size() > 0)
			state = States.get(States.size()-1);
		
		return state;
	}
	
	public State CurrentState()
	{
		return States.get(States.size()-1);
	}
}
