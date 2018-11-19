/**
 * A single sound button in the soundboard.
 *
 * created Nov 6, 2018
 */

import java.io.Serializable;
import javax.swing.JButton;


public class SoundButton extends JButton implements Comparable<SoundButton>, Serializable
{
	private String _soundLabel;
	private String _projectTitle;
    private String _track;
	
	SoundButton(String soundLabel)
    {
		super(soundLabel);
		this.setSoundLabel(soundLabel);
	}
	
	String getSoundLabel()
	{
		return this._soundLabel;
	}

    String getProjectTitle()
    {
        return this._projectTitle;
    }

    String getTrack()
    {
        return this._track;
    }
	
	void setSoundLabel(String soundLabel)
    {
        this._soundLabel = soundLabel;
    }

	void setProjectTitle(String projectTitle)
    {
        this._projectTitle = projectTitle;
    }

    // track is a string representing a file path
	void setTrack(String track)
    {
        this._track = track;
    }

	public int compareTo(SoundButton otherBtn)
	{
		if(otherBtn == null || !(otherBtn instanceof SoundButton)) {
		    throw new RuntimeException("Error Comparing Buttons");
        }

        var otherLabel = otherBtn.getSoundLabel();
        int comparison = getSoundLabel().compareTo(otherLabel);
				
		if(comparison == 0) {
            return 0;
        }
		else if(comparison > 0) {
            return 1;
        }
		else {
            return -1;
        }
	}
}
