import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    static Window ventana;
    public static void main(String[] args) throws Exception {
        FlatLightLaf.setup();

        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
        } catch(Exception ignored){}
        
        
        ventana = new Window();
        ventana.setVisible(true);

    }
}
