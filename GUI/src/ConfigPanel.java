import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Color;


public class ConfigPanel extends JPanel implements ActionListener{
    private GridBagConstraints gbc;
    private JButton configButton;
    private JLabel l1;
    private ConfigDialog configDialog;
    private FlatSVGIcon configSVG;
    private CompoundIcon configIcon;

    public ConfigPanel(){
        this.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(1,4,1,1);
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        configSVG = new FlatSVGIcon(new File("icon/config.svg"));
        configSVG = configSVG.derive(35,35);
        configSVG.setColorFilter(new ColorFilter(color -> new Color(204,204,204)));
        configButton = new JButton();
        configIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            configSVG,
            new TextIcon(configButton, "UIAD"));
        configButton.setIcon(configIcon);
        configButton.setPreferredSize(new Dimension(80,80));
        configButton.addActionListener(this);
        this.add(configButton,gbc);

        gbc.gridheight = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(8,0,0,0);

        l1 = new JLabel("Configuración");
        l1.setEnabled(false);
        this.add(l1,gbc);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==configButton){
            this.configDialog = new ConfigDialog((JFrame)App.ventana, "Configuración de la UIAD", true);
            this.configDialog.setVisible(true);
        }
    }
}
