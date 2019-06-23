/**
 * Outer container of the soundboard side which displays the project title.
 *
 * created Nov 6, 2018
 */

import java.awt.*;
import javax.swing.*;


class SoundboardSide extends JPanel {

    private JLabel title;
    private SoundboardInner sboardInner;

    private static final String TITLE_TXT = "Press 'Enter' to stop sound      PROJECT_NAME: ";


    SoundboardSide(SoundboardInner sboardInner) {
        this.sboardInner = sboardInner;
        // Set title
        this.title = new JLabel(this.TITLE_TXT  + "New Project");
        this.title.setHorizontalAlignment(JLabel.CENTER);
        // Set scroll settings
        var yScroll = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        var xScroll = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        var scroller = new JScrollPane(this.sboardInner, yScroll, xScroll);
        super.setBackground(Color.white);
        super.setLayout(new BorderLayout());
        super.add(this.title, BorderLayout.NORTH);
        super.add(scroller, BorderLayout.CENTER);
    }


    void setProjectTitle() {
        super.remove(this.title);
        var prjTitle = this.sboardInner.getProjectTitle();
        this.title.setText(this.TITLE_TXT + prjTitle);
        super.add(this.title, BorderLayout.NORTH);
        super.repaint();
        super.revalidate();
    }
}
