import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.awt.Container;
import java.awt.Font;

public class AboutDialog extends JDialog{
    private GridBagConstraints gbc;
    private FlatSVGIcon ujapIcon;
    private Container co;
    private JLabel l1, l2, l3, l4, l5, l6;

    public AboutDialog(JFrame f, String nombre, Boolean modal){
        super(f,nombre,modal);
        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        co = this.getContentPane();
        co.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(1,1,1,1);
        ujapIcon = new FlatSVGIcon(new File("icon/icon.svg"));
        ujapIcon = ujapIcon.derive(130,130);
        l1 = new JLabel(ujapIcon);
        co.add(l1,gbc);

        gbc.gridy = 1;
        l2 = new JLabel("Interfaz Gráfica de la UIAD");
        l2.setFont(new Font(l1.getFont().getFamily(), Font.BOLD, 20));
        co.add(l2,gbc);

        gbc.gridy = 2;
        l3 = new JLabel("Requisito para optar al Título de Ingeniero Electrónico");
        l3.setFont(new Font(l1.getFont().getFamily(), Font.PLAIN, 14));
        co.add(l3,gbc);

        gbc.insets = new Insets(8,1,1,1);
        gbc.gridy = 3;
        l4 = new JLabel("Copyright © 2022 Miguel Freytes");
        l4.setEnabled(false);
        co.add(l4,gbc);

        gbc.gridy = 4;
        l5 = new JLabel("Versión 1.0.0");
        l5.setEnabled(false);
        co.add(l5,gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(1,1,1,1);
        l6 = new JLabel("Todos los derechos reservados");
        l6.setEnabled(false);
        co.add(l6,gbc);
        
    }
}
