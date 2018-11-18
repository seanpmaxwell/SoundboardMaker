/**
 * Starter file for the SoundBoard Maker project.
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


class SoundboardInner extends JPanel implements ActionListener, LineListener
{
	private JPopupMenu _popup;
	private JMenuItem _deleteSoundOpt;
	private JMenuItem _renameSoundOpt;

	private JFileChooser _fileChooser;
	private ArrayList<SoundButton> _allSoundBtns;

    private String _projectTitle;
    private String _soundBtnlabel;

    // Playing Sound
	private SoundButton _currSoundBtn;
    private Clip _audioClip;

    final static File ROOT_DIR = new File(new JFileChooser().getFileSystemView().getDefaultDirectory() +
            "\\SoundboardMaker");

    // New Sound
    final static String NEW_SOUND_MSG = "Enter Label for this Sound";
    final static String NEW_SOUND_LABEL = "New Quote Label";

    // New Project
    final static String NEW_PRJ_MSG = "Enter Title for this Project";
    final static String NEW_PRJ_LABEL = "New Project Title";
    final static String SAVE_PRJ_ALERT = "Must create at least one button in order to save";

    // Rename Project
    final static String RENAME_PRJ_MSG = "Enter New Title for this Soundboard";

    // Rename Button
    final static String RENAME_BTN_MSG = "Enter Label for this Button";
    final static String RENAME_BTN_LABEL = "New Button Label";


            // **************************************************************************************************** //
    //                                    Initialize Soundboard Inner Portion
    // **************************************************************************************************** //
    	
	SoundboardInner()
	{
        this._allSoundBtns = new ArrayList();

        super.setBackground(Color.WHITE);
		super.setLayout(new WrapLayout());

        this._initBtnPopup();
        this._setKeyBindings();
	}

    private void _initBtnPopup()
    {
        this._popup = new JPopupMenu();

        this._deleteSoundOpt = new JMenuItem("Delete Sound");
        this._renameSoundOpt = new JMenuItem("Rename Sound");

        this._deleteSoundOpt.addActionListener(this);
        this._renameSoundOpt.addActionListener(this);

        this._popup.add(this._deleteSoundOpt);
        this._popup.add(this._renameSoundOpt);
    }

    private void _setKeyBindings()
    {
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



    // **************************************************************************************************** //
    //                                          Soundboard Button Actions
    // **************************************************************************************************** //

    void sort()
    {
        Collections.sort(this._allSoundBtns);
        this._cleanPanel();
        this._addAllBtns();
    }

    void newSound()
    {
        var label = JOptionPane.showInputDialog(null, NEW_SOUND_MSG, NEW_SOUND_LABEL, JOptionPane.PLAIN_MESSAGE);

        this._checkName(label);
        this._createSoundBtn(label);

        super.repaint();
        super.revalidate();
    }

    void saveProject()
    {
        if(this._allSoundBtns.size() == 0) {
            JOptionPane.showMessageDialog(null, SAVE_PRJ_ALERT);
        }
        else {
            this._fileChooser = new JFileChooser();
            this._fileChooser.setCurrentDirectory(this.ROOT_DIR);

            if(this._fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                this._saveProjectFile();
            }
        }
    }

    void loadProject()
    {
        var filter = new FileNameExtensionFilter("Soundboard file", new String[] {"sdb"});

        this._fileChooser = new JFileChooser();
        this._fileChooser.setFileFilter(filter);
        this._fileChooser.setCurrentDirectory(ROOT_DIR);

        if(this._fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this._openProjectFile();

            if(this._allSoundBtns.size() != 0) {
                this._projectTitle = this._allSoundBtns.get(0).getProjectTitle();
            }

            this._cleanPanel();
            this._addAllBtns();
        }
    }

    void createNewProject()
    {
        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, NEW_PRJ_MSG, NEW_PRJ_LABEL, msg);

        this._checkName(label);

        this._projectTitle = label;
        this._allSoundBtns.clear();
        this._cleanPanel();
    }

    void renameProject()
    {
        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, RENAME_PRJ_MSG, NEW_PRJ_LABEL, msg);

        this._checkName(label);
        this._projectTitle = label;

        for(SoundButton button: this._allSoundBtns) {
            button.setProjectTitle(label);
        }
    }

    String getProjectTitle()
    {
        return this._projectTitle;
    }

    void createSoundBtnAuto(String currSoundName, String path)
    {
        this._label = currSoundName;
        this._createSoundBtn(currSoundName);
        this._currSoundBtn.setTrack(path);

        super.repaint();
        super.revalidate();
    }



    // **************************************************************************************************** //
    //                                    Sound Button Actions
    // **************************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        var src = actionEvent.getSource();

        if(src instanceof SoundButton) {

            if(this._audioClip != null) {
                this._audioClip.close();
            }

            this._currSoundBtn = (SoundButton)actionEvent.getSource();

            this._openSoundFile();
            this._audioClip.start();
        }
        else if(src == this._deleteSoundOpt) {
            this._deleteSound();
        }
        else if(src == this._renameSoundOpt) {
            //come back to this
            this._renameSoundBtn();
        }
    }

    private void _openSoundFile()
    {
        try {

            var track = this._currSoundBtn.getTrack();
            var fileInputStream = new FileInputStream(track);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);

            this._audioClip = AudioSystem.getClip();
            this._audioClip.addLineListener(this);
            this._audioClip.open(audioInputStream);
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    private void _deleteSound()
    {
        this._allSoundBtns.remove(this._currSoundBtn);

        if(this._audioClip != null) {
            this._audioClip.close();
        }

        super.remove(this._currSoundBtn);
        super.repaint();
        super.revalidate();
    }

    private void _renameSoundBtn()
    {
        var msg = JOptionPane.PLAIN_MESSAGE;
        var label = JOptionPane.showInputDialog(null, RENAME_BTN_MSG, RENAME_BTN_LABEL, msg);

        this._checkName(label);
        this._currSoundBtn.setSoundLabel(label);
        this._currSoundBtn.setText(label);
    }



    // **************************************************************************************************** //
    //
    // **************************************************************************************************** //

    @Override
    public void update(LineEvent le)
    {
        if(le.getType().equals(LineEvent.Type.STOP)) {
            this._audioClip.close();
            this._audioClip = null;
        }
    }

    private void _addAllBtns()
    {
        for(SoundButton btn: this._allSoundBtns) {
            var listener = new PopupListener();
            btn.addActionListener(this);
            btn.addMouseListener(listener);

            super.add(btn);
            new DropTargetListener(btn);
        }
    }

    private ArrayList<Sound_Button> Get_Project_Copy()
    {
        var buttonsToSave = new ArrayList<SoundButton>();
        SoundButton btnTo;

        for(Sound_Button btn_from: all_sound_buttons) {
            btn_to = new Sound_Button( btn_from.Get_Sound_Label() );
            btn_to.Set_Track( btn_from.Get_Track() );
            btn_to.Set_Project_Title( btn_from.Get_Project_Title() );
            buttons_to_save.add( btn_to );
        }

        return buttons_to_save;
    }

    private void _openProjectFile()
    {
        try {
            FileInputStream fileInputStream = new FileInputStream(file_chooser.getSelectedFile());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

            ArrayList<SoundButton> instance = (ArrayList<SoundButton>)objectInputStream.readObject();
            this._allSoundBtns = instance;
        }
        catch(IOException | ClassNotFoundException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }


	private class PopupListener extends MouseAdapter
	{
	    public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
	    public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }

	    private void maybeShowPopup(MouseEvent e)
		{
	        if( e.isPopupTrigger() )
			{
	            popup.show( e.getComponent(), e.getX(), e.getY() );
	            current_sound = (Sound_Button)e.getSource();
	        }
	    }
	}





    // **************************************************************************************************** //
    //                                              Shared
    // **************************************************************************************************** //

    private void _cleanPanel()
    {
        super.removeAll();
        super.repaint();
        super.revalidate();
    }

    private void _checkName(String label)
    {
        if(!label.matches("^[a-zA-Z0-9_ ]+$")) {
            JOptionPane.showMessageDialog(null, "Name can only have letters and numbers and cannot be length 0");
            throw new RuntimeException("Name can only have letters and numbers");
        }
    }

    private void _createSoundBtn(String label)
    {
        this._currSoundBtn = new SoundButton(label);
        this._currSoundBtn.setProjectTitle(this._projectTitle);
        this._currSoundBtn.addActionListener(this);
        this._currSoundBtn.addMouseListener(new PopupListener());

        this._allSoundBtns.add(this._currSoundBtn);
        super.add(this._currSoundBtn);

        new DropTargetListener(this._currSoundBtn);
    }

    // Don't use a buffered output stream here, it was causing errors
    private void _saveProjectFile()
    {
        try {

            if(file_chooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, "Wrong File Type");
                throw new RuntimeException("No file");
            }

            FileOutputStream fos = new FileOutputStream(this._fileChooser.getSelectedFile() + ".sdb");
            ObjectOutputStream ois = new ObjectOutputStream( fos );
            ois.writeObject( Get_Project_Copy() );
        }
        catch(IOException ex) {
            System.out.println("File error:" + ex.getMessage());
        }
    }
}
