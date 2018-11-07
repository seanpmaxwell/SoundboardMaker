/**
 *
 *
 * created Nov 6, 2018
 */


import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Sound_Controller extends JPanel implements ActionListener, LineListener, DragGestureListener
{
	/* Buttons for loading or recording a new sound */
	private JButton load_sound;
	private JButton play_sound;
	private JButton pause_sound;
	private JButton stop_sound;
	private JButton record_sound;
	private JButton current_sound;

    /* Images for control buttons */
    private ImageIcon load_icon;
    private ImageIcon play_icon;
    private ImageIcon pause_icon;
    private ImageIcon stop_icon;
    private ImageIcon record_icon;
    private ImageIcon note_icon;

    /* Layout */
    private Sound_Recorder sound_recorder;
    private Soundboard_Inner sboard_inner;
    private GridBagConstraints c;
    private JLabel sound_header;

    /* Opening and Playing Sound File */
    private File wave_file;  //the file on the hard disk, has to be loaded into an audio stream to play
    private File directory;
    private Clip audio_clip; //the actual audio clip itself used to start and stop the sounds
    private JFileChooser file_chooser;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private AudioInputStream audio_input_stream;

    /* Misc */
    private boolean recording;
    private boolean pausing;
    private DragSource drag_source;
    private Cursor cursor;

    public Sound_Controller(Soundboard_Inner sboard_inner)
    {
        super.setBackground(Color.WHITE);
		this.sboard_inner = sboard_inner;
        directory = new File( new JFileChooser().getFileSystemView().getDefaultDirectory() + "\\SoundboardMaker" );

        var horse = "horse";
        System.out.println(horse);

        Init_Sound_Ctrl_Images();
		Init_Sound_Ctrl_Buttons();
        Init_Sound_Controller();
        Init_Sound_Action_Listeners();

        file_chooser = new JFileChooser();
        file_chooser.setCurrentDirectory( directory );
        file_chooser.setFileFilter( new FileNameExtensionFilter("Sound File", new String[] {"wav"}) );
        drag_source = new DragSource();
        drag_source.createDefaultDragGestureRecognizer( current_sound, DnDConstants.ACTION_COPY, this );
	}


/***************************** Interface Methods ***********************************************/
	@Override
	public void actionPerformed( ActionEvent ae )
    {
        pausing = false;

		if( ae.getSource() == load_sound )
            Load_Selected_Sound();
		else if( ae.getSource() == play_sound )
            audio_clip.start();
		else if( ae.getSource() == pause_sound )
			Pause();
		else if( ae.getSource() == stop_sound )
			Stop();
		else if( ae.getSource() == record_sound )
			Record();
		else if( ae.getSource() == current_sound )
            Make_Button_from_Current_Sound();
	}

    @Override
    public void update( LineEvent le )
    {
        if( le.getType().equals(LineEvent.Type.STOP) && !pausing )
            audio_clip.setFramePosition(0);
    }

    @Override
    public void dragGestureRecognized( DragGestureEvent dge )
    {
        if( dge.getDragAction() == DnDConstants.ACTION_COPY )
            cursor = DragSource.DefaultCopyDrop;

        dge.startDrag( cursor, new TransferableFile(wave_file) );
    }

/*********************************** Private Methods *************************************/

    private void Init_Sound_Action_Listeners()
    {
        load_sound.addActionListener( this );
        play_sound.addActionListener( this );
        pause_sound.addActionListener( this );
        stop_sound.addActionListener( this );
        record_sound.addActionListener( this );
        this.current_sound.addActionListener( this ); // added this because intellij was saying method contained dup code
    }

    private void Init_Sound_Controller()
    {
        super.setLayout( new GridBagLayout() );
        c = new GridBagConstraints();
        sound_header = new JLabel( "Current Sound:" );

        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 5;
        c.anchor = GridBagConstraints.NORTH;
        super.add( sound_header, c );

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        super.add( new Sound_Controller_Panel(), c );
    }

	private void Init_Sound_Ctrl_Buttons()
	{
        load_sound = new JButton( "Load", load_icon );
        play_sound = new JButton( "Play", play_icon );
        pause_sound = new JButton( "Pause", pause_icon );
        stop_sound = new JButton( "Stop", stop_icon );
        record_sound = new JButton( "Start Recording", record_icon );
        current_sound = new JButton( "Current Sound", note_icon );
	}

    private void Init_Sound_Ctrl_Images()
    {
        load_icon = new ImageIcon( this.getClass().getResource("load.png") );
        play_icon = new ImageIcon( this.getClass().getResource("play.png") );
        pause_icon = new ImageIcon( this.getClass().getResource("pause.png") );
        stop_icon = new ImageIcon( this.getClass().getResource("stop.png") );
        record_icon = new ImageIcon( this.getClass().getResource("record.png") );
        note_icon = new ImageIcon( this.getClass().getResource("note.png") );
    }

    private void Load_Selected_Sound()
    {
        if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
        {
            if( file_chooser.getSelectedFile() == null )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Wrong File Type");
                throw new RuntimeException("No file");
            }
            else
            {
                wave_file = file_chooser.getSelectedFile();
                sound_header.setText( "Clip: " + wave_file.getName());
                Load_Sound_File();
            }
        }
    }

    private void Load_Sound_File()
    {
        try {
            fis = new FileInputStream( wave_file );
            bis = new BufferedInputStream( fis );
            audio_input_stream = AudioSystem.getAudioInputStream( bis );

            audio_clip = AudioSystem.getClip();
            audio_clip.open( audio_input_stream );
            audio_clip.addLineListener( this );

            fis.close();
            bis.close();
            audio_input_stream.close();
        }
        catch( IOException | UnsupportedAudioFileException | LineUnavailableException ex ) {
            System.out.println( "File error:" + ex.getMessage() );
        }
    }

    private void Make_Button_from_Current_Sound()
    {
        sboard_inner.Create_Sound_Button_Auto( wave_file.getName(), wave_file.toPath().toString() );
    }

    private void Pause()
    {
        pausing = true;
        audio_clip.stop();
    }

    private void Record()
    {
        if( !recording )
            Start_Recording();
        else
            Stop_Recording();
    }

    private void Start_Recording()
    {
        sound_recorder = new Sound_Recorder();
        recording = true;
        record_sound.setText( "Stop Recording" );
        audio_clip = null;
        sound_recorder.start();
    }

    private void Stop()
    {
        audio_clip.setFramePosition(0);
        audio_clip.stop();
    }

    private void Stop_Recording()
    {
        record_sound.setText( "Start Recording" );
        sound_header.setText( "Clip: recordedClip.wav" );
        recording = false;
        sound_recorder.Stop_Recording();
        sound_recorder = null;
        wave_file = new File( new JFileChooser().getFileSystemView().getDefaultDirectory() + "\\SoundboardMaker\\recordedClip.wav" );
        Load_Sound_File();
    }

/********************************* Private Classes **********************************************/
    private class Sound_Controller_Panel extends JPanel
    {
        public Sound_Controller_Panel()
        {
            super.setLayout( new GridLayout(2,3) );
            super.add( load_sound );
            super.add( play_sound );
            super.add( pause_sound );
            super.add( stop_sound );
            super.add( record_sound );
            super.add( current_sound );
        }
    }

}
