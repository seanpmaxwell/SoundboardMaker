/**
 * Classes for recording sounds from the user's microphone.
 *
 * created Nov 18, 2018
 */

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SoundRecorder extends Thread
{
    private File wave_file;
    private TargetDataLine _targetDataLine;

    // AudioFormat(sampleRate, sampleSize(bits), channels, signed, bigEndian)
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(16000, 8, 2, true, true);

    // Location to save new recording
    private static final String newRecordingPath = new JFileChooser().getFileSystemView().getDefaultDirectory() +
            "\\SoundboardMaker\\recordedClip.wav";

    public void run()
    {
        try {
            var dataLineInfo = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);

            if(!AudioSystem.isLineSupported(dataLineInfo)) {
                System.out.println("Line not supported");
                System.exit(0);
            }

            this._targetDataLine = (TargetDataLine)AudioSystem.getLine(dataLineInfo);
            this._targetDataLine.open(AUDIO_FORMAT);
            this._targetDataLine.start();

            var audioInputStream = new AudioInputStream(this._targetDataLine);
            wave_file = new File(newRecordingPath);

            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wave_file );
            audioInputStream.close();
        }
        catch(LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    void stopRecording()
    {
        this._targetDataLine.stop();
        this._targetDataLine.close();
    }
}