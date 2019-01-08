/**
 * Starter file for the SoundBoard Maker project.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class ControllerSide extends JPanel implements ActionListener {

    private SoundboardInner _sboardInner;
    private SoundboardSide _sboardSide;

    private JButton _loadProjectBtn;
    private JButton _saveProjectBtn;
    private JButton _newProjectBtn;
    private JButton _newSoundBtn;
    private JButton _sortBtn;
    private JButton _renameProjectBtn;


    ControllerSide(SoundboardSide sboardOuter, SoundboardInner sboardInner) {

        this._sboardSide = sboardOuter;
        this._sboardInner = sboardInner;

        super.setLayout(new GridBagLayout());
        super.setBackground(Color.WHITE);

        this._initProjectCtlrBtns();
        this._initLayout();
    }



    // **************************************************************************************************** //
    //                                          Initialize Controller
    // **************************************************************************************************** //

    private void _initProjectCtlrBtns() {

        var c = this.getClass();

        var loadIcon = new ImageIcon(c.getResource("load.gif"));
        var saveIcon = new ImageIcon(c.getResource("save.gif"));
        var newProjectIcon = new ImageIcon(c.getResource("sdb.gif"));
        var newSoundIcon = new ImageIcon(c.getResource("quote.gif"));
        var sortIcon = new ImageIcon(c.getResource("sort.png"));
        var renameIcon = new ImageIcon(c.getResource("rename.png"));

        this._loadProjectBtn = new JButton("Load Project", loadIcon);
        this._saveProjectBtn = new JButton("Save", saveIcon);
        this._newProjectBtn = new JButton("New Project", newProjectIcon);
        this._newSoundBtn = new JButton("New Sound", newSoundIcon);
        this._sortBtn = new JButton("Sort Sounds", sortIcon);
        this._renameProjectBtn = new JButton("Rename Project", renameIcon);

        this._loadProjectBtn.addActionListener(this);
        this._newProjectBtn.addActionListener(this);
        this._newSoundBtn.addActionListener(this);
        this._renameProjectBtn.addActionListener(this);
        this._saveProjectBtn.addActionListener(this);
        this._sortBtn.addActionListener(this);
    }

    private void _initLayout() {

        var gridBag = new GridBagConstraints();
        var c = this.getClass();

        // Project Control Label
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.fill = GridBagConstraints.HORIZONTAL;

        var projectCtlrLabel = c.getResource("label1.png");
        var projectCtlrIcon = new ImageIcon(projectCtlrLabel);
        super.add(new JLabel(projectCtlrIcon), gridBag);

        // Project Control Buttons Container
        gridBag.gridx = 0;
        gridBag.gridy = 1;
        gridBag.weightx = 1;

        class ProjectCtlrPanel extends JPanel {
            ProjectCtlrPanel() {
                super.setLayout(new GridLayout(2,3));
                super.add(_loadProjectBtn);
                super.add(_saveProjectBtn);
                super.add(_newProjectBtn);
                super.add(_newSoundBtn);
                super.add(_sortBtn);
                super.add(_renameProjectBtn);
            }
        }
        super.add(new ProjectCtlrPanel(), gridBag);

        var sep = new JLabel();
        gridBag.gridx = 0;
        gridBag.gridy = 2;
        gridBag.weightx = 1;
        gridBag.weighty = 4;
        super.add(sep, gridBag);
        gridBag.weighty = 0;


        // Sound Control Label
        var soundCtlrLabel = c.getResource("sound_ctrl_label.png");
        var soundCtlrIcon = new ImageIcon(soundCtlrLabel);

        gridBag.gridx = 0;
        gridBag.gridy = 3;
        gridBag.weightx = 1;
        super.add(new JLabel(soundCtlrIcon), gridBag);

        // Sound Control Buttons Container
        gridBag.gridx = 0;
        gridBag.gridy = 4;
        gridBag.weightx = 1;
        super.add(new SoundController(this._sboardInner), gridBag);

        //  Title Box Container
        gridBag.gridx = 0;
        gridBag.gridy = 5;
        gridBag.weighty = 1;
        var titleBox = new TitleBox();
        super.add(titleBox, gridBag);
    }



    // **************************************************************************************************** //
    //                                        Listen for Button Events
    // **************************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent e) {

        var src = e.getSource();

        if (src == this._sortBtn) {
            this._sboardInner.sort();
        } else if (src == this._newSoundBtn) {
            this._sboardInner.newSound();
        } else if (src == this._saveProjectBtn) {
            this._sboardInner.saveProject();
            this._sboardSide.setProjectTitle();
        } else if (src == this._loadProjectBtn) {
            this._sboardInner.loadProject();
            this._sboardSide.setProjectTitle();
        } else if (src == this._newProjectBtn) {
            this._sboardInner.createNewProject();
            this._sboardSide.setProjectTitle();
        } else if (src == this._renameProjectBtn) {
            this._sboardInner.renameProject();
            this._sboardSide.setProjectTitle();
        }
    }
}
