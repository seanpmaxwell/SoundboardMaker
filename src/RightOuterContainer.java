/**
 * Outer container of the soundboard side which displays the project title.
 *
 * created Nov 6, 2018
 */

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;


class RightOuterContainer extends JPanel {

    private JLabel title;
    private RightInnerContainer rightInnerContainer;

    private final String TITLE_TXT = "Press 'Enter' to stop sound      PROJECT_NAME: ";
    private static final long serialVersionUID = 1L;


    RightOuterContainer(RightInnerContainer rightInnerContainer) {
        this.rightInnerContainer = rightInnerContainer;
        // Set title
        this.title = new JLabel(this.TITLE_TXT  + "New Project");
        this.title.setHorizontalAlignment(JLabel.CENTER);
        // Set scroll settings
        int yScroll = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        int xScroll = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        var scroller = new JScrollPane(this.rightInnerContainer, yScroll, xScroll);
        // Set container layout
        super.setBackground(Color.white);
        super.setLayout(new BorderLayout());
        super.add(this.title, BorderLayout.NORTH);
        super.add(scroller, BorderLayout.CENTER);
    }


    void setProjectTitle() {
        super.remove(this.title);
        String title = this.rightInnerContainer.getProjectTitle();
        this.title.setText(this.TITLE_TXT + title);
        super.add(this.title, BorderLayout.NORTH);
        super.repaint();
        super.revalidate();
    }
}
