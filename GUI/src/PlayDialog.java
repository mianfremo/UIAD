import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PlayDialog extends JDialog implements ActionListener{
    private ArrayList<JLabel> labels;
    private JMenuItem loadImage, configLabels;
    private Image backgroundImage;
    private BackgroundPanel cuadro;

    public PlayDialog(JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        this.setLayout( new BorderLayout( 3,3 ));
        this.labels = new ArrayList<JLabel>();

        JMenuBar menuBar = new JMenuBar();  
        JMenu file = new JMenu("Archivo");
        JMenu control = new JMenu("Control");
        loadImage = new JMenuItem("Cargar Imágen");
        loadImage.addActionListener(this);
        configLabels = new JMenuItem("Configurar Etiquetas");
        configLabels.addActionListener(this);
        file.add(loadImage);
        file.add(configLabels);
        menuBar.add(file);
        menuBar.add(control);

        this.add(menuBar,"North");
        try {
            backgroundImage = javax.imageio.ImageIO.read(new File("icon/control.png"));
        } catch (Exception e) {
            //TODO: handle exception
        }

        this.cuadro = new BackgroundPanel(backgroundImage);  
        this.cuadro.setBackground(Color.WHITE);
        this.cuadro.setStyle(2);

        for (IO canal : Window.channels) {
            JLabel l = new JLabel();

            if(canal.getEnabled()){
                l.setText("Canal "+canal.getId()+": ");
                l.setPreferredSize(new Dimension(120,20));
                cuadro.add(l);
            }

            this.labels.add(l);
        }

        PositionsFile archivo = new PositionsFile();
        if(archivo.getExist() && !archivo.isDefault()){
            this.cuadro.setLayout(null);
            updateLabelPositions(archivo.getPosiciones());
        }else{
            this.cuadro.setLayout(new BoxLayout(this.cuadro, BoxLayout.Y_AXIS));
            this.cuadro.setBorder( BorderFactory.createEmptyBorder(5,5,5,5));
        }

        this.add(cuadro,"Center");

    }

    public void updateChannel(IO canal){
        String pattern = "#.###";
        DecimalFormat decimalFormat =  new DecimalFormat(pattern);

        double last = Window.medidas.get(canal.getId()-1)[299];
        labels.get(canal.getId()-1).setText("Canal "+canal.getId()+": "+decimalFormat.format(last)+" "+canal.getUnity().getNombre());
        
    }

    public void updateLabelPositions(ArrayList<Point> posiciones){
        for (int i = 0; i < Window.channels.size(); i++) {
            if(Window.channels.get(i).getEnabled()){
                Dimension d = labels.get(i).getPreferredSize();
                Point pos = posiciones.get(i);
                labels.get(i).setBounds((int)pos.getX(), (int)pos.getY(), (int)d.getWidth()+10, (int)d.getHeight());
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==configLabels){
            LabelsDialog etiquetas = new LabelsDialog(App.ventana, "Configurar vista de Planta", false);
            etiquetas.setVisible(true);
            this.dispose();
        }else if(e.getSource()==loadImage){
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog((JFrame)App.ventana) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    File actual = new File("icon/control.png");

                    actual.delete();

                    this.cuadro.setImage(javax.imageio.ImageIO.read(file));
                    
                    file.renameTo(new File("icon/control.png"));

                } catch (IOException ex) {
                    try {
                        this.cuadro.setImage(javax.imageio.ImageIO.read(file));
                        
                    } catch (Exception x) {
                        //TODO: handle exception
                    }

                    file.renameTo(new File("icon/control.png"));
                }
                
            }
        }
    }


}
