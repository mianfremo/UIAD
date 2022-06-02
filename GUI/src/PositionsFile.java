import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.awt.Point;

public class PositionsFile {
    private FileReader positionsFile;
    private ArrayList<Point> posiciones;
    private boolean exist;

    public PositionsFile(){
        posiciones = new ArrayList<Point>();
        try {
            this.positionsFile = new FileReader("positions.csv");
            this.exist = true;
            this.readFile();
        } catch (Exception e) {
            this.exist = false;
            this.createFile();
            this.readFile();
        }
    }

    public void createFile(){
        try {
            FileWriter archivo = new FileWriter("positions.csv");
            String delimiter = ",";
            String NEXT_LINE = "\n";

            for (int i = 0; i < 16; i++) {
                if(i!=15){
                    archivo
                        .append("0").append(delimiter).append("0")
                        .append(NEXT_LINE);
                }else{
                    archivo
                        .append("0").append(delimiter).append("0");
                }
            }
            
            archivo.close();

            this.positionsFile = new FileReader("positions.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        try{
            // Abrir el .csv en buffer de lectura
            BufferedReader bufferLectura = new BufferedReader(this.positionsFile);
            
            // Leer una linea del archivo
            String linea = bufferLectura.readLine();
            
            while (linea != null) {
                // Sepapar la linea leída con el separador definido previamente
                String[] campos = linea.split(",");

                Point pos = new Point(Integer.parseInt(campos[0]),Integer.parseInt(campos[1]));

                this.posiciones.add(pos);
             
                // Volver a leer otra línea del fichero
                linea = bufferLectura.readLine();
            }
            bufferLectura.close();

            this.positionsFile.close();

            

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Point> getPosiciones() {
        return posiciones;
    }

    public void updateFile(ArrayList<Point> posiciones){
        try{
            //Se crea un nuevo archivo con un nombre distinto
            FileWriter archivo = new FileWriter("positionsUpdated.csv");
            String delimiter = ",";
            String NEXT_LINE = "\n";

            for (int i = 0; i < 16; i++) {
                int x = (int)posiciones.get(i).getX();
                int y = (int)posiciones.get(i).getY();


                if(i!=15){
                    archivo
                        .append(Integer.toString(x)).append(delimiter).append(Integer.toString(y))
                        .append(NEXT_LINE);
                }else{
                    archivo
                        .append(Integer.toString(x)).append(delimiter).append(Integer.toString(y));

                }
            }
            
            //Se cierra el archivo creado
            archivo.close();

            File fichero = new File("positions.csv");
            //Se abre y se elimina el antiguo archivo
            this.deleteFile(fichero);

            fichero = new File("positionsUpdated.csv");
            //Se abre al archivo creado actualizado y se le cambia el nombre
            fichero.renameTo(new File("positions.csv"));

            

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDefault(){
        boolean flag = true;
        for (Point point : posiciones) {
            if(point.getX()!=0||point.getY()!=0){
                flag = false;
                break;
            }
        }

        return flag;
    }

    public void setPosiciones(ArrayList<Point> posiciones) {
        this.posiciones = posiciones;
    }

    public boolean deleteFile(File file){
        if (file.delete()){
            return true;
        }else{
            return false;
        }
    }

    public boolean getExist(){
        return this.exist;
    }

    
}
