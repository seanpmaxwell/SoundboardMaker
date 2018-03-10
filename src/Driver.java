import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.File;

public class Driver extends JFrame
{
	private Controller_Side ctrl_side;
	private Soundboard_Side sboard_outer;
	private Soundboard_Inner sboard_inner;
	private JSplitPane pane;
    private File dir;
	
	public Driver() throws LineUnavailableException
	{
        sboard_inner = new Soundboard_Inner();
        sboard_outer = new Soundboard_Side( sboard_inner );
		ctrl_side = new Controller_Side( sboard_outer, sboard_inner );

		Configure_Directory();
		Configure_Overall_Layout();
        Configure_Overall_Container();
	}

    private void Configure_Directory()
    {
        dir = new File( new JFileChooser().getFileSystemView().getDefaultDirectory() + "\\SoundboardMaker");
        if( !dir.exists() )
            dir.mkdir();
    }

    private void Configure_Overall_Layout()
    {
        pane = new JSplitPane();
        pane.setOrientation( JSplitPane.HORIZONTAL_SPLIT );
        pane.add( ctrl_side, JSplitPane.LEFT );
        pane.add( sboard_outer, JSplitPane.RIGHT );
    }

    private void Configure_Overall_Container()
    {
        super.getContentPane().add( pane );
        super.setBounds( 400, 100, 1300, 600 );
        super.setTitle( "Soundboard Maker" );
        super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        super.setVisible( true );
    }
	
	public static void main(String[] args) throws LineUnavailableException
	{
		new Driver();
	}

}

