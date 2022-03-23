import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private ConwayWidget widget;

    public MainFrame() {
        super("Conway's Game of Life by LK");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        widget = new ConwayWidget();
        add(widget);

        Box horizontalBox = Box.createHorizontalBox();

        JButton pauseButton = new JButton("Start/Stop");
        pauseButton.addActionListener(this::pauseButtonPressed);
        Dimension preferred = pauseButton.getPreferredSize();
        pauseButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferred.height));

        horizontalBox.add(pauseButton);

        add(horizontalBox);

        pack();
        setResizable(false);

        new Thread(widget).start();
    }

    private void pauseButtonPressed(ActionEvent evt) {
        widget.toggle();
    }
    
}
