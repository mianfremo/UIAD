import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.Color;

public class TabsMenu extends JTabbedPane{
    private ArrayList<JPanel> tabs;
    private ChannelsTab channels;

    public TabsMenu(){
        this.setPreferredSize(new Dimension(1280,150));
        tabs = new ArrayList<JPanel>();

        
        FileTab p1 = new FileTab();
        tabs.add(p1);

        channels = new ChannelsTab();
        tabs.add(channels);

        
        HelpTab p5 = new HelpTab();
        tabs.add(p5);
        
        this.add("Archivo",p1);  
        this.add("Canales de la UIAD",channels);  
        this.add("Ayuda",p5);
        this.setSelectedIndex(1);
        this.setBackgroundAt(0, new Color(60,131,197));
 
    }

    public ChannelsTab getChannels() {
        return channels;
    }

}
