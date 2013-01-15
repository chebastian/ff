import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class CreateLevelState extends State implements ActionListener {

	
	int desiredWidth, desiredHeight;
	JTextField WidthField, HeightField, NameField;
	JLabel WidthLable, HeightLable;
	JButton CreateButton;
	String STR_LVL_W = "Level Width: ";
	String STR_LVL_H = "Level Height: ";
	String CREATE_BUTTON_LABLE = "Create";
	TileEdit mGame;
	Container fields;
	
	static final int MAX_WIDTH = 20;
	static final int MAX_HEIGHT = 20;
	
	
	public CreateLevelState(TileEdit game, int stateId){
		super(stateId,game);
		mGame = game;
		
	}
	
	public void onEnter()
	{
		int fieldsz = 25;
		WidthField = new JTextField();
		WidthField.setText("W");
		HeightField = new JTextField();
		HeightField.setText("H");
		WidthField.setBounds(fieldsz, 20, fieldsz,fieldsz);
		HeightField.setBounds(fieldsz*3,20, fieldsz, fieldsz);
		
		mGame.mFrame.add(WidthField);
		mGame.mFrame.add(HeightField);
		
		CreateButton = new JButton();
		CreateButton.setText(CREATE_BUTTON_LABLE);
		CreateButton.addActionListener(this);
		CreateButton.setActionCommand(CREATE_BUTTON_LABLE);
		CreateButton.setBounds(20, fieldsz*2, 80, 20);
		mGame.mFrame.add(CreateButton);
		
		WidthLable = new JLabel(STR_LVL_W);
		HeightLable = new JLabel(STR_LVL_H);
	}
	
	public void onExit()
	{
		mGame.mFrame.remove(WidthField);
		mGame.mFrame.remove(HeightField);
		mGame.mFrame.remove(CreateButton);
	}
	
	public void render(Graphics2D g)
	{
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() == CREATE_BUTTON_LABLE)
		{
			int w = Integer.parseInt( WidthField.getText() );
			int h = Integer.parseInt( HeightField.getText() );
			
			if(w > MAX_WIDTH || h > MAX_HEIGHT)
			{
				w = MAX_WIDTH; h = MAX_HEIGHT;
			}
			
			EditState state = new EditState(mGame,w,h);
			mGame.States.ChangeState(state);
		}
		
	}

}
