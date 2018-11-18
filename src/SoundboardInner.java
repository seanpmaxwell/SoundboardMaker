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


class SoundboardInner extends JPanel implements ActionListener, LineListener
{
	private JPopupMenu _popup;
	private JMenuItem _deleteSoundEle;
	private JMenuItem _renameSoundEle;

	private ArrayList<SoundButton> _allSoundBtns;
    private JFileChooser _fileChooser;
    private File _directory;
    private MouseListener _popupListener;

    private String _projectTitle;
    private String _label;

    // Playing Sound
	private SoundButton _currSoundBtn;
    private Clip _audioClip;

    private static final File _ROOT_DIR = new File(new JFileChooser().getFileSystemView().getDefaultDirectory() +
            "\\SoundboardMaker");


    // Pick up here, hardcode some strings



    // **************************************************************************************************** //
    //                                    Initialize Soundboard Inner Portion
    // **************************************************************************************************** //
    	
	SoundboardInner()
	{
        this._allSoundBtns = new ArrayList<>();

        super.setBackground(Color.WHITE);
		super.setLayout(new WrapLayout());

        this._initBtnPopup();
        this._setKeyBindings();
	}

    private void _initBtnPopup()
    {
        this._popup = new JPopupMenu();

        this._deleteSoundEle = new JMenuItem("Delete Sound");
        this._renameSoundEle = new JMenuItem("Rename Sound");

        this._deleteSoundEle.addActionListener(this);
        this._renameSoundEle.addActionListener(this);

        this._popup.add(this._deleteSoundEle);
        this._popup.add(this._renameSoundEle);
    }

    private void _setKeyBindings()
    {
        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "stop sound");

        super.getActionMap().put("stop sound", new Key_Binding());
    }



    // **************************************************************************************************** //
    //
    // **************************************************************************************************** //

    void createSoundBtn()
    {
        this._currSoundBtn = new SoundButton(this._label);
        this._currSoundBtn.setProjectTitle(this._projectTitle);
        this._currSoundBtn.addActionListener(this);
        this._currSoundBtn.addMouseListener(new PopupListener());

        this._allSoundBtns.add(this._currSoundBtn);
        super.add(this._currSoundBtn);

        new DropTargetListener(this._currSoundBtn);
    }

    void createSoundBtnAuto(String currSoundName, String path)
    {
        this._label = currSoundName;
        createSoundBtn();
        this._currSoundBtn.setTrack(path);

        super.repaint();
        super.revalidate();
    }
	
	String getProjectTitle()
    {
        return this._projectTitle;
    }

    void loadProject()
    {
        var filter = new FileNameExtensionFilter("Soundboard file", new String[] {"sdb"});

        this._fileChooser = new JFileChooser();
        this._fileChooser.setFileFilter(filter);
        this._fileChooser.setCurrentDirectory(this._ROOT_DIR);

        if(this._fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Open_Project_File();

            if(this._allSoundBtns.size() != 0) {
                this._projectTitle = this._allSoundBtns.get(0).getProjectTitle();
            }

            this._cleanPanel();
            this._addAllBtns();
        }
    }

    void createNewProject()
    {
        label = JOptionPane.showInputDialog(null, "Enter Title for this Project", "New Project Title", JOptionPane.PLAIN_MESSAGE);
        Check_Name();
        project_title = label;
        all_sound_buttons.clear();
        this._cleanPanel();
    }

    void newSound()
    {
        label = JOptionPane.showInputDialog( null, "Enter Label for this Sound", "New Quote Label",
                                             JOptionPane.PLAIN_MESSAGE );

        Check_Name();
        this.createSoundBtn();

        super.repaint();
        super.revalidate();
    }

    void renameProject()
    {
        label = JOptionPane.showInputDialog( null, "Enter New Title for this Soundboard", "New Project Title",
                                             JOptionPane.PLAIN_MESSAGE );
        Check_Name();
        project_title = label;
        for( Sound_Button button: all_sound_buttons )
            button.Set_Project_Title( label );
    }

    void saveProject()
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

    void sort()
    {
        Collections.sort( all_sound_buttons );
        Clean_Panel();
        Add_All_Buttons();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() instanceof SoundButton) {

            if(audio_clip != null)
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
        if(le.getType().equals(LineEvent.Type.STOP)) {
            audio_clip.close();
            audio_clip = null;
        }
    }

    private void _addAllBtns()
    {
        for(SoundButton btn: allSoundBtns ) {
            popup_listener = new PopupListener();
            btn.addActionListener( this );
            btn.addMouseListener( popup_listener );
            super.add( btn );
            new Drop_Target_Listener(btn);
        }
    }

    private void _checkName()
    {
        if(!label.matches("^[a-zA-Z0-9_ ]+$")) {
            JOptionPane.showMessageDialog(null, "Name can only have letters and numbers and cannot be length 0");
            throw new RuntimeException("Name can only have letters and numbers");
        }
    }

    private void _deleteSound()
    {
        this._allSoundBtns.remove(this._currSoundBtn);

        if(audio_clip != null) {
            audio_clip.close();
        }

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



    // **************************************************************************************************** //
    //                                            Shared
    // **************************************************************************************************** //

    private void _cleanPanel()
    {
        super.removeAll();
        super.repaint();
        super.revalidate();
    }
}
