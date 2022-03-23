import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        setLookAndFeel();
        
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        new MainFrame().setVisible(true);
    }
    
    /**
     * Causes any Swing components to be reskinned, looking more like a native
     * app on the user's system instead of Swing's ugly default L&F.
     * @return true if successful, otherwise false.
     */
    private static boolean setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            return true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            return false;
        }
    }

}