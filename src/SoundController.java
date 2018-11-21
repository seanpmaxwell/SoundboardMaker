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


public class SoundController extends JPanel implements ActionListener, LineListener, DragGestureListener
{
    // Define buttons
    private JButton _loadSoundBtn;
    private JButton _playSoundBtn;
    private JButton _pauseSoundBtn;
    private JButton _stopSoundBtn;
    private JButton _recordSoundBtn;
    private JButton _currSoundBtn;

    // Appearance Settings
    private SoundRecorder _soundRecorder;
    private SoundboardInner _sboardInner;
    private JLabel _soundHeader;

    // Opening and Playing Sound File
    private File _waveFile;
    private Clip _audioClip;
    private JFileChooser _fileChooser;

    private boolean _isRecording;
    private boolean _isPausing;


    SoundController(SoundboardInner sboardInner)
    {
        super.setBackground(Color.WHITE);
        this._sboardInner = sboardInner;

        this._initSoundCtlrBtns();
        this._initLayout();

        this._fileChooser = new JFileChooser();
        var defaultDir = new File(Constants.DEFAULT_RECORDING_PATH);
        this._fileChooser.setCurrentDirectory(defaultDir);

        var extensionFilter = new FileNameExtensionFilter("Sound File", new String[] {"wav"});
        this._fileChooser.setFileFilter(extensionFilter);

        var dragSrc = new DragSource();
        var copyAction = DnDConstants.ACTION_COPY;
        dragSrc.createDefaultDragGestureRecognizer(this._currSoundBtn, copyAction, this);
    }



    // ************************************************************************************************************** //
    //                                           Initialize Sound Controller
    // ************************************************************************************************************** //

    private void _initSoundCtlrBtns()
    {
        var c = this.getClass();

        var loadIcon = new ImageIcon(c.getResource("load.png"));
        var playIcon = new ImageIcon(c.getResource("play.png"));
        var pauseIcon = new ImageIcon(c.getResource("pause.png"));
        var stopIcon = new ImageIcon(c.getResource("stop.png"));
        var recordIcon = new ImageIcon(c.getResource("record.png"));
        var noteIcon = new ImageIcon(c.getResource("note.png"));

        this._loadSoundBtn = new JButton("Load Sound", loadIcon);
        this._playSoundBtn = new JButton("Play", playIcon);
        this._pauseSoundBtn = new JButton("Pause", pauseIcon);
        this._stopSoundBtn = new JButton("Stop", stopIcon);
        this._recordSoundBtn = new JButton("Start Recording", recordIcon);
        this._currSoundBtn = new JButton("Current Sound", noteIcon);

        this._loadSoundBtn.addActionListener(this);
        this._playSoundBtn.addActionListener(this);
        this._pauseSoundBtn.addActionListener(this);
        this._stopSoundBtn.addActionListener(this);
        this._recordSoundBtn.addActionListener(this);
        this._currSoundBtn.addActionListener(this);
    }

    private void _initLayout()
    {
        super.setLayout(new GridBagLayout());
        var gridBag = new GridBagConstraints();

        this._setSoundCtlrLabelLayout(gridBag);
        this._setSoundCtlrLayout(gridBag);
    }

    private void _setSoundCtlrLabelLayout(GridBagConstraints gridBag)
    {
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.ipady = 5;
        gridBag.anchor = GridBagConstraints.NORTH;

        this._soundHeader = new JLabel("Current Sound:");
        super.add(this._soundHeader, gridBag);
    }

    private void _setSoundCtlrLayout(GridBagConstraints gridBag)
    {
        gridBag.gridx = 0;
        gridBag.gridy = 1;
        gridBag.weightx = 1;
        gridBag.fill = GridBagConstraints.HORIZONTAL;

        class SoundCtlrPanel extends JPanel {
            SoundCtlrPanel() {
                super.setLayout(new GridLayout(2,3));
                super.add(_loadSoundBtn);
                super.add(_playSoundBtn);
                super.add(_pauseSoundBtn);
                super.add(_stopSoundBtn);
                super.add(_recordSoundBtn);
                super.add(_currSoundBtn);
            }
        }

        super.add(new SoundCtlrPanel(), gridBag);
    }



    // ************************************************************************************************************** //
    //                                             Listen for Events
    // ************************************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        this._isPausing = false;

        var src = actionEvent.getSource();

        if(src == this._loadSoundBtn) {
            this._loadSelectedSound();
        }
        else if(src == this._playSoundBtn) {
            this._audioClip.start();
        }
        else if(src == this._pauseSoundBtn) {
            this._isPausing = true;
            this._audioClip.stop();
        }
        else if(src == this._stopSoundBtn) {
            this._audioClip.setFramePosition(0);
            this._audioClip.stop();
        }
        else if(src == this._recordSoundBtn) {
            this._record();
        }
        else if(src == this._currSoundBtn) {
            var path = this._waveFile.toPath().toString();
            this._sboardInner.createSoundBtnAuto(this._waveFile.getName(), path);
        }
    }

    private void _loadSelectedSound()
    {
        int state = this._fileChooser.showOpenDialog(this);

        if(state == JFileChooser.APPROVE_OPTION) {

            if(this._fileChooser.getSelectedFile() == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Wrong File Type");
                throw new RuntimeException("No file");
            }
            else {
                this._waveFile = this._fileChooser.getSelectedFile();
                this._soundHeader.setText("Clip: " + this._waveFile.getName());
                this._loadSoundFile();
            }
        }
    }

    private void _record()
    {
        if(!this._isRecording) {
            this._soundRecorder = new SoundRecorder();
            this._isRecording = true;
            this._recordSoundBtn.setText("Stop Recording");
            this._audioClip = null;
            this._soundRecorder.start();
        }
        else {
            this._recordSoundBtn.setText("Start Recording");
            this._soundHeader.setText("Clip: " + Constants.DEFAULT_RECORDING_NAME);
            this._isRecording = false;
            this._soundRecorder.stopRecording();
            this._soundRecorder = null;
            this._waveFile = new File(Constants.DEFAULT_RECORDING_PATH);
            this._loadSoundFile();
        }
    }

    @Override
    public void update(LineEvent lineEvent)
    {
        var stopEvent = LineEvent.Type.STOP;
        var isStopEvent = lineEvent.getType().equals(stopEvent);

        if(isStopEvent && !this._isPausing) {
            this._audioClip.setFramePosition(0);
        }
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event)
    {
        if(event.getDragAction() == DnDConstants.ACTION_COPY) {
            var cursor = DragSource.DefaultCopyDrop;
            var file = new TransferableFile(this._waveFile);
            event.startDrag(cursor, file);
        }
    }


    // *************************** Shared *************************** //

    private void _loadSoundFile()
    {
        try {
            var fileInputStream = new FileInputStream(this._waveFile);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);

            this._audioClip = AudioSystem.getClip();
            this._audioClip.open(audioInputStream);
            this._audioClip.addLineListener(this);

            fileInputStream.close();
            bufferedInputStream.close();
            audioInputStream.close();
        }
        catch(IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }
}
