import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SoundRecorder extends Thread
{
    private DataLine.Info dline_info;
    private File wave_file;
    private AudioInputStream audio_input_stream;
    private TargetDataLine target_dline;

    public void run()
    {
        try {
            dline_info = new DataLine.Info( TargetDataLine.class, Get_Audio_Format() );
            if( !AudioSystem.isLineSupported(dline_info) ) {
                System.out.println("Line not supported");
                System.exit(0);
            }

            target_dline = (TargetDataLine)AudioSystem.getLine( dline_info );
            target_dline.open( Get_Audio_Format() );
            target_dline.start();

            audio_input_stream = new AudioInputStream( target_dline );
            wave_file = new File( new JFileChooser().getFileSystemView().getDefaultDirectory() + "\\SoundboardMaker\\recordedClip.wav" );
            AudioSystem.write( audio_input_stream, AudioFileFormat.Type.WAVE, wave_file );
            audio_input_stream.close();
        }
        catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    void stopRecording()
    {
        target_dline.stop();
        target_dline.close();
    }

    // Specify the audio format -> AudioFormat(sampleRate, sampleSize(bits), channels, signed, bigEndian)
    private AudioFormat Get_Audio_Format()
    {
        return new AudioFormat(16000, 8, 2, true, true);
    }
}