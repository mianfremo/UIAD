import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.SwingUtilities;

public class VisualizationPanel extends JPanel implements ActionListener {
    private JLabel l1;
    private JButton plantButton, plotButton;
    private GridBagConstraints gbc;
    private FlatSVGIcon plantSVG, plotSVG;
    private CompoundIcon plantIcon, plotIcon;
    private PlotDialog plotDialog;
    private PlayDialog playDialog;

    public VisualizationPanel(){

        plotDialog = new PlotDialog((JFrame)App.ventana, "Mediciones en vivo", false);

        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(1,5,1,1);  //Espaciado entre elementos


        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;

        plantSVG = new FlatSVGIcon(new File("icon/plant.svg"));
        plantSVG = plantSVG.derive(30,30);
        plantButton = new JButton();
        plantIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
        plantSVG,
            new TextIcon(plantButton, "Planta"));
        plantButton.setIcon(plantIcon);
        plantButton.setPreferredSize(new Dimension(80,80));
        plantButton.addActionListener(this);
        this.add(plantButton,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(1,4,1,1);  //Espaciado entre elementos
        plotSVG = new FlatSVGIcon(new File("icon/plot.svg"));
        plotSVG = plotSVG.derive(30,30);
        plotSVG.setColorFilter(new ColorFilter(color -> new Color(183,0,0)));
        plotButton = new JButton();
        plotIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            plotSVG,
            new TextIcon(plotButton, "Curvas"));
        plotButton.setIcon(plotIcon);
        plotButton.setPreferredSize(new Dimension(80,80));
        plotButton.addActionListener(this);
        this.add(plotButton,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(8,0,0,0);
        l1 = new JLabel("Monitoreo");
        l1.setEnabled(false);
        l1.setHorizontalAlignment(JLabel.CENTER);
        this.add(l1,gbc);


    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==plantButton){
            System.out.println(Window.channels.get(0).getController());
            playDialog = new PlayDialog((JFrame)SwingUtilities.getRoot(this), "Mediciones en vivo", false);
            if(Window.listener!=null){
                Window.listener.setCaller(playDialog,0);
            }
            playDialog.setVisible(true);
        }else if(e.getSource()==plotButton){
            plotDialog.addSeries();
            plotDialog.getChartPanel().repaint();
            plotDialog.setVisible(true);
            if(Window.listener!=null){
                Window.listener.setCaller(plotDialog,1);
            }
        }
    }

    public PlotDialog getPlotDialog() {
        return plotDialog;
    }
}
