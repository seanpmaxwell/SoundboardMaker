/**
 * Outer container of the soundboard side which displays the project title.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import javax.swing.*;


class SoundboardSide extends JPanel
{
    private JLabel _title;
    private SoundboardInner _sboardInner;

    private static final String _TITLE_TXT = "Press 'Enter' to stop sound      PROJECT_NAME: ";


    SoundboardSide(SoundboardInner sboardInner)
    {
        this._sboardInner = sboardInner;

        // Set title
        this._title = new JLabel(_TITLE_TXT  + "New Project");
        this._title.setHorizontalAlignment(JLabel.CENTER);

        // Set scroll settings
        var yScroll = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        var xScroll = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        var scroller = new JScrollPane(this._sboardInner, yScroll, xScroll);

        super.setBackground(Color.white);
        super.setLayout(new BorderLayout());
        super.add(this._title, BorderLayout.NORTH);
        super.add(scroller, BorderLayout.CENTER);
    }

    void setProjectTitle()
    {
        super.remove(this._title);

        var prjTitle = this._sboardInner.getProjectTitle();
        this._title.setText(_TITLE_TXT + prjTitle);

        super.add(this._title, BorderLayout.NORTH);
        super.repaint();
        super.revalidate();
    }
}
