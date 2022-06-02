import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Point;
import java.awt.Image;
import java.io.File;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;

public class LabelsDialog extends JDialog implements ActionListener{
    private JButton b1;
    private JLabel l1;
    private ArrayList<PlayLabel> labels;
    private BackgroundPanel content;
    private Image backgroundImage;

    public LabelsDialog(JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        this.setLayout( new BorderLayout( 3,3 ));
        this.labels = new ArrayList<PlayLabel>();

        b1 = new JButton("Guardar Cambios");
        b1.addActionListener(this);

        l1 = new JLabel("Arrastra y suelta las etiquetas donde deseas que se muestren");

        try {
            backgroundImage = javax.imageio.ImageIO.read(new File("icon/control.png"));
        } catch (Exception e) {
            //TODO: handle exception
        }

        content = new BackgroundPanel(backgroundImage);
        content.setStyle(2);
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder( BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        footer.add(b1,"East");
        footer.add(l1, "West");
        

        for (IO canal : Window.channels) {
            if(canal.getEnabled()){
                PlayLabel etiqueta = new PlayLabel("Canal "+canal.getId()+" :");
                labels.add(etiqueta);
                content.add(etiqueta);
            }else{
                labels.add(new PlayLabel(""));
            }
        }
        
        this.add(content, "Center");
        this.add(footer, "South");
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==b1){
            this.updatePosition();
            this.dispose();

            PlayDialog planta = new PlayDialog(App.ventana, "Mediciones en vivo", true);
            planta.setVisible(true);
        }
    }

    public void updatePosition(){
        ArrayList<Point> posiciones = new ArrayList<Point>();

        for (int i = 0; i < Window.channels.size(); i++) {
            IO canal = Window.channels.get(i);

            if(canal.getEnabled()){
                posiciones.add(new Point(labels.get(i).getX(),labels.get(i).getY()));
            }else{
                posiciones.add(new Point(0,0));
            }
        }

        PositionsFile archivo = new PositionsFile();
        archivo.updateFile(posiciones);
        
    }

}