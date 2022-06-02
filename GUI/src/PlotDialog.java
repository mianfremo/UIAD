import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class PlotDialog extends JDialog implements ActionListener{
    private XYChart chart;
    private JButton b1;
    private JPanel chartPanel;
    private JMenuItem lista, restablecer, exportar;
    private double[] xData = new double[1];
    private double[] yData = new double[1];

    public PlotDialog(JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout( 3,3 ));

        JMenuBar menuBar = new JMenuBar();  
        JMenu plot = new JMenu("Gráfica");
        JMenu control = new JMenu("Control");
        lista = new JMenuItem("Lista de Controladores");
        lista.addActionListener(this);
        
        restablecer = new JMenuItem("Restablecer Gráfica");
        restablecer.addActionListener(this);

        exportar = new JMenuItem("Exportar Gráfica");
        exportar.addActionListener(this);

        control.add(lista);
        plot.add(restablecer);
        plot.add(exportar);
        menuBar.add(plot);
        menuBar.add(control);



        this.add(menuBar,"North");

        chart = new XYChart(700, 400,ChartTheme.Matlab);
        String pattern = "#.##";

        DecimalFormat decimalFormat =  new DecimalFormat(pattern);

        Function<Double, String> setFormat = a -> decimalFormat.format(a);

        chart.setCustomXAxisTickLabelsFormatter(setFormat);
        this.addSeries();       

        chartPanel = new XChartPanel(chart);
        this.add(chartPanel, "Center");

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                chart = null;
            }
        });

        xData[0] = 0.0;

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==b1){
        }else if(e.getSource()== lista ){
            if(!App.ventana.getTb().getChannels().getMeassuring()){
                ControllerListDialog cld = new ControllerListDialog(null, "Lista de Controladores", true);
                cld.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null,"No puede realizar modificaciones en los controladores mientras la simulación está en proceso","Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(e.getSource()==restablecer){
            if(Window.listener!=null){
                ArrayList<XYSeries> series = new ArrayList<XYSeries>(chart.getSeriesMap().values());
                xData = new double[]{0.0};
                yData = new double[]{0.0};
    
                for (XYSeries serie : series) {
                    chart.updateXYSeries(serie.getName(), xData,yData,null);
                }

                chartPanel.repaint();
    
                Window.listener.setCounter(1);
            }else{
                JOptionPane.showMessageDialog(null,"No puedes reestablecer la gráfica si no hay datos para restablecer","Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(e.getSource()==exportar){
            CSVExporter exportador = new CSVExporter();

            exportador.writeCSVRows(chart, System.getProperty("user.dir"));
        }
    }

    public void updateChart(IO canal, int counter){

        yData = Window.medidas.get(canal.getId()-1).clone();
        
        if(counter> 300){

            double[] copia = new double[300];
            
            System.arraycopy(xData, 1, copia,  0, xData.length-1);
            
            xData = copia.clone();
            
            xData[xData.length-1] = xData[xData.length-2]+(1/(double)Window.samples);
            
            chart.updateXYSeries(canal.getNombre(), xData,yData,null);
            chartPanel.repaint();

        }else{
            double[] copia = new double[counter];

            System.arraycopy(yData, yData.length-counter,copia,  0, counter);

            yData = copia.clone();

            if(counter>1){
                copia = xData.clone();

                xData = new double[counter];

                System.arraycopy(copia, 0, xData,  0, copia.length);    
                
                xData[xData.length-1] = xData[xData.length-2]+(1/(double)Window.samples);
            }


            chart.updateXYSeries(canal.getNombre(), xData,yData,null);
            chartPanel.repaint();

        }
        
         
    }

    public void addSeries(){
        
        Map<String,XYSeries> mapa = chart.getSeriesMap();
        ArrayList <String> series = new ArrayList<String>();
        ArrayList <String> habilitados = new ArrayList<String>();

        for (IO canal : Window.channels) {
            if(canal.getEnabled() && canal.getIo().getType()==0){
                habilitados.add(canal.getNombre());
            }
        }

        for (XYSeries serie : mapa.values()) {
            series.add(serie.getName());
        }

        if(habilitados.size()>series.size()){
            habilitados.removeAll(series);

            for (String string : habilitados) {
                XYSeries s = chart.addSeries(string, xData, yData);
                s.setMarker(SeriesMarkers.NONE);
            }
        }else if(habilitados.size()<series.size()){
            series.removeAll(habilitados);
            for (String string : series) {
                chart.removeSeries(string);
            }
        }else{
            ArrayList<String> copia = new ArrayList<String>();
            copia = (ArrayList<String>) series.clone();
            series.removeAll(habilitados);

            if(!series.isEmpty()){
                

                for (String string : series) {
                    chart.removeSeries(string);
                }

                habilitados.removeAll(copia);

                for (String string : habilitados) {
                    XYSeries s = chart.addSeries(string, xData, yData);
                    s.setMarker(SeriesMarkers.NONE);

                }
            }

            

        }


        


    }

    public JPanel getChartPanel() {
        return chartPanel;
    }

}
