import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;

public class FileTab extends JPanel implements ActionListener{
    private GridBagConstraints gbc;
    private JPanel configFilePanel;
    private FlatSVGIcon newFileSVG, loadFileSVG, exportFileSVG, infoSVG;
    private CompoundIcon loadFileIcon;
    private JButton newFileButton, loadFileButton, exportFileButton, infoButton;
    private JLabel l1;

    public FileTab() {        
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        configFilePanel = new JPanel();
        configFilePanel.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(1,3,1,3);


        gbc.gridheight = 3;
        gbc.gridwidth = 1;

        loadFileButton = new JButton();
        loadFileSVG = new FlatSVGIcon(new File("icon/loadFile.svg"));
        loadFileSVG = loadFileSVG.derive(25,25);
        loadFileSVG.setColorFilter(new ColorFilter(color -> new Color(255,204,51)));
        loadFileIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS, 
            loadFileSVG,
            new TextIcon(loadFileButton, "Cargar"));
        loadFileButton.setIcon(loadFileIcon);
        loadFileButton.setPreferredSize(new Dimension(80,80));
        loadFileButton.addActionListener(this);

        configFilePanel.add(loadFileButton,gbc);

        gbc.gridx = 1;
        gbc.gridheight = 1;

        exportFileButton = new JButton("Exportar Archivo");
        exportFileSVG = new FlatSVGIcon(new File("icon/exportFile.svg"));
        exportFileSVG = exportFileSVG.derive(15,15);
        exportFileButton.setIcon(exportFileSVG);
        exportFileButton.addActionListener(this);

        configFilePanel.add(exportFileButton,gbc);

        gbc.gridy = 1;

        newFileButton = new JButton("Nuevo Archivo");
        newFileSVG = new FlatSVGIcon(new File("icon/newFile.svg"));
        newFileSVG = newFileSVG.derive(15,15);
        newFileButton.setIcon(newFileSVG);
        newFileButton.addActionListener(this);

        configFilePanel.add(newFileButton,gbc);

        gbc.gridy = 2;
        infoButton = new JButton("Información");
        infoSVG = new FlatSVGIcon(new File("icon/about.svg"));
        infoSVG = infoSVG.derive(15,15);
        infoButton.setIcon(infoSVG);
        infoButton.addActionListener(this);
        configFilePanel.add(infoButton,gbc);

        gbc.insets = new Insets(8,3,3,3);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        l1 = new JLabel("Archivo de Configuración");
        l1.setHorizontalAlignment(JLabel.CENTER);
        l1.setEnabled(false);
        
        configFilePanel.add(l1,gbc);

        this.add(configFilePanel);


    }

    public void actionPerformed(ActionEvent e){
        ChannelsFile channelsFile = new ChannelsFile();
        IOTableModel dtm = (IOTableModel)Window.tabla.getModel();

        if(e.getSource() == newFileButton){

            int result = JOptionPane.showConfirmDialog((JFrame)SwingUtilities.getRoot(this), "Estás seguro que deseas reestablecer el archivo de configuración de la UIAD?", "Nuevo Archivo de Configuración", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(result==0){
                channelsFile.createFile();
                dtm.llenarTabla();
            }
        }else if(e.getSource() == loadFileButton){
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog((JFrame)SwingUtilities.getRoot(this)) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                channelsFile.readFile(file);
                channelsFile.updateFile();
                dtm.llenarTabla();
            }
        }else if(e.getSource() == infoButton){
            InfoDialog informacion = new InfoDialog((JFrame)SwingUtilities.getRoot(this), "Información", true);
            informacion.setVisible(true);
        }else if(e.getSource() == exportFileButton){
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                ChannelsFile file = new ChannelsFile();
                file.exportFile(fileChooser.getSelectedFile().getAbsolutePath()+".csv");

                JOptionPane.showMessageDialog(null, "Se ha exportado el archivo con éxito","Exportación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }
}
