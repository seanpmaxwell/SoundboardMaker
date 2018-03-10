import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Title_Box extends JPanel implements ActionListener
{
    private ImageIcon title_icon;
    private JButton title_button;
    private JOptionPane info_box;

    public Title_Box()
    {
        title_icon = new ImageIcon( this.getClass().getResource("title.png") );
        title_button = new JButton( title_icon );
        title_button.setPreferredSize( new Dimension(525, 300) );
        title_button.addActionListener( this );
        info_box = new JOptionPane();

        super.setBackground( Color.WHITE );
        super.setLayout( new FlowLayout() );
        super.add( title_button );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( e.getSource() == title_button )
            Display_About_Info();
    }

    private void Display_About_Info()
    {
        info_box.showMessageDialog( this, Get_Info() );
    }

    private String Get_Info()
    {
        return "_INSTRUCTIONS_\n" +

                "\nGetting Started: \n" +
                "This is a soundboard creator. The right side is the soundboard and the left side is the controls for\n" +
                "making new sounds and adding sounds to the soundboard. You can record new sounds or upload your own.\n" +
                "To start using the the soundboard maker you must first either record a new sound or load a new sound.\n" +
                "Or you could also create a new sound button on the right by clicking on the 'New Sound' button; although,\n" +
                "this new button will not be able to play sound yet.\n" +

                "\nRecording Sound: \n" +
                "You can record a new sound by clicking the 'Record' button under sound controls. When that button is\n" +
                "clicked the program will record any sound through your speakers. To stop recording simply click the same\n" +
                "same button again.\n" +

                "\nLoading Sound: \n" +
                "To load a new sound that you already have saved on your computer, click the 'Load' button under sound\n" +
                "and select your sound file. Note, this program can only use wave files.\n" +

                "\nPlaying a loaded or recorded sound:\n" +
                "A sound file that has been loaded or recorded can be played, paused, or stopped using the sound controls.\n" +
                "These controls will apply to the most recent sound which has been loaded or record. They cannot control\n" +
                "sounds which are played by pushing a soundboard button on the right. To stop a sound which is playing on\n" +
                "the soundboard, push the 'Enter' key.\n" +

                "\nCreating a new sound button:\n" +
                "To create a new sound on the soundboard you can click 'New Sound' under project controls or click 'Current\n" +
                "Sound' under sound controls. Clicking 'New Sound' will create an empty button with no label which does\n" +
                "not currently play a sound. Clicking 'Current Sound' will create a button automatically for you which\n" +
                "plays that sound. The label for the new sound will be the name of the currently loaded clip. If a new\n" +
                "sound has just been recorded then the label for the new button will be 'recordedClip.wav'. To add a sound\n" +
                "to a button that you created by clicking 'New Sound', click-and-hold on 'Current Sound' then drag the\n " +
                "cursor to the soundboard button that you want to play that sound.\n" +

                "\n_ABOUT_ \n\n" +

                "Thank you for choosing Soundboard Maker. This is a simple desktop application for creating soundboards. A\n" +
                "soundboard is just a panel with a group of buttons. Each button can be labeled and made to play a different\n" +
                "sound. Soundboards can used for wide variety of applications from organizing lectures, music, and sound\n" +
                "effects. This application was created by me, Sean Faubion, as a student in comp sci and shortly after\n" +
                "graduating. Its major purpose was to give me practice with java, but after learning web-design during my\n" +
                "summer internship, I intend to leave this as a simple project and create a more advanced version as a \n" +
                "web-application. You can view the source code for this project by going to seanfaubion.com/projects";
    }
}
