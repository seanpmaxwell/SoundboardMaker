/**
 * Starter file for the SoundBoard Maker project.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class ControllerSide extends JPanel implements ActionListener {

    private SoundboardInner sboardInner;
    private SoundboardSide sboardSide;

    private JButton loadProjectBtn;
    private JButton saveProjectBtn;
    private JButton newProjectBtn;
    private JButton newSoundBtn;
    private JButton sortBtn;
    private JButton renameProjectBtn;


    ControllerSide(SoundboardSide sboardOuter, SoundboardInner sboardInner) {
        this.sboardSide = sboardOuter;
        this.sboardInner = sboardInner;
        super.setLayout(new GridBagLayout());
        super.setBackground(Color.WHITE);
        this.initProjectCtlrBtns();
        this.initLayout();
    }


    // **************************************************************************************************** //
    //                                          Initialize Controller
    // **************************************************************************************************** //

    private void initProjectCtlrBtns() {
        var c = this.getClass();
        var loadIcon = new ImageIcon(c.getResource("load.gif"));
        var saveIcon = new ImageIcon(c.getResource("save.gif"));
        var newProjectIcon = new ImageIcon(c.getResource("sdb.gif"));
        var newSoundIcon = new ImageIcon(c.getResource("quote.gif"));
        var sortIcon = new ImageIcon(c.getResource("sort.png"));
        var renameIcon = new ImageIcon(c.getResource("rename.png"));
        this.loadProjectBtn = new JButton("Load Project", loadIcon);
        this.saveProjectBtn = new JButton("Save", saveIcon);
        this.newProjectBtn = new JButton("New Project", newProjectIcon);
        this.newSoundBtn = new JButton("New Sound", newSoundIcon);
        this.sortBtn = new JButton("Sort Sounds", sortIcon);
        this.renameProjectBtn = new JButton("Rename Project", renameIcon);
        this.loadProjectBtn.addActionListener(this);
        this.newProjectBtn.addActionListener(this);
        this.newSoundBtn.addActionListener(this);
        this.renameProjectBtn.addActionListener(this);
        this.saveProjectBtn.addActionListener(this);
        this.sortBtn.addActionListener(this);
    }


    private void initLayout() {
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
                super.add(loadProjectBtn);
                super.add(saveProjectBtn);
                super.add(newProjectBtn);
                super.add(newSoundBtn);
                super.add(sortBtn);
                super.add(renameProjectBtn);
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
        super.add(new SoundController(this.sboardInner), gridBag);
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
        if (src == this.sortBtn) {
            this.sboardInner.sort();
        } else if (src == this.newSoundBtn) {
            this.sboardInner.newSound();
        } else if (src == this.saveProjectBtn) {
            this.sboardInner.saveProject();
            this.sboardSide.setProjectTitle();
        } else if (src == this.loadProjectBtn) {
            this.sboardInner.loadProject();
            this.sboardSide.setProjectTitle();
        } else if (src == this.newProjectBtn) {
            this.sboardInner.createNewProject();
            this.sboardSide.setProjectTitle();
        } else if (src == this.renameProjectBtn) {
            this.sboardInner.renameProject();
            this.sboardSide.setProjectTitle();
        }
    }
}
