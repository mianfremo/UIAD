import javax.swing.JFrame;
import javax.swing.JScrollPane;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.*;
import java.util.ArrayList;

public class Window extends JFrame{
    static final IOTable tabla = new IOTable(new IOTableModel());
    static int connection = 4;
    static int samples = 1;
    static SerialPort puerto;
    static MessageListener listener;
    static ArrayList<IO> channels;
    static ArrayList<double[]> medidas = new ArrayList<double[]>();
    static ArrayList<Control> controllers = new ArrayList<Control>();


    private FooterPane footer;
    private JScrollPane sp;
    private TabsMenu tb;
    private SidePane side;

    public Window(){
        super("Interfaz Gráfica de la UIAD");
        this.setSize(1280,720);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        Image icono = Toolkit.getDefaultToolkit().getImage("icon/icon.png"); //Icono de la Aplicación
        this.setIconImage(icono);

        //Definiendo el Layout a utilizar
        BorderLayout bl = new BorderLayout( 3,3 );
        this.setLayout(bl);

        
        this.sp = new JScrollPane(tabla);
        this.sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);  
        this.add(this.sp,"Center");

        
        ControlFile file = new ControlFile();
        

        //Creando el panel con los tabs
        this.tb = new TabsMenu();
        this.add( this.tb,"North" );


        this.footer = new FooterPane();
        this.add(this.footer,"South" );


        this.side = new SidePane();
        this.add( this.side,"East" );

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if(puerto!=null){
                    puerto.closePort();
                }
            }
        });

        for (int i = 0; i < 8; i++) {
            medidas.add(new double[300]);
        }

       
    }

    public FooterPane getFooter() {
        return footer;
    }

    public TabsMenu getTb() {
        return tb;
    }

    public SidePane getSide() {
        return side;
    }




}