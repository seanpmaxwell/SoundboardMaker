/**
 * A single sound button in the soundboard.
 *
 * created Nov 6, 2018
 */

import java.io.Serializable;
import javax.swing.JButton;


public class SoundButton extends JButton implements Comparable<SoundButton>, Serializable {

    private static final long serialVersionUID = 1L;

    private String soundLabel;
    private String projectTitle;
    private String track;


    SoundButton(String soundLabel) {
        super(soundLabel);
        this.setSoundLabel(soundLabel);
    }


    String getSoundLabel() {
        return this.soundLabel;
    }


    String getProjectTitle() {
        return this.projectTitle;
    }


    String getTrack() {
        return this.track;
    }


    void setSoundLabel(String soundLabel) {
        this.soundLabel = soundLabel;
    }


    void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }


    // track is a string representing a file path
    void setTrack(String track) {
        this.track = track;
    }


    public int compareTo(SoundButton otherBtn) {
        if (otherBtn == null) {
            return -1;
        }
        String otherLabel = otherBtn.getSoundLabel();
        return getSoundLabel().compareTo(otherLabel);
    }
}
