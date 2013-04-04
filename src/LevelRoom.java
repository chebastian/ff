import java.util.LinkedList;
import java.util.List;


public class LevelRoom {

	LayeredTilemap Map;
	LinkedList<GameEntity> Entitys;
	
	public LevelRoom(LayeredTilemap map)
	{
		Map = map;
		Entitys = new LinkedList<>();
	}
	
	public void SetRoomName(String name)
	{
		Map.SetName(name);
	}
	
	public String RoomName()
	{
		return Map.getName();
	}
}
