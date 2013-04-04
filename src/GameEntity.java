import java.awt.Point;


public class GameEntity {
	
	protected String mName;
	protected Point mPosition;
	protected boolean Solid;

	public final boolean isSolid() {
		return Solid;
	}


	public void setSolid(boolean solid) {
		Solid = solid;
	}


	public GameEntity(String name, Point pos)
	{
		mPosition = pos;
		mName = name;
	}
	

	public final String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public final Point getPosition() {
		return mPosition;
	}

	public void setPosition(Point mPosition) {
		this.mPosition = mPosition;
	}
}
