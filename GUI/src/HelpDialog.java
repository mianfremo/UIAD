import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JSplitPane;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


public class HelpDialog extends JDialog {
    private JEditorPane contentPanel;
    private JTree treePanel;
    private JSplitPane sp;

    public HelpDialog (JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        this.treePanel = new JTree();

        this.contentPanel = new JEditorPane();
        this.contentPanel.setEditable(false);        
        this.contentPanel.setContentType( "text/html" );
        this.contentPanel.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        this.contentPanel.setText( readFile("archivo1") );//lee el primer archivo
        this.contentPanel.setCaretPosition(0);

        treePanel.setModel(createTreeModel());       
        //listener
        treePanel.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {             
                //cuando se realice un clic sobre algun item, se carga el archivo HTML correspondiente
                TreePath path = treePanel.getSelectionPath();
                if( path!=null )
                {
                    DefaultMutableTreeNode NodoSeleccionado = (DefaultMutableTreeNode)path.getLastPathComponent();              
                    //Obtiene el nombre del archivo HTML correspondiente al item seleccionado
                    String archivo = ((HelpFile) NodoSeleccionado.getUserObject()).getArchivo();
                    contentPanel.setText( readFile(archivo) );
                    contentPanel.setCaretPosition(0);
                }
            }            
        }); 


        this.sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, contentPanel);
        this.sp.setDividerLocation(250);
        this.add(sp);
        
    }

    private String readFile(String fileName){        
        StringBuilder result = new StringBuilder("");     
        File  file = new File("help/"+ fileName + ".html"); 
        try {
                if( file.exists() ){
                    Scanner scanner = new Scanner(file);
                    //lee archivo linea por linea
                    while (scanner.hasNextLine()) {
                    String line = replaceSRC(scanner.nextLine().trim());
                    result.append(line);                        
                    } 
                    scanner.close(); 
                }else{
                    System.out.println("El archivo ["+fileName+".html] no existe");
                }
        } catch (IOException e) {
                    System.err.println(e.getMessage());
        } 
        return result.toString();
    }

    private String replaceSRC(String value){        
        //si existe imagen
        if( value.indexOf("src=") != -1 ){            
            //System.out.println("Antes: " + value); 
            Pattern patron = Pattern.compile("src=\"(\\w+).(jpg|png|gif)\"");
            Matcher matcher = patron.matcher(value);
            matcher.find();       
            value = value.replaceAll( matcher.group(1), Matcher.quoteReplacement(new File("image/"+matcher.group(1)).toURI()+"") );
        }
        return value;
    }

    private DefaultTreeModel createTreeModel(){
        DefaultMutableTreeNode root  = new DefaultMutableTreeNode();   
        DefaultMutableTreeNode hoja = new DefaultMutableTreeNode();           
        //carga archivo help 
        try {
            Scanner scanner = new Scanner(new File("help/indice.txt"));
            //lee archivo linea por linea
            while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();                        
                if( line.substring(0, 1).equals(">") ){
                    root = new DefaultMutableTreeNode(createHelpArchivo(line.substring(1, line.length())) );         
                }else if( line.substring(0, 1).equals("|") ){
                    hoja = new DefaultMutableTreeNode(createHelpArchivo(line.substring(1, line.length())) );    
                    root.add(hoja);
                }else if( line.substring(0, 1).equals("-") ){
                    hoja.add(new DefaultMutableTreeNode(createHelpArchivo(line.substring(1, line.length())) ));
                }
            } 
            scanner.close(); 
            //se añade arbol al modelo
            DefaultTreeModel modelo = new DefaultTreeModel(root);
            return modelo; 
        } catch (FileNotFoundException e) {            
            System.out.println(e);
        } 
        return null;
    }

    private HelpFile createHelpArchivo( String value )
    {
       HelpFile helpArchivo = new HelpFile();
       //System.out.println( value );
       Pattern patron = Pattern.compile("\\[(.*)\\]\\[(\\w+)\\]");
       Matcher matcher = patron.matcher(value);       
       matcher.find();//busca cadenas
       helpArchivo.setTexto(matcher.group(1));
       helpArchivo.setArchivo(matcher.group(2));        
       return helpArchivo;
    }

}
