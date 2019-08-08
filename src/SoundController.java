/**
 * Controller for playing/recording sounds
 *
 * created Nov 6, 2018
 */

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.Cursor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SoundController extends JPanel implements ActionListener, LineListener,
    DragGestureListener {

    // Define buttons
    private JButton loadSoundBtn;
    private JButton playSoundBtn;
    private JButton pauseSoundBtn;
    private JButton stopSoundBtn;
    private JButton recordSoundBtn;
    private JButton currSoundBtn;

    // Appearance Settings
    private SoundRecorder soundRecorder;
    private RightInnerContainer rightInnerContainer;
    private JLabel soundHeader;

    // Opening and Playing Sound File
    private File waveFile;
    private Clip audioClip;
    private JFileChooser fileChooser;

    private boolean isRecording;
    private boolean isPausing;

    private static final long serialVersionUID = 1L;


    SoundController(RightInnerContainer rightInnerContainer) {
        this.rightInnerContainer = rightInnerContainer;
        this.initSoundCtlrBtns();
        this.initLayout();
        super.setBackground(Color.WHITE);
        this.fileChooser = new JFileChooser();
        var defaultDir = new File(Constants.DEFAULT_RECORDING_PATH);
        this.fileChooser.setCurrentDirectory(defaultDir);
        var extensionFilter = new FileNameExtensionFilter("Sound File", new String[] {"wav"});
        this.fileChooser.setFileFilter(extensionFilter);
        var dragSrc = new DragSource();
        int copyAction = DnDConstants.ACTION_COPY;
        dragSrc.createDefaultDragGestureRecognizer(this.currSoundBtn, copyAction, this);
    }


    private void initSoundCtlrBtns() {
        // Init Image Icons
        ImageIcon loadIcon = this.getImgIcon("load.png");
        ImageIcon playIcon = this.getImgIcon("play.png");
        ImageIcon pauseIcon = this.getImgIcon("pause.png");
        ImageIcon stopIcon = this.getImgIcon("stop.png");
        ImageIcon recordIcon = this.getImgIcon("record.png");
        ImageIcon noteIcon = this.getImgIcon("note.png");
        // Init Btns
        this.loadSoundBtn = new JButton("Load Sound", loadIcon);
        this.playSoundBtn = new JButton("Play", playIcon);
        this.pauseSoundBtn = new JButton("Pause", pauseIcon);
        this.stopSoundBtn = new JButton("Stop", stopIcon);
        this.recordSoundBtn = new JButton("Start Recording", recordIcon);
        this.currSoundBtn = new JButton("Current Sound", noteIcon);
        // Init Action listeners
        this.loadSoundBtn.addActionListener(this);
        this.playSoundBtn.addActionListener(this);
        this.pauseSoundBtn.addActionListener(this);
        this.stopSoundBtn.addActionListener(this);
        this.recordSoundBtn.addActionListener(this);
        this.currSoundBtn.addActionListener(this);
    }


    private ImageIcon getImgIcon(String name) {
        return new ImageIcon(this.getClass().getResource("res/" + name));
    }


    private void initLayout() {
        super.setLayout(new GridBagLayout());
        var gridBag = new GridBagConstraints();
        // Sound Controller Label
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.ipady = 5;
        gridBag.anchor = GridBagConstraints.NORTH;
        this.soundHeader = new JLabel("Current Sound:");
        super.add(this.soundHeader, gridBag);
        // Sound Controller
        gridBag.gridx = 0;
        gridBag.gridy = 1;
        gridBag.weightx = 1;
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        super.add(new SoundControllerPanel(), gridBag);
    }


    private class SoundControllerPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        SoundControllerPanel() {
            super.setLayout(new GridLayout(2,3));
            super.add(loadSoundBtn);
            super.add(playSoundBtn);
            super.add(pauseSoundBtn);
            super.add(stopSoundBtn);
            super.add(recordSoundBtn);
            super.add(currSoundBtn);
        }
    }


    // ***************************************************************************************** //
    //                                 Listen for Events
    // ***************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.isPausing = false;
        Object src = actionEvent.getSource();
        if (src == this.loadSoundBtn) {
            this.loadSelectedSound();
        } else if (src == this.playSoundBtn) {
            this.audioClip.start();
        } else if (src == this.pauseSoundBtn) {
            this.isPausing = true;
            this.audioClip.stop();
        } else if (src == this.stopSoundBtn) {
            this.audioClip.setFramePosition(0);
            this.audioClip.stop();
        } else if (src == this.recordSoundBtn) {
            this.record();
        } else if (src == this.currSoundBtn) {
            String path = this.waveFile.toPath().toString();
            this.rightInnerContainer.createSoundBtnAuto(this.waveFile.getName(), path);
        }
    }


    private void loadSelectedSound() {
        int state = this.fileChooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            if (this.fileChooser.getSelectedFile() == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Wrong File Type");
                throw new RuntimeException("No file");
            } else {
                this.waveFile = this.fileChooser.getSelectedFile();
                this.soundHeader.setText("Clip: " + this.waveFile.getName());
                this.loadSoundFile();
            }
        }
    }


    private void record() {
        if (!this.isRecording) {
            this.soundRecorder = new SoundRecorder();
            this.isRecording = true;
            this.recordSoundBtn.setText("Stop Recording");
            this.audioClip = null;
            this.soundRecorder.start();
        } else {
            this.recordSoundBtn.setText("Start Recording");
            this.soundHeader.setText("Clip: " + Constants.DEFAULT_RECORDING_NAME);
            this.isRecording = false;
            this.soundRecorder.stopRecording();
            this.soundRecorder = null;
            this.waveFile = new File(Constants.DEFAULT_RECORDING_PATH);
            this.loadSoundFile();
        }
    }


    @Override
    public void update(LineEvent lineEvent) {
        LineEvent.Type stopEvent = LineEvent.Type.STOP;
        boolean isStopEvent = lineEvent.getType().equals(stopEvent);
        if (isStopEvent && !this.isPausing) {
            this.audioClip.setFramePosition(0);
        }
    }


    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        if (event.getDragAction() == DnDConstants.ACTION_COPY) {
            Cursor cursor = DragSource.DefaultCopyDrop;
            var file = new TransferableFile(this.waveFile);
            event.startDrag(cursor, file);
        }
    }


    // *************************** Shared *************************** //

    private void loadSoundFile() {
        try {
            // Init streams
            var fileInputStream = new FileInputStream(this.waveFile);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            // Loud sound file
            this.audioClip = AudioSystem.getClip();
            this.audioClip.open(audioInputStream);
            this.audioClip.addLineListener(this);
            // Close streams
            fileInputStream.close();
            bufferedInputStream.close();
            audioInputStream.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }
}
