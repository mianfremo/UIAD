import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.io.File;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;


public class HelpTab extends JPanel implements ActionListener{
    private GridBagConstraints gbc;
    private JPanel helpPanel, aboutPanel;
    private JButton helpButton, aboutButton;
    private FlatSVGIcon helpSVG, aboutSVG;
    private CompoundIcon helpIcon, aboutIcon;
    private JLabel l1,l2;
    private AboutDialog aboutDialog;
    private HelpDialog helpDialog;

    public HelpTab(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        helpPanel = new JPanel();
        helpPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(1,1,1,1);
        gbc.gridx = 0;
        gbc.gridy = 0;

        helpButton = new JButton();
        helpSVG = new FlatSVGIcon(new File("icon/help.svg"));
        helpSVG = helpSVG.derive(35,35);
        helpSVG.setColorFilter(new ColorFilter(color -> new Color(51,153,255)));
        helpIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            helpSVG,
            new TextIcon(helpButton, "Ayuda"));
        helpButton.setIcon(helpIcon);
        helpButton.setPreferredSize(new Dimension(80,80));
        helpButton.addActionListener(this);
        helpPanel.add(helpButton,gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(8,0,0,0);
        l1 = new JLabel("Guia");
        l1.setEnabled(false);
        helpPanel.add(l1,gbc);

        this.add(helpPanel);

        this.add(new JSeparator(SwingConstants.VERTICAL));

        aboutPanel = new JPanel();
        aboutPanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(1,2,1,1);
        gbc.gridx = 0;
        gbc.gridy = 0;

        aboutButton = new JButton();
        aboutSVG = new FlatSVGIcon(new File("icon/about.svg"));
        aboutSVG = aboutSVG.derive(35,35);
        aboutSVG.setColorFilter(new ColorFilter(color -> new Color(0,0,153)));
        aboutIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            aboutSVG,
            new TextIcon(aboutButton, "Acerca de"));
        aboutButton.setIcon(aboutIcon);
        aboutButton.setPreferredSize(new Dimension(80,80));
        aboutButton.addActionListener(this);
        aboutPanel.add(aboutButton,gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(8,0,0,0);
        l2 = new JLabel("Información");
        l2.setEnabled(false);
        aboutPanel.add(l2,gbc);

        this.add(aboutPanel);

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==aboutButton){
            this.aboutDialog = new AboutDialog( (JFrame)SwingUtilities.getRoot(this), "Acerca de", true);
            this.aboutDialog.setVisible(true);
        }else if(e.getSource() == helpButton){
            this.helpDialog = new HelpDialog( (JFrame)SwingUtilities.getRoot(this), "Ayuda", true);
            this.helpDialog.setVisible(true);
        }
    }
}
