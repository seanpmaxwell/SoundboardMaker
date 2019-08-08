/**
 * Wrapper for the left side of the GUI which contains controls for creating new projects and 
 * sounds.
 *
 * created Nov 6, 2018
 */

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


class LeftContainer extends JPanel implements ActionListener {

    private RightOuterContainer rightOuterContainer;
    private RightInnerContainer rightInnerContainer;

    private JButton loadProjectBtn;
    private JButton saveProjectBtn;
    private JButton newProjectBtn;
    private JButton newSoundBtn;
    private JButton sortBtn;
    private JButton renameProjectBtn;

    private static final long serialVersionUID = 1L;


    LeftContainer(RightOuterContainer rightOuterContainer, 
        RightInnerContainer rightInnerContainer) {
        this.rightOuterContainer = rightOuterContainer;
        this.rightInnerContainer = rightInnerContainer;
        this.initProjectCtlrBtns();
        this.initLayout();
    }


    // ***************************************************************************************** //
    //                                 Initialize Controller
    // ***************************************************************************************** //

    private void initProjectCtlrBtns() {
        // Init Image Icons
        ImageIcon loadIcon = this.getImgIcon("load.gif");
        ImageIcon saveIcon = this.getImgIcon("save.gif");
        ImageIcon newProjectIcon = this.getImgIcon("sdb.gif");
        ImageIcon newSoundIcon = this.getImgIcon("quote.gif");
        ImageIcon sortIcon = this.getImgIcon("sort.png");
        ImageIcon renameIcon = this.getImgIcon("rename.png");
        // Init Buttons
        this.loadProjectBtn = new JButton("Load Project", loadIcon);
        this.saveProjectBtn = new JButton("Save", saveIcon);
        this.newProjectBtn = new JButton("New Project", newProjectIcon);
        this.newSoundBtn = new JButton("New Sound", newSoundIcon);
        this.sortBtn = new JButton("Sort Sounds", sortIcon);
        this.renameProjectBtn = new JButton("Rename Project", renameIcon);
        // Init Actions Listeners
        this.loadProjectBtn.addActionListener(this);
        this.newProjectBtn.addActionListener(this);
        this.newSoundBtn.addActionListener(this);
        this.renameProjectBtn.addActionListener(this);
        this.saveProjectBtn.addActionListener(this);
        this.sortBtn.addActionListener(this);
    }


    private void initLayout() {
        super.setLayout(new GridBagLayout());
        super.setBackground(Color.WHITE);
        var gridBag = new GridBagConstraints();
        // Project Control Label
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        ImageIcon projectCtlrIcon = new ImageIcon("label1.png");
        super.add(new JLabel(projectCtlrIcon), gridBag);
        // Project Control Buttons Container
        gridBag.gridx = 0;
        gridBag.gridy = 1;
        gridBag.weightx = 1;
        super.add(new ProjectCtlrPanel(), gridBag);
        var sep = new JLabel();
        gridBag.gridx = 0;
        gridBag.gridy = 2;
        gridBag.weightx = 1;
        gridBag.weighty = 4;
        super.add(sep, gridBag);
        gridBag.weighty = 0;
        // Sound Control Label
        ImageIcon soundCtlrIcon = this.getImgIcon("sound_ctrl_label.png");
        gridBag.gridx = 0;
        gridBag.gridy = 3;
        gridBag.weightx = 1;
        super.add(new JLabel(soundCtlrIcon), gridBag);
        // Sound Control Buttons Container
        gridBag.gridx = 0;
        gridBag.gridy = 4;
        gridBag.weightx = 1;
        super.add(new SoundController(this.rightInnerContainer), gridBag);
        //  Title Box Container
        gridBag.gridx = 0;
        gridBag.gridy = 5;
        gridBag.weighty = 1;
        var titleBox = new TitleBox();
        super.add(titleBox, gridBag);
    }

    private class ProjectCtlrPanel extends JPanel {

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


    // ***************************************************************************************** //
    //                                 Listen for Button Events
    // ***************************************************************************************** //

    @Override
    public void actionPerformed(ActionEvent event) {
        Object src = event.getSource();
        if (src == this.sortBtn) {
            this.rightInnerContainer.sort();
        } else if (src == this.newSoundBtn) {
            this.rightInnerContainer.newSound();
        } else if (src == this.saveProjectBtn) {
            this.rightInnerContainer.saveProject();
            this.rightOuterContainer.setProjectTitle();
        } else if (src == this.loadProjectBtn) {
            this.rightInnerContainer.loadProject();
            this.rightOuterContainer.setProjectTitle();
        } else if (src == this.newProjectBtn) {
            this.rightInnerContainer.createNewProject();
            this.rightOuterContainer.setProjectTitle();
        } else if (src == this.renameProjectBtn) {
            this.rightInnerContainer.renameProject();
            this.rightOuterContainer.setProjectTitle();
        }
    }


    // ***************************************************************************************** //
    //                                      Helpers
    // ***************************************************************************************** //

    private ImageIcon getImgIcon(String name) {
        return new ImageIcon(this.getClass().getResource("res/" + name));
    }
}
