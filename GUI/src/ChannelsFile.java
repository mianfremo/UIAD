import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;

public class ChannelsFile {
    private FileReader channelsFile;
    private ArrayList<IO> channels;

    public ChannelsFile(){
        this.channels = new ArrayList<IO>();

        try {
            this.channelsFile = new FileReader("channels.csv");
            this.readFile();
        } catch (Exception e) {
            System.out.println("El archivo channels.csv no existe, creando nuevo");
            this.createFile();
            this.readFile();
        }
    }


    public void updateRow(IO row){
        //Obtiene el id de la fila pasada como parámetro
        int id = row.getId()-1; //Se le resta 1 porque el número de columnas de la tabla comienza en 1
        this.channels.set(id, row); //Se sustituye el elemento en el array de elementos
        this.updateFile(); //Se sobreescribe el archivo con los elementos nuevos
    }

    public boolean deleteFile(File file){
        if (file.delete()){
            return true;
        }else{
            return false;
        }
    }

    public void updateFile(){
        try{
            //Se crea un nuevo archivo con un nombre distinto
            FileWriter archivo = new FileWriter("channelsUpdated.csv");
            String delimiter = "&;";
            String NEXT_LINE = "\n";
            

            //Se le agrega al nuevo archivo los elementos del array que está guardado en el objeto actual
            for (int i = 0; i < this.channels.size(); i++) {
                if(this.channels.get(i).getId() != 16){
                    archivo
                        .append(Integer.toString(this.channels.get(i).getId())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getIo().getType())).append(delimiter)
                        .append(this.channels.get(i).getNombre()).append(delimiter)
                        .append(this.channels.get(i).getDesc()).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getTipo().getType())).append(delimiter)
                        .append(this.channels.get(i).getSlot()).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMinRange())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMaxRange())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getUnity().getId())).append(delimiter)
                        .append(Boolean.toString(this.channels.get(i).getEnabled())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKp())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKi())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKd()));
                    archivo.append(NEXT_LINE);
                }else{
                    //Si es la ultima fila no se agrega el salto de linea
                    archivo
                        .append(Integer.toString(this.channels.get(i).getId())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getIo().getType())).append(delimiter)
                        .append(this.channels.get(i).getNombre()).append(delimiter)
                        .append(this.channels.get(i).getDesc()).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getTipo().getType())).append(delimiter)
                        .append(this.channels.get(i).getSlot()).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMinRange())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMaxRange())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getUnity().getId())).append(delimiter)
                        .append(Boolean.toString(this.channels.get(i).getEnabled())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKp())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKi())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKd()));
                        
                }
            }
            
            //Se cierra el archivo creado
            archivo.close();

            File fichero = new File("channels.csv");
            //Se abre y se elimina el antiguo archivo
            this.deleteFile(fichero);

            fichero = new File("channelsUpdated.csv");
            //Se abre al archivo creado actualizado y se le cambia el nombre
            fichero.renameTo(new File("channels.csv"));

            Window.channels = this.channels;
            

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //La función readFile lee el archivo channels.csv y llena el arraylist de elementos
    public void readFile(){
        try{
            this.channels.clear();
            // Abrir el .csv en buffer de lectura
            BufferedReader bufferLectura = new BufferedReader(this.channelsFile);
            
            // Leer una linea del archivo
            String linea = bufferLectura.readLine();
            
            while (linea != null) {
                // Sepapar la linea leída con el separador definido previamente
                String[] campos = linea.split("&;");

                int id = Integer.parseInt(campos[0]);
                TipoIO io = new TipoIO(Integer.parseInt(campos[1]));
                String nombre = campos[2];
                String desc = campos[3];
                Tipo tipo = new Tipo(Integer.parseInt(campos[4]));
                String slot = campos[5];
                double minRange = Double.parseDouble(campos[6]);
                double maxRange = Double.parseDouble(campos[7]);
                Unidad unity = new Unidad(Integer.parseInt(campos[8]));
                boolean enabled = Boolean.parseBoolean(campos[9]);
                double kp = Double.parseDouble(campos[10]);
                double ki = Double.parseDouble(campos[11]);
                double kd = Double.parseDouble(campos[12]);
                
                IO canal = new IO(id,io,nombre,desc,tipo,slot,minRange,maxRange,unity,enabled, kp, ki, kd);

                this.channels.add(canal);
             
                // Volver a leer otra línea del fichero
                linea = bufferLectura.readLine();
            }
            bufferLectura.close();

            Window.channels = this.channels;

            this.channelsFile.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Esta función es similar a la anterior pero recibe otro archivo como parámetro para llenar el array de elementos
    public void readFile(File file){
        try{

            if(FilenameUtils.getExtension(file.getName())=="csv"){
                // Abrir el .csv en buffer de lectura
                this.channels.clear();
                BufferedReader bufferLectura = new BufferedReader(new FileReader(file));
                
                // Leer una linea del archivo
                String linea = bufferLectura.readLine();
                
                while (linea != null) {
                    // Sepapar la linea leída con el separador definido previamente
                    String[] campos = linea.split("&;");

                    int id = Integer.parseInt(campos[0]);
                    TipoIO io = new TipoIO(Integer.parseInt(campos[1]));
                    String nombre = campos[2];
                    String desc = campos[3];
                    Tipo tipo = new Tipo(Integer.parseInt(campos[4]));
                    String slot = campos[5];
                    double minRange = Double.parseDouble(campos[6]);
                    double maxRange = Double.parseDouble(campos[7]);
                    Unidad unity = new Unidad(Integer.parseInt(campos[8]));
                    boolean enabled = Boolean.parseBoolean(campos[9]);
                    double kp = Double.parseDouble(campos[10]);
                    double ki = Double.parseDouble(campos[11]);
                    double kd = Double.parseDouble(campos[12]);

                    IO canal = new IO(id,io,nombre,desc,tipo,slot,minRange,maxRange,unity,enabled, kp, ki, kd);
                    

                    this.channels.add(canal);
                
                    // Volver a leer otra línea del fichero
                    linea = bufferLectura.readLine();
                }
                bufferLectura.close();

                Window.channels = this.channels;

                this.channelsFile.close();
                

            }else{
                JOptionPane.showMessageDialog(null,"El archivo seleccionado no es un archivo de configuración válido","Error",JOptionPane.ERROR_MESSAGE); 
            }

        }catch (IOException e) {
            JOptionPane.showMessageDialog(null,"El archivo seleccionado no es un archivo de configuración válido","Error",JOptionPane.ERROR_MESSAGE); 
        }
    }

    public void createFile(){
        try {
            FileWriter archivo = new FileWriter("channels.csv");
            String delimiter = "&;";
            String NEXT_LINE = "\n";

            int n = 0;

            for (int i = 0; i < 16; i++) {
                if(i<8){
                    archivo.append(Integer.toString(i+1)).append(delimiter).append("0").append(delimiter).append("NC").append(delimiter).append("NC").append(delimiter).append("404").append(delimiter).append("I"+Integer.toString(i)).append(delimiter).append("0.0").append(delimiter).append("0.0").append(delimiter).append("17").append(delimiter).append("false").append(delimiter).append("0").append(delimiter).append("0").append(delimiter).append("0");
                    archivo.append(NEXT_LINE); 
                }else if(i>7 && i!=15){
                    archivo.append(Integer.toString(i+1)).append(delimiter).append("1").append(delimiter).append("NC").append(delimiter).append("NC").append(delimiter).append("404").append(delimiter).append("O"+Integer.toString(n)).append(delimiter).append("0.0").append(delimiter).append("0.0").append(delimiter).append("17").append(delimiter).append("false").append(delimiter).append("0").append(delimiter).append("0").append(delimiter).append("0");
                    archivo.append(NEXT_LINE);
                    n++;
                }else{
                    archivo.append(Integer.toString(i+1)).append(delimiter).append("1").append(delimiter).append("NC").append(delimiter).append("NC").append(delimiter).append("404").append(delimiter).append("O"+Integer.toString(n)).append(delimiter).append("0.0").append(delimiter).append("0.0").append(delimiter).append("17").append(delimiter).append("false").append(delimiter).append("0").append(delimiter).append("0").append(delimiter).append("0");
                }
            }

            archivo.close();

            this.channelsFile = new FileReader("channels.csv");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportFile(String absolutePath){
        try{
            //Se crea un nuevo archivo con un nombre distinto
            FileWriter archivo = new FileWriter(absolutePath);
            String delimiter = "&;";
            String NEXT_LINE = "\n";

            //Se le agrega al nuevo archivo los elementos del array que está guardado en el objeto actual
            for (int i = 0; i < this.channels.size(); i++) {
                if(this.channels.get(i).getId() != 16){
                    archivo
                        .append(Integer.toString(this.channels.get(i).getId())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getIo().getType())).append(delimiter)
                        .append(this.channels.get(i).getNombre()).append(delimiter)
                        .append(this.channels.get(i).getDesc()).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getTipo().getType())).append(delimiter)
                        .append(this.channels.get(i).getSlot()).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMinRange())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMaxRange())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getUnity().getId())).append(delimiter)
                        .append(Boolean.toString(this.channels.get(i).getEnabled())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKp())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKi())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKd()));
                    archivo.append(NEXT_LINE);
                }else{
                    //Si es la ultima fila no se agrega el salto de linea
                    archivo
                        .append(Integer.toString(this.channels.get(i).getId())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getIo().getType())).append(delimiter)
                        .append(this.channels.get(i).getNombre()).append(delimiter)
                        .append(this.channels.get(i).getDesc()).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getTipo().getType())).append(delimiter)
                        .append(this.channels.get(i).getSlot()).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMinRange())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getMaxRange())).append(delimiter)
                        .append(Integer.toString(this.channels.get(i).getUnity().getId())).append(delimiter)
                        .append(Boolean.toString(this.channels.get(i).getEnabled())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKp())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKi())).append(delimiter)
                        .append(Double.toString(this.channels.get(i).getKd()));
                        
                }
            }
            
            //Se cierra el archivo creado
            archivo.close();            

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<IO> getChannels(){
        return this.channels;
    }
}
