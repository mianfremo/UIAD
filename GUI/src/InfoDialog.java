import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;



public class InfoDialog extends JDialog{
    private JLabel l1;

    public InfoDialog(JFrame f, String nombre, Boolean modal){
        super(f,nombre,modal);
        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        l1 = new JLabel("<html><p align=\"justify\">El archivo channels.csv contiene la información de los canales de la UIAD, si desea emplear la misma configuración en otro dispositivo es posible exportar el archivo mediante el botón correspondiente.</p><br>"+
        "<center><img src=\""+new File("icon/export.png").toURI()+"\"></center><br>"+
        "<p align=\"justify\">En cambio si desea utilizar una configuración existente puede utilizar el botón 'Cargar'.</p><br>"+
        "<center><img src=\""+new File("icon/load.png").toURI()+"\"></center>"+
        "</html>");
        l1.setPreferredSize(new Dimension(350, 350));

        this.add(l1);

    }
}
