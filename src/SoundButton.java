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
	private String sound_label;
	private String project_title; 
	
	public SoundButton( String sound_label )
    {
		super(sound_label);
		Set_Sound_Label( sound_label );
	}
	
	public String Get_Sound_Label() { return sound_label; }
    public String Get_Project_Title() { return project_title; }
    public String Get_Track() { return track; }
	
	public void Set_Sound_Label( String sound_label ) { this.sound_label = sound_label; }
	public void Set_Project_Title( String project_title ) { this.project_title = project_title; }
	public void Set_Track( String path ) { track = path; }


	public int compareTo(Sound_Button other)
	{
		if( other == null || !(other instanceof Sound_Button) )
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

