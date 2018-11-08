/**
 * Starter file for the SoundBoard Maker project.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class ControllerSide extends JPanel implements ActionListener
{
    private SoundboardInner _sboardInner;
	private SoundboardSide _sboardSide;
    private TitleBox _titleBox;

    private ImageIcon _projectCtlrLabel;
    private ImageIcon _soundCtlrLabel;
	private ImageIcon _loadIcon;
	private ImageIcon _saveIcon;
	private ImageIcon _newProjectIcon;
	private ImageIcon _newSoundIcon;
    private ImageIcon _sortIcon;
    private ImageIcon _renameIcon;

	private JButton _loadProjectBtn;
	private JButton _saveProjectBtn;
	private JButton _newProjectBtn;
	private JButton _newSoundBtn;
	private JButton _sortBtn;
	private JButton _renameProjectBtn;

    private GridBagConstraints _gridBag;


	ControllerSide(SoundboardSide sboardOuter, SoundboardInner sboardInner)
	{
        this._sboardSide = sboardOuter;
        this._sboardInner = sboardInner;

        super.setLayout(new GridBagLayout());
        super.setBackground(Color.WHITE);
        this._gridBag = new GridBagConstraints();

        this._initProjectCtlrBtns();
        this._initProjectCtlr();
        this._initSoundCtlr();
        this._initTitleBox();
	}

    private void _initProjectCtlrBtns()
    {
        var c = this.getClass();

        var loadIcon = new ImageIcon(c.getResource("load.gif"));
        var saveIcon = new ImageIcon(c.getResource("save.gif"));
        var newProjectIcon = new ImageIcon(c.getResource("sdb.gif"));
        var newSoundIcon = new ImageIcon(c.getResource("quote.gif"));
        var sortIcon = new ImageIcon(c.getResource("sort.png"));
        var renameIcon = new ImageIcon(c.getResource("rename.png"));

        this._loadProjectBtn = new JButton("Load", loadIcon);
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

    private void _initProjectCtlr()
    {
        this._gridBag.gridx = 0;
        this._gridBag.gridy = 0;
        this._gridBag.fill = GridBagConstraints.HORIZONTAL;

        var label = this.getClass().getResource("label1.png");
        this._projectCtlrLabel = new ImageIcon(label);
        super.add(new JLabel(this._projectCtlrLabel), this._gridBag);

        this._gridBag.gridx = 0;
        this._gridBag.gridy = 1;
        this._gridBag.weightx = 1;

        super.add(new ProjectCtlrPanel(), this._gridBag);
    }

    private void _initSoundCtlr()
    {
        var label = this.getClass().getResource("sound_ctrl_label.png");
        this._soundCtlrLabel = new ImageIcon(label);

        this._gridBag.gridx = 0;
        this._gridBag.gridy = 2;
        this._gridBag.weightx = 1;
        super.add(new JLabel(this._soundCtlrLabel), this._gridBag);

        this._gridBag.gridx = 0;
        this._gridBag.gridy = 3;
        this._gridBag.weightx = 1;
        this._gridBag.weightx = 1;
        super.add(new SoundController(this._sboardInner), this._gridBag);
    }

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    var src = e.getSource();

        if(src == this._sortBtn) {
            this._sboardInner.sort();
        }
		else if(src == this._newSoundBtn) {
            this._sboardInner.newSound();
        }
		else if(src == this._saveProjectBtn) {
            this._sboardInner.saveProject();
        }
		else if(src == this._loadProjectBtn) {
            this._sboardInner.loadProject();
        }
        else if(src == this._newProjectBtn) {
            this._sboardInner.newProject();
        }
		else if(src == this._renameProjectBtn) {
            this._sboardInner.renameProject();
        }

        if(src == this._loadProjectBtn || src == this._newProjectBtn || src == this._renameProjectBtn) {
            this._sboardSide.SetTitle();
        }
	}

    private void _initTitleBox()
    {
        this._gridBag.gridx = 0;
        this._gridBag.gridy = 4;
        this._gridBag.weighty = 1;

        this._titleBox = new TitleBox();
        super.add(this._titleBox, this._gridBag);
    }

    private class ProjectCtlrPanel extends JPanel
    {
        ProjectCtlrPanel()
        {
            super.setLayout(new GridLayout(2,3));

            super.add(_loadProjectBtn);
            super.add(_saveProjectBtn);
            super.add(_newProjectBtn);
            super.add(_newSoundBtn);
            super.add(_sortBtn);
            super.add(_renameProjectBtn);
        }
    }
}
