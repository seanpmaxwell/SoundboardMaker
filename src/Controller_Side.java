import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Controller_Side extends JPanel implements ActionListener
{
	private Soundboard_Inner sboard_inner;
	private Soundboard_Side sboard_outer;
    private Title_Box title_box;

    /* Import the pics for the control buttons */
    private ImageIcon project_ctrl_label;
    private ImageIcon sound_ctrl_label;
	private ImageIcon load_icon;
	private ImageIcon save_icon;
	private ImageIcon new_project_icon;
	private ImageIcon new_sound_icon;
    private ImageIcon sort_icon;
    private ImageIcon rename_icon;


    /* The buttons for controlling the current project
    (not the controls for the current sound */
	private JButton load_project;
	private JButton save_project;
	private JButton new_project;
	private JButton new_sound;
	private JButton sort;
	private JButton rename_project;

    /* To arrange the buttons and labels on the control side */
    private GridBagConstraints c;

	public Controller_Side( Soundboard_Side sboard_outer, Soundboard_Inner sboard_inner )
	{
        this.sboard_outer = sboard_outer;
        this.sboard_inner = sboard_inner;

        super.setLayout(new GridBagLayout());
        super.setBackground(Color.WHITE);
        c = new GridBagConstraints();

        Init_Images();
        Init_Project_Ctrl_Buttons();
        Init_Project_Controller();
        Init_Project_Action_Listeners();
        Init_Sound_Controller();
        Init_Title_Box();
	}

/************************** Interface Methods ****************************************/
	@Override
	public void actionPerformed( ActionEvent e )
	{
        if( e.getSource() == sort )
            sboard_inner.Sort();
		else if( e.getSource() == new_sound )
            sboard_inner.New_Sound();
		else if( e.getSource() == save_project )
            sboard_inner.Save_Project();
		else if( e.getSource() == load_project )
            sboard_inner.Load_Project();
        else if( e.getSource() == new_project )
            sboard_inner.New_Project();
		else if( e.getSource() == rename_project )
            sboard_inner.Rename_Project();

        if( e.getSource() == load_project || e.getSource() == new_project || e.getSource() == rename_project )
            sboard_outer.Set_Title();
	}

/**************************** Private Helper Methods **********************************/

    private void Init_Project_Action_Listeners()
    {
        load_project.addActionListener( this );
        new_project.addActionListener( this );
        new_sound.addActionListener( this );
        rename_project.addActionListener( this );
        save_project.addActionListener( this );
        this.sort.addActionListener( this ); //added this because intellij was saying method contained dup code
    }

    private void Init_Images()
    {
        project_ctrl_label = new ImageIcon( this.getClass().getResource("label1.png") );
        sound_ctrl_label = new ImageIcon( this.getClass().getResource("sound_ctrl_label.png") );
        load_icon = new ImageIcon( this.getClass().getResource("load.gif") );
        save_icon = new ImageIcon( this.getClass().getResource("save.gif") );
        new_project_icon = new ImageIcon( this.getClass().getResource("sdb.gif") );
        new_sound_icon = new ImageIcon( this.getClass().getResource("quote.gif") );
        sort_icon = new ImageIcon( this.getClass().getResource("sort.png") );
        rename_icon = new ImageIcon( this.getClass().getResource("rename.png") );

    }

    private void Init_Project_Controller()
    {
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        super.add( new JLabel( project_ctrl_label ), c );

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        super.add( new Project_Controller_Panel(), c );
    }

    private void Init_Project_Ctrl_Buttons()
    {
        load_project = new JButton( "Load", load_icon );
        save_project = new JButton( "Save", save_icon );
        new_project = new JButton( "New Project", new_project_icon );
        new_sound = new JButton( "New Sound", new_sound_icon );
        sort = new JButton( "Sort Sounds", sort_icon );
        rename_project = new JButton( "Rename Project", rename_icon );
    }

    private void Init_Sound_Controller()
    {
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        super.add( new JLabel(sound_ctrl_label), c );

        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.weightx = 1;
        super.add( new Sound_Controller(sboard_inner), c );
    }

    private void Init_Title_Box()
    {
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 1;
        title_box = new Title_Box();
        super.add( title_box, c );
    }

/******************************* Private class ***************************************/
    private class Project_Controller_Panel extends JPanel
    {
        public Project_Controller_Panel()
        {
            super.setLayout( new GridLayout(2,3) );
            super.add( load_project );
            super.add( save_project );
            super.add( new_project );
            super.add( new_sound );
            super.add( sort );
            super.add( rename_project );
        }
    }
}
