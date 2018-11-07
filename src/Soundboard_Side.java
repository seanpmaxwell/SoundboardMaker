import java.awt.*;
import javax.swing.*;

public class Soundboard_Side extends JPanel
{
	private JLabel title;
	private String title_portion;
	private JScrollPane scroller;
	private SoundboardInner sboard_inner;
	
	public Soundboard_Side( Soundboard_Inner sboard_inner )
    {
        this.sboard_inner = sboard_inner;

        title_portion = "Press 'Enter' to stop sound      PROJECT_NAME: ";
        title = new JLabel( title_portion + "New Project" );
        title.setHorizontalAlignment( JLabel.CENTER );
        scroller = new JScrollPane(sboard_inner, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );

		super.setBackground( Color.white );
		super.setLayout( new BorderLayout() );
		super.add( title, BorderLayout.NORTH );
		super.add( scroller, BorderLayout.CENTER );
	}		

	public void Set_Title()
    {
		super.remove( title );
		title.setText( title_portion + sboard_inner.Get_Project_Title() );
		super.add( title, BorderLayout.NORTH );
		super.repaint();
		super.revalidate();
	}
}
