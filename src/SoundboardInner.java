/**
 * Holds the buttons which play the different sounds when clicked on.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


class SoundboardInner extends JPanel implements ActionListener, LineListener {

    private JPopupMenu _popup;
    private JMenuItem _deleteSoundOpt;
    private JMenuItem _renameSoundOpt;

    private JFileChooser _fileChooser;
    private ArrayList<SoundButton> _allSoundBtns;
    private String _projectTitle;

    // Playing Sound
    private SoundButton _currSoundBtn;
    private Clip _audioClip;

    // New Sound
    private final static String _NEW_SOUND_MSG = "Enter Label for this Sound";
    private final static String _NEW_SOUND_LABEL = "New Quote Label";

    // New Project
    private final static String _NEW_PRJ_MSG = "Enter Title for this Project";
    private final static String _NEW_PRJ_LABEL = "New Project Title";
    private final static String _SAVE_PRJ_ALERT = "Must create at least one button in order to save";

    // Rename Project
    private final static String _RENAME_PRJ_MSG = "Enter New Title for this Soundboard";

    // Rename Button
    private final static String _RENAME_BTN_MSG = "Enter Label for this Button";
    private final static String _RENAME_BTN_LABEL = "New Button Label";

    private final static String _NAME_FORMAT_ERR = "Name can only have letters and numbers and cannot be length 0";


    // **************************************************************************************************** //
    //                                    Initialize Soundboard Inner Portion
    // **************************************************************************************************** //

    SoundboardInner() {

        this._allSoundBtns = new ArrayList();

        super.setBackground(Color.WHITE);
        super.setLayout(new WrapLayout());

        this._initBtnPopup();
        this._setKeyBindings();
    }

    private void _initBtnPopup() {

        this._popup = new JPopupMenu();

        this._deleteSoundOpt = new JMenuItem("Delete Sound");
        this._renameSoundOpt = new JMenuItem("Rename Sound");

        this._deleteSoundOpt.addActionListener(this);
        this._renameSoundOpt.addActionListener(this);

        this._popup.add(this._deleteSoundOpt);
        this._popup.add(this._renameSoundOpt);
    }

    private void _setKeyBindings() {

        var keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "stop sound");

        class KeyBinding extends AbstractAction {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                _audioClip.close();
            }
        }

        super.getActionMap().put("stop sound", new KeyBinding());
    }



    // ************************************************************************************************************** //
    //                                            Soundboard Button Actions
    // ************************************************************************************************************* //

    void sort() {
        Collections.sort(this._allSoundBtns);
        this._cleanPanel();
        this._addAllBtns();
    }

    void newSound() {

        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, _NEW_SOUND_MSG, _NEW_SOUND_LABEL, msg);

        if (this._checkName(label)) {
            this._createSoundBtn(label);
            super.repaint();
            super.revalidate();
        }
    }

    void saveProject() {

        if (this._allSoundBtns.size() == 0) {
            JOptionPane.showMessageDialog(null, _SAVE_PRJ_ALERT);
        } else {
            this._fileChooser = new JFileChooser();
            var loc = new File(Constants.PROJECTS_DIR);
            this._fileChooser.setCurrentDirectory(loc);

            if (this._fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                this._saveProjectFile();
            }
        }
    }

    // Don't use a buffered output stream here, it was causing errors
    private void _saveProjectFile() {

        try {
            if (this._fileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, "Wrong File Type");
                throw new RuntimeException("No file");
            }

            var filePath = this._fileChooser.getSelectedFile();
            this._projectTitle = filePath.getName();

            var fileNameWithExt = filePath + ".sdb";
            var fileOutputStream = new FileOutputStream(fileNameWithExt);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this._getProjectCopy());

        } catch (IOException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }

    private ArrayList<SoundButton> _getProjectCopy() {

        var buttonsToSave = new ArrayList<SoundButton>();
        SoundButton btnTo;

        for(SoundButton btnFrom: this._allSoundBtns) {
            btnTo = new SoundButton(btnFrom.getSoundLabel());
            btnTo.setTrack(btnFrom.getTrack());
            btnTo.setProjectTitle(btnFrom.getProjectTitle());
            buttonsToSave.add(btnTo);
        }

        return buttonsToSave;
    }

    void loadProject() {

        var filter = new FileNameExtensionFilter("Soundboard file", new String[] {"sdb"});

        this._fileChooser = new JFileChooser();
        this._fileChooser.setFileFilter(filter);

        var loc = new File(Constants.PROJECTS_DIR);
        this._fileChooser.setCurrentDirectory(loc);

        int state = this._fileChooser.showOpenDialog(this);

        if (state == JFileChooser.APPROVE_OPTION) {
            this._openProjectFile();

            if (this._allSoundBtns.size() != 0) {
                this._projectTitle = this._allSoundBtns.get(0).getProjectTitle();
            }

            this._cleanPanel();
            this._addAllBtns();
        }
    }

    private void _openProjectFile() {

        try {
            var file = this._fileChooser.getSelectedFile();
            var fileInputStream = new FileInputStream(file);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var objectInputStream = new ObjectInputStream(bufferedInputStream);

            var instance = (ArrayList<SoundButton>)objectInputStream.readObject();
            this._allSoundBtns = instance;

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }

    void createNewProject() {

        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, _NEW_PRJ_MSG, _NEW_PRJ_LABEL, msg);

        if (this._checkName(label)) {
            this._projectTitle = label;
            this._allSoundBtns.clear();
            this._cleanPanel();
        } else {
            this._projectTitle = "";
        }
    }

    void renameProject() {

        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, _RENAME_PRJ_MSG, _NEW_PRJ_LABEL, msg);

        if (this._checkName(label)) {
            this._projectTitle = label;
            for (SoundButton button: this._allSoundBtns) {
                button.setProjectTitle(label);
            }
        }
    }

    String getProjectTitle() {
        return this._projectTitle;
    }

    void createSoundBtnAuto(String currSoundName, String path) {

        this._createSoundBtn(currSoundName);
        this._currSoundBtn.setTrack(path);

        super.repaint();
        super.revalidate();
    }


    // *************************** Shared *************************** //

    private void _addAllBtns() {

        for (SoundButton btn: this._allSoundBtns) {
            var listener = new _PopupListener();
            btn.addActionListener(this);
            btn.addMouseListener(listener);

            super.add(btn);
            new DropTargetListener(btn);
        }
    }

    private void _cleanPanel() {
        super.removeAll();
        super.repaint();
        super.revalidate();
    }

    private void _createSoundBtn(String label) {

        this._currSoundBtn = new SoundButton(label);
        this._currSoundBtn.setProjectTitle(this._projectTitle);
        this._currSoundBtn.addActionListener(this);
        this._currSoundBtn.addMouseListener(new _PopupListener());

        this._allSoundBtns.add(this._currSoundBtn);
        super.add(this._currSoundBtn);

        new DropTargetListener(this._currSoundBtn);
    }

    private class _PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
        public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }

        private void maybeShowPopup(MouseEvent event) {

            if (event.isPopupTrigger()) {
                _popup.show(event.getComponent(), event.getX(), event.getY());
                _currSoundBtn = (SoundButton)event.getSource();
            }
        }
    }



    // ************************************************************************************************************** //
    //                                            Sound Button Actions
    // ************************************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        var src = actionEvent.getSource();

        if (src instanceof SoundButton) {
            this._playSound((SoundButton)src);
        } else if (src == this._deleteSoundOpt) {
            this._deleteSoundBtn();
        } else if (src == this._renameSoundOpt) {
            this._renameSoundBtn();
        }
    }

    private void _playSound(SoundButton soundButton) {

        if (this._audioClip != null) {
            this._audioClip.close();
        }

        this._currSoundBtn = soundButton;
        var track = soundButton.getTrack();

        if (track != null) {
            this._openSoundFile(track);
        }
    }

    private void _openSoundFile(String track) {

        try {
            var fileInputStream = new FileInputStream(track);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);

            this._audioClip = AudioSystem.getClip();
            this._audioClip.addLineListener(this);
            this._audioClip.open(audioInputStream);
            this._audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    private void _deleteSoundBtn() {

        this._allSoundBtns.remove(this._currSoundBtn);

        if (this._audioClip != null) {
            this._audioClip.close();
        }

        super.remove(this._currSoundBtn);
        super.repaint();
        super.revalidate();
    }

    private void _renameSoundBtn() {

        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, this._RENAME_BTN_MSG, this._RENAME_BTN_LABEL, msg);

        if (this._checkName(label)) {
            this._currSoundBtn.setSoundLabel(label);
            this._currSoundBtn.setText(label);
        }
    }



    // ************************************************************************************************************** //
    //                                                  Events
    // ************************************************************************************************************** //

    @Override
    public void update(LineEvent lineEvent) {

        var stopType = LineEvent.Type.STOP;

        if(lineEvent.getType().equals(stopType)) {
            this._audioClip.close();
            this._audioClip = null;
        }
    }



    // ************************************************************************************************************** //
    //                                          Shared Multiple Sections
    // ************************************************************************************************************** //

    private boolean _checkName(String label) {

        if (!label.matches("^[a-zA-Z0-9_ ]+$")) {
            JOptionPane.showMessageDialog(null, _NAME_FORMAT_ERR);
            return false;
        } else {
            return true;
        }
    }
}
