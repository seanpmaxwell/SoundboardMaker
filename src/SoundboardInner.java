/**
 * Starter file for the SoundBoard Maker project.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SoundboardInner extends JPanel implements ActionListener, LineListener
{
    // Misc
	private JPopupMenu popup;
	private JMenuItem delete_sound;
	private JMenuItem rename_sound;
	private ArrayList<Sound_Button> all_sound_buttons;
    private JFileChooser file_chooser;
    private File directory;
    private MouseListener popup_listener;
    private String project_title;
    private String label;

    // Playing Sound
	private Sound_Button current_sound;
    private Clip audio_clip;

    	
	SoundboardInner() throws LineUnavailableException
	{
		all_sound_buttons = new ArrayList<>();
        directory = new File( new JFileChooser().getFileSystemView().getDefaultDirectory() + "\\SoundboardMaker" );

        super.setBackground( Color.WHITE );
		super.setLayout( new WrapLayout() );

        Init_Button_Popup();
        Set_Key_Bindings();
	}

    // ********************************************************************************************************** //
    //
    // ********************************************************************************************************** //

    public void Create_Sound_Button()
    {
        current_sound = new Sound_Button( label );
        current_sound.Set_Project_Title( project_title );
        current_sound.addActionListener( this );
        current_sound.addMouseListener( new PopupListener() );

        all_sound_buttons.add( current_sound );
        super.add( current_sound );
        new Drop_Target_Listener( current_sound );
    }

    public void Create_Sound_Button_Auto( String current_sound_name, String path )
    {
        label = current_sound_name;
        Create_Sound_Button();
        current_sound.Set_Track( path );

        super.repaint();
        super.revalidate();
    }
	
	public String Get_Project_Title()
    {
        return project_title;
    }

    void Load_Project()
    {
        file_chooser = new JFileChooser();
        file_chooser.setFileFilter( new FileNameExtensionFilter( "Soundboard file", new String[] {"sdb"} ) );
        file_chooser.setCurrentDirectory( directory );

        if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
        {
            Open_Project_File();

            if( all_sound_buttons.size() != 0 )
                project_title = all_sound_buttons.get(0).Get_Project_Title();

            Clean_Panel();
            Add_All_Buttons();
        }
    }

    public void New_Project()
    {
        label = JOptionPane.showInputDialog( null, "Enter Title for this Project", "New Project Title",
                                                     JOptionPane.PLAIN_MESSAGE);
        Check_Name();
        project_title = label;
        all_sound_buttons.clear();
        Clean_Panel();
    }

    public void New_Sound()
    {
        label = JOptionPane.showInputDialog( null, "Enter Label for this Sound", "New Quote Label",
                                             JOptionPane.PLAIN_MESSAGE );

        Check_Name();
        Create_Sound_Button();

        super.repaint();
        super.revalidate();
    }

    public void Rename_Project()
    {
        label = JOptionPane.showInputDialog( null, "Enter New Title for this Soundboard", "New Project Title",
                                             JOptionPane.PLAIN_MESSAGE );
        Check_Name();
        project_title = label;
        for( Sound_Button button: all_sound_buttons )
            button.Set_Project_Title( label );
    }

    public void Save_Project()
    {
        if( all_sound_buttons.size() == 0)
            JOptionPane.showMessageDialog(null, "Must create at least one button in order to save");
        else
        {
            file_chooser = new JFileChooser();
            file_chooser.setCurrentDirectory( directory );
            if( file_chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION )
                Save_Project_File();
        }
    }

    public void Sort()
    {
        Collections.sort( all_sound_buttons );
        Clean_Panel();
        Add_All_Buttons();
    }

/******************************** Interface Methods **************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( e.getSource() instanceof Sound_Button )
        {
            if( audio_clip != null )
                audio_clip.close();

            current_sound = (Sound_Button)e.getSource();
            Open_Sound_File();
            audio_clip.start();
        }
        else if( e.getSource() == delete_sound )
            Delete_Sound();
        else if( e.getSource() == rename_sound ) //come back to this
            Rename_Sound();
    }

    @Override
    public void update(LineEvent le)
    {
        if( le.getType().equals(LineEvent.Type.STOP) )
        {
            audio_clip.close();
            audio_clip = null;
        }
    }

/************************************** Private Methods **********************************************/
    private void Add_All_Buttons()
    {
        for( Sound_Button btn: all_sound_buttons )
        {
            popup_listener = new PopupListener();
            btn.addActionListener( this );
            btn.addMouseListener( popup_listener );
            super.add( btn );
            new Drop_Target_Listener( btn );
        }
    }

    private void Check_Name()
    {
        if( !label.matches("^[a-zA-Z0-9_ ]+$") )
        {
            JOptionPane.showMessageDialog(null, "Name can only have letters and numbers and cannot be length 0");
            throw new RuntimeException("Name can only have letters and numbers");
        }

    }

    private void Clean_Panel()
    {
        super.removeAll();
        super.repaint();
        super.revalidate();
    }

    private void Delete_Sound()
    {
        all_sound_buttons.remove( current_sound );

        if( audio_clip != null )
            audio_clip.close();

        super.remove( current_sound );
        super.repaint();
        super.revalidate();
    }

    private ArrayList<Sound_Button> Get_Project_Copy()
    {
        ArrayList<Sound_Button> buttons_to_save = new ArrayList<>();
        Sound_Button btn_to;
        for( Sound_Button btn_from: all_sound_buttons )
        {
            btn_to = new Sound_Button( btn_from.Get_Sound_Label() );
            btn_to.Set_Track( btn_from.Get_Track() );
            btn_to.Set_Project_Title( btn_from.Get_Project_Title() );
            buttons_to_save.add( btn_to );
        }
        return buttons_to_save;
    }

    private void Init_Button_Popup()
    {
        popup = new JPopupMenu();
        delete_sound = new JMenuItem( "Delete Sound" );
        rename_sound = new JMenuItem( "Rename Sound" );
        delete_sound.addActionListener( this );
        rename_sound.addActionListener( this );
        popup.add( delete_sound );
        popup.add( rename_sound );
    }

    private void Open_Project_File()
    {
        try
        {
            FileInputStream fis = new FileInputStream( file_chooser.getSelectedFile() );
            BufferedInputStream bis = new BufferedInputStream( fis );
            ObjectInputStream ois = new ObjectInputStream( bis );
            ArrayList<Sound_Button> instance = (ArrayList<Sound_Button>)ois.readObject();
            all_sound_buttons = instance;
        }
        catch( IOException | ClassNotFoundException ex )
        {
            System.out.println( "File error:" + ex.getMessage() );
        }
    }

    /* Loading and playing a sound file from a string path */
    private void Open_Sound_File()
    {
        try
        {
            FileInputStream fis = new FileInputStream( current_sound.Get_Track() );
            BufferedInputStream bis = new BufferedInputStream( fis );
            AudioInputStream ais = AudioSystem.getAudioInputStream( bis );
            audio_clip = AudioSystem.getClip();
            audio_clip.addLineListener( this );
            audio_clip.open( ais );
        }
        catch( UnsupportedAudioFileException | IOException | LineUnavailableException ex )
        {
            ex.printStackTrace();
        }
    }

    private void Rename_Sound()
    {
        label = JOptionPane.showInputDialog( null, "Enter Label for this Button", "New Button Label",
                JOptionPane.PLAIN_MESSAGE );
        Check_Name();
        current_sound.Set_Sound_Label( label );
        current_sound.setText( label );
    }

    //Don't use a buffered output stream here, it was causing errors
    private void Save_Project_File()
    {
        try
        {
            if (file_chooser.getSelectedFile() == null)
            {
                JOptionPane.showMessageDialog(null, "Wrong File Type");
                throw new RuntimeException("No file");
            }
            FileOutputStream fos = new FileOutputStream( file_chooser.getSelectedFile() + ".sdb" );
            ObjectOutputStream ois = new ObjectOutputStream( fos );
            ois.writeObject( Get_Project_Copy() );
        }
        catch( IOException ex )
        {
            System.out.println( "File error:" + ex.getMessage() );
        }
    }

    private void Set_Key_Bindings()
    {
        super.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW )
                .put( KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "stop sound" );
        super.getActionMap().put("stop sound", new Key_Binding() );
    }

/************************************ Private Classes *************************************************/
	private class PopupListener extends MouseAdapter
	{
	    public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
	    public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }

	    private void maybeShowPopup(MouseEvent e)
		{
	        if( e.isPopupTrigger() )
			{
	            popup.show( e.getComponent(), e.getX(), e.getY() );
	            current_sound = (Sound_Button)e.getSource();
	        }
	    }
	}

    private class Key_Binding extends AbstractAction
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            audio_clip.close();
        }
    }
}




