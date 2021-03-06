/**
 * Classes for recording sounds from the user's microphone.
 *
 * created Nov 18, 2018
 */

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import java.io.File;
import java.io.IOException;


public class SoundRecorder extends Thread {

    private TargetDataLine targetDataLine;

    // AudioFormat(sampleRate, sampleSize(bits), channels, signed, bigEndian)
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(16000, 8, 2, true, true);


    @Override
    public void run() {
        try {
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
            if (!AudioSystem.isLineSupported(dataLineInfo)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            this.targetDataLine = (TargetDataLine)AudioSystem.getLine(dataLineInfo);
            this.targetDataLine.open(AUDIO_FORMAT);
            this.targetDataLine.start();
            var audioInputStream = new AudioInputStream(this.targetDataLine);
            var waveFile = new File(Constants.DEFAULT_RECORDING_PATH);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, waveFile);
            audioInputStream.close();
        } catch(LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }


    void stopRecording() {
        this.targetDataLine.stop();
        this.targetDataLine.close();
    }
}