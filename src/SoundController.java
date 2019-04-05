/**
 * Controller for playing/recording sounds
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


public class SoundController extends JPanel implements ActionListener, LineListener, DragGestureListener {

    // Define buttons
    private JButton loadSoundBtn;
    private JButton playSoundBtn;
    private JButton pauseSoundBtn;
    private JButton stopSoundBtn;
    private JButton recordSoundBtn;
    private JButton currSoundBtn;

    // Appearance Settings
    private SoundRecorder soundRecorder;
    private SoundboardInner sboardInner;
    private JLabel soundHeader;

    // Opening and Playing Sound File
    private File waveFile;
    private Clip audioClip;
    private JFileChooser fileChooser;

    private boolean isRecording;
    private boolean isPausing;


    SoundController(SoundboardInner sboardInner) {

        super.setBackground(Color.WHITE);
        this.sboardInner = sboardInner;

        this.initSoundCtlrBtns();
        this.initLayout();

        this.fileChooser = new JFileChooser();
        var defaultDir = new File(Constants.DEFAULT_RECORDING_PATH);
        this.fileChooser.setCurrentDirectory(defaultDir);

        var extensionFilter = new FileNameExtensionFilter("Sound File", new String[] {"wav"});
        this.fileChooser.setFileFilter(extensionFilter);

        var dragSrc = new DragSource();
        var copyAction = DnDConstants.ACTION_COPY;
        dragSrc.createDefaultDragGestureRecognizer(this.currSoundBtn, copyAction, this);
    }


    // ************************************************************************************************************** //
    //                                           Initialize Sound Controller
    // ************************************************************************************************************** //

    private void initSoundCtlrBtns() {

        var c = this.getClass();

        var loadIcon = new ImageIcon(c.getResource("load.png"));
        var playIcon = new ImageIcon(c.getResource("play.png"));
        var pauseIcon = new ImageIcon(c.getResource("pause.png"));
        var stopIcon = new ImageIcon(c.getResource("stop.png"));
        var recordIcon = new ImageIcon(c.getResource("record.png"));
        var noteIcon = new ImageIcon(c.getResource("note.png"));

        this.loadSoundBtn = new JButton("Load Sound", loadIcon);
        this.playSoundBtn = new JButton("Play", playIcon);
        this.pauseSoundBtn = new JButton("Pause", pauseIcon);
        this.stopSoundBtn = new JButton("Stop", stopIcon);
        this.recordSoundBtn = new JButton("Start Recording", recordIcon);
        this.currSoundBtn = new JButton("Current Sound", noteIcon);

        this.loadSoundBtn.addActionListener(this);
        this.playSoundBtn.addActionListener(this);
        this.pauseSoundBtn.addActionListener(this);
        this.stopSoundBtn.addActionListener(this);
        this.recordSoundBtn.addActionListener(this);
        this.currSoundBtn.addActionListener(this);
    }


    private void initLayout() {

        super.setLayout(new GridBagLayout());
        var gridBag = new GridBagConstraints();

        this.setSoundCtlrLabelLayout(gridBag);
        this.setSoundCtlrLayout(gridBag);
    }


    private void setSoundCtlrLabelLayout(GridBagConstraints gridBag) {

        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.ipady = 5;
        gridBag.anchor = GridBagConstraints.NORTH;

        this.soundHeader = new JLabel("Current Sound:");
        super.add(this.soundHeader, gridBag);
    }


    private void setSoundCtlrLayout(GridBagConstraints gridBag) {

        gridBag.gridx = 0;
        gridBag.gridy = 1;
        gridBag.weightx = 1;
        gridBag.fill = GridBagConstraints.HORIZONTAL;

        class SoundCtlrPanel extends JPanel {
            SoundCtlrPanel() {
                super.setLayout(new GridLayout(2,3));
                super.add(loadSoundBtn);
                super.add(playSoundBtn);
                super.add(pauseSoundBtn);
                super.add(stopSoundBtn);
                super.add(recordSoundBtn);
                super.add(currSoundBtn);
            }
        }

        super.add(new SoundCtlrPanel(), gridBag);
    }


    // ************************************************************************************************************** //
    //                                             Listen for Events
    // ************************************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        this.isPausing = false;
        var src = actionEvent.getSource();

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
            var path = this.waveFile.toPath().toString();
            this.sboardInner.createSoundBtnAuto(this.waveFile.getName(), path);
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

        var stopEvent = LineEvent.Type.STOP;
        var isStopEvent = lineEvent.getType().equals(stopEvent);

        if (isStopEvent && !this.isPausing) {
            this.audioClip.setFramePosition(0);
        }
    }


    @Override
    public void dragGestureRecognized(DragGestureEvent event) {

        if (event.getDragAction() == DnDConstants.ACTION_COPY) {
            var cursor = DragSource.DefaultCopyDrop;
            var file = new TransferableFile(this.waveFile);
            event.startDrag(cursor, file);
        }
    }


    // *************************** Shared *************************** //

    private void loadSoundFile() {

        try {
            var fileInputStream = new FileInputStream(this.waveFile);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);

            this.audioClip = AudioSystem.getClip();
            this.audioClip.open(audioInputStream);
            this.audioClip.addLineListener(this);

            fileInputStream.close();
            bufferedInputStream.close();
            audioInputStream.close();

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }
}
