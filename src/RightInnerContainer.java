/**
 * Holds the buttons which play the different sounds when clicked on.
 *
 * created Nov 6, 2018
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Collections;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;


class RightInnerContainer extends JPanel implements ActionListener, LineListener {

    private static final long serialVersionUID = 1L;

    private JPopupMenu popup;
    private JMenuItem deleteSoundOpt;
    private JMenuItem renameSoundOpt;

    private JFileChooser fileChooser;
    private ArrayList<SoundButton> allSoundBtns;
    private String projectTitle;

    // Playing Sound
    private SoundButton currSoundBtn;
    private Clip audioClip;

    // New Sound
    private final String NEW_SOUND_MSG = "Enter Label for this Sound";
    private final String NEW_SOUND_LABEL = "New Quote Label";

    // New Project
    private final String NEW_PRJ_MSG = "Enter Title for this Project";
    private final String NEW_PRJ_LABEL = "New Project Title";
    private final String SAVE_PRJ_ALERT = "Must create at least one button in order to save";

    // Rename Project
    private final String RENAME_PRJ_MSG = "Enter New Title for this Soundboard";

    // Rename Button
    private final String RENAME_BTN_MSG = "Enter Label for this Button";
    private final String RENAME_BTN_LABEL = "New Button Label";
    private final String NAME_FORMAT_ERR = "Name can only have letters and numbers and cannot " +
        "be length 0";


    // ***************************************************************************************** //
    //                             Initialize Soundboard Inner Portion
    // ***************************************************************************************** //

    RightInnerContainer() {
        this.allSoundBtns = new ArrayList<SoundButton>();
        super.setBackground(Color.WHITE);
        super.setLayout(new WrapLayout());
        this.initBtnPopup();
        this.setKeyBindings();
    }


    private void initBtnPopup() {
        this.popup = new JPopupMenu();
        this.deleteSoundOpt = new JMenuItem("Delete Sound");
        this.renameSoundOpt = new JMenuItem("Rename Sound");
        this.deleteSoundOpt.addActionListener(this);
        this.renameSoundOpt.addActionListener(this);
        this.popup.add(this.deleteSoundOpt);
        this.popup.add(this.renameSoundOpt);
    }


    private void setKeyBindings() {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "stop sound");
        super.getActionMap().put("stop sound", new KeyBinding());
    }


    private class KeyBinding extends AbstractAction {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            audioClip.close();
        }
    }


    // ***************************************************************************************** //
    //                               Soundboard Button Actions
    // ***************************************************************************************** //

    void sort() {
        Collections.sort(this.allSoundBtns);
        this.cleanPanel();
        this.addAllBtns();
    }


    void newSound() {
        int msg = JOptionPane.PLAIN_MESSAGE;
        String label = JOptionPane.showInputDialog(null, this.NEW_SOUND_MSG, this.NEW_SOUND_LABEL, 
            msg);
        if (this.checkName(label)) {
            this.createSoundBtn(label);
            super.repaint();
            super.revalidate();
        }
    }


    void saveProject() {
        if (this.allSoundBtns.size() == 0) {
            JOptionPane.showMessageDialog(null, this.SAVE_PRJ_ALERT);
        } else {
            this.fileChooser = new JFileChooser();
            var loc = new File(Constants.PROJECTS_DIR);
            this.fileChooser.setCurrentDirectory(loc);
            if (this.fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                this.saveProjectFile();
            }
        }
    }


    private void saveProjectFile() {
        try {
            if (this.fileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, "Wrong File Type");
                throw new RuntimeException("No file");
            }
            File filePath = this.fileChooser.getSelectedFile();
            this.projectTitle = filePath.getName();
            String fileNameWithExt = filePath + ".sdb";
            var fileOutputStream = new FileOutputStream(fileNameWithExt);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.getProjectCopy());
            objectOutputStream.close();
        } catch (IOException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }


    private ArrayList<SoundButton> getProjectCopy() {
        var buttonsToSave = new ArrayList<SoundButton>();
        SoundButton btnTo;
        for (SoundButton btnFrom: this.allSoundBtns) {
            btnTo = new SoundButton(btnFrom.getSoundLabel());
            btnTo.setTrack(btnFrom.getTrack());
            btnTo.setProjectTitle(btnFrom.getProjectTitle());
            buttonsToSave.add(btnTo);
        }
        return buttonsToSave;
    }


    void loadProject() {
        var filter = new FileNameExtensionFilter("Soundboard file", new String[] {"sdb"});
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(filter);
        var loc = new File(Constants.PROJECTS_DIR);
        this.fileChooser.setCurrentDirectory(loc);
        int state = this.fileChooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            this.openProjectFile();
            if (this.allSoundBtns.size() != 0) {
                this.projectTitle = this.allSoundBtns.get(0).getProjectTitle();
            }
            this.cleanPanel();
            this.addAllBtns();
        }
    }


    private void openProjectFile() {
        try {
            File file = this.fileChooser.getSelectedFile();
            var fileInputStream = new FileInputStream(file);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var objectInputStream = new ObjectInputStream(bufferedInputStream);
            Object instance = objectInputStream.readObject();
            this.allSoundBtns = (ArrayList<SoundButton>)instance;
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }


    void createNewProject() {
        int msg = JOptionPane.PLAIN_MESSAGE;
        String label = JOptionPane.showInputDialog(null,
            this.NEW_PRJ_MSG, this.NEW_PRJ_LABEL, msg);
        if (this.checkName(label)) {
            this.projectTitle = label;
            this.allSoundBtns.clear();
            this.cleanPanel();
        } else {
            this.projectTitle = "";
        }
    }


    void renameProject() {
        int msg = JOptionPane.PLAIN_MESSAGE;
        String label = JOptionPane.showInputDialog(null,
            this.RENAME_PRJ_MSG, this.NEW_PRJ_LABEL, msg);
        if (this.checkName(label)) {
            this.projectTitle = label;
            for (SoundButton button: this.allSoundBtns) {
                button.setProjectTitle(label);
            }
        }
    }


    String getProjectTitle() {
        return this.projectTitle;
    }


    void createSoundBtnAuto(String currSoundName, String path) {
        this.createSoundBtn(currSoundName);
        this.currSoundBtn.setTrack(path);
        super.repaint();
        super.revalidate();
    }


    // *************************** Shared *************************** //

    private void addAllBtns() {
        for (SoundButton btn: this.allSoundBtns) {
            var listener = new PopupListener();
            btn.addActionListener(this);
            btn.addMouseListener(listener);
            super.add(btn);
            new DropTargetListener(btn);
        }
    }


    private void cleanPanel() {
        super.removeAll();
        super.repaint();
        super.revalidate();
    }


    private void createSoundBtn(String label) {
        this.currSoundBtn = new SoundButton(label);
        this.currSoundBtn.setProjectTitle(this.projectTitle);
        this.currSoundBtn.addActionListener(this);
        this.currSoundBtn.addMouseListener(new PopupListener());
        this.allSoundBtns.add(this.currSoundBtn);
        super.add(this.currSoundBtn);
        new DropTargetListener(this.currSoundBtn);
    }


    private class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
        public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
        private void maybeShowPopup(MouseEvent event) {
            if (event.isPopupTrigger()) {
                popup.show(event.getComponent(), event.getX(), event.getY());
                currSoundBtn = (SoundButton)event.getSource();
            }
        }
    }


    // ***************************************************************************************** //
    //                                    Sound Button Actions
    // ***************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object src = actionEvent.getSource();
        if (src instanceof SoundButton) {
            this.playSound((SoundButton)src);
        } else if (src == this.deleteSoundOpt) {
            this.deleteSoundBtn();
        } else if (src == this.renameSoundOpt) {
            this.renameSoundBtn();
        }
    }


    private void playSound(SoundButton soundButton) {
        if (this.audioClip != null) {
            this.audioClip.close();
        }
        this.currSoundBtn = soundButton;
        String track = soundButton.getTrack();
        if (track != null) {
            this.openSoundFile(track);
        }
    }


    private void openSoundFile(String track) {
        try {
            var fileInputStream = new FileInputStream(track);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            this.audioClip = AudioSystem.getClip();
            this.audioClip.addLineListener(this);
            this.audioClip.open(audioInputStream);
            this.audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }


    private void deleteSoundBtn() {
        this.allSoundBtns.remove(this.currSoundBtn);
        if (this.audioClip != null) {
            this.audioClip.close();
        }
        super.remove(this.currSoundBtn);
        super.repaint();
        super.revalidate();
    }


    private void renameSoundBtn() {
        int msg = JOptionPane.PLAIN_MESSAGE;
        String label = JOptionPane.showInputDialog(null, this.RENAME_BTN_MSG,
            this.RENAME_BTN_LABEL, msg);
        if (this.checkName(label)) {
            this.currSoundBtn.setSoundLabel(label);
            this.currSoundBtn.setText(label);
        }
    }


    // ***************************************************************************************** //
    //                                        Events
    // ***************************************************************************************** //

    @Override
    public void update(LineEvent lineEvent) {
        LineEvent.Type stopType = LineEvent.Type.STOP;
        if(lineEvent.getType().equals(stopType)) {
            this.audioClip.close();
            this.audioClip = null;
        }
    }


    // ***************************************************************************************** //
    //                                 Shared Multiple Sections
    // ***************************************************************************************** //

    private boolean checkName(String label) {
        if (!label.matches("^[a-zA-Z0-9_ ]+$")) {
            JOptionPane.showMessageDialog(null, this.NAME_FORMAT_ERR);
            return false;
        } else {
            return true;
        }
    }
}
