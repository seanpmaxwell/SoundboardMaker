/**
 * A single sound button in the soundboard.
 *
 * created Nov 6, 2018
 */

import java.io.Serializable;
import javax.swing.JButton;


public class SoundButton extends JButton implements Comparable<SoundButton>, Serializable
{
	private String track;
	private String soundLabel;
	private String projectTitle;
	
	SoundButton(String soundLabel)
    {
		super(soundLabel);
		this.setSoundLabel(soundLabel);
	}
	
	String getSoundLabel()
	{
		return sound_label;
	}

    String Get_Project_Title() { return project_title; }
    String Get_Track() { return track; }
	
	void Set_Sound_Label( String sound_label ) { this.sound_label = sound_label; }
	void Set_Project_Title( String project_title ) { this.project_title = project_title; }
	void Set_Track( String path ) { track = path; }


	public int compareTo(SoundButton other)
	{
		if(other == null || !(other instanceof SoundButton))
			throw new RuntimeException("Error Comparing Buttons");

        int comparison = Get_Sound_Label().compareTo( other.Get_Sound_Label() );
				
		if( comparison == 0 )
			return 0;
		else if( comparison > 0 )
			return 1;
		else 
			return -1;
	}
}

