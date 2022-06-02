import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Color;
import javax.swing.Timer;


public class ChannelsTab extends JPanel implements ActionListener{
    private JPanel medidas;
    private ConfigPanel config;
    private SensorPanel sensor;
    private VisualizationPanel visual;
    private JLabel l1;
    private JButton playButton;
    private GridBagConstraints gbc;
    private CompoundIcon playIcon;
    private FlatSVGIcon playSVG, pauseSVG;
    private Timer sensing;
    private boolean meassuring;

    public ChannelsTab(){      

        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.meassuring = false;

        medidas = new JPanel();
        medidas.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(5,1,1,1);  //Espaciado entre elementos
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        playButton = new JButton();
        pauseSVG = new FlatSVGIcon(new File("icon/pause.svg"));
        pauseSVG = pauseSVG.derive(20,30);
        pauseSVG.setColorFilter( new ColorFilter(color -> new Color(24,49,83)));

        playSVG = new FlatSVGIcon(new File("icon/play.svg"));
        playSVG = playSVG.derive(20,30);

        playSVG.setColorFilter( new ColorFilter(color -> new Color(0,102,0)));
        playIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            playSVG,
            new TextIcon(playButton, "Iniciar"));
        playButton.setIcon(playIcon);
        playButton.setPreferredSize(new Dimension(80,80));
        playButton.addActionListener(this);
        medidas.add(playButton,gbc);

        gbc.gridy = 3;

        l1 = new JLabel("Medidas");
        l1.setEnabled(false);
        l1.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(8,0,0,0);
        medidas.add(l1, gbc);

        this.add(medidas);

        this.add(new JSeparator(SwingConstants.VERTICAL));

        sensor = new SensorPanel();
        this.add(sensor);

        this.add(new JSeparator(SwingConstants.VERTICAL));

        visual = new VisualizationPanel();
        this.add(visual);
        
        this.add(new JSeparator(SwingConstants.VERTICAL));

        config = new ConfigPanel();
        this.add(config);

        this.sensing = new Timer (1000/Window.samples, new ActionListener ()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String message = "02,";

                for (int i = 0; i < 8; i++) {
                    IO canal = Window.channels.get(i);
                    message = message + Integer.toString(i);
        
                    if(canal.getEnabled()){
                        message+= "1";
                    }else{
                        message+= "0";
                    }
        
                    if(i!=7){
                        message+="&";
                    }
                }
                message+="\n";
        
                byte[] msg = message.getBytes();

        
                Window.puerto.writeBytes(msg,27);



            }
        });



    }

    public void actionPerformed(ActionEvent e){
            if(e.getSource() == playButton){
                Boolean flag = true;
                for (IO canal : Window.channels) {
                    if(canal.getEnabled() && canal.getState()!=1){
                        flag = false;
                    }
                }
                if(flag){
                    if(!this.sensing.isRunning()){
                        this.sensing.start();

                        this.meassuring = true;
    
                        playIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
                            pauseSVG,
                            new TextIcon(playButton, "Detener"));
                        playButton.setIcon(playIcon);
    
                        PlayDialog playDialog = new PlayDialog(App.ventana, "Mediciones en vivo", false);
                        PlotDialog plotDialog = visual.getPlotDialog();
                        Window.listener.setCaller(playDialog,0);
                        Window.listener.setCaller(plotDialog, 1);
                        playDialog.setVisible(true);
    
                    }else{
                        this.meassuring = false;

                        this.sensing.stop();
                        playIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
                            playSVG,
                            new TextIcon(playButton, "Iniciar"));
                        playButton.setIcon(playIcon);
                        
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Se debe realizar el diagnóstico antes de iniciar las mediciones","Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            }    

    }

    public SensorPanel getSensor() {
        return sensor;
    }

    public JButton getPlayButton() {
        return playButton;
    }
    
    public boolean getMeassuring(){
        return this.meassuring;
    }

}
