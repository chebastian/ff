import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.LayoutFocusTraversalPolicy;

import org.omg.CORBA.portable.ApplicationException;

public class TileEditPanel extends JPanel
	{
		public TileEditFrame frame;
	    public TileEditPanel(TileEditFrame frame)
	    {
	        //setBorder(BorderFactory.createLineBorder(Color.black));
	        setFocusable(true);
	        setBorder(BorderFactory.createEmptyBorder(0, 0, frame.WIN_WIDTH, frame.WIN_HEIGHT));
	        setLayout(null);
	        this.frame = frame;
	    }
		
	    @Override
		public Dimension getPreferredSize() {
	        return new Dimension(WIDTH,HEIGHT);
	    }

	    @Override
		public void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D)g;
	        super.paintComponent(g2d);
	        
	        frame.GameDraw(g2d);
	        
	    }
	}