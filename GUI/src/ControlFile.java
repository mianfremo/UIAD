import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.WindowConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ControlFile {
    private FileReader controlFile;
    private ArrayList<Control> controllers;

    public ControlFile(){
        this.controllers = new ArrayList<Control>();
        try {
            this.controlFile = new FileReader("control.csv");
            this.readFile();
        } catch (Exception e) {
            this.createFile();
            this.readFile();
        }
    }

    public void createFile(){
        try {
            FileWriter archivo = new FileWriter("control.csv");
            String delimiter = ",";
            String NEXT_LINE = "\n";

            for (int i = 8; i < 16; i++) {
                if(i!=15){
                    archivo
                        .append(Integer.toString(i)).append(delimiter).append("404").append(delimiter).append("404").append(delimiter).append("0").append(delimiter).append("0").append(delimiter).append("0")
                        .append(NEXT_LINE);
                }else{
                    archivo
                    .append(Integer.toString(i)).append(delimiter).append("404").append(delimiter).append("404").append(delimiter).append("0").append(delimiter).append("0").append(delimiter).append("0");
                }
            }
            
            archivo.close();

            this.controlFile = new FileReader("control.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        try{
            // Abrir el .csv en buffer de lectura
            BufferedReader bufferLectura = new BufferedReader(this.controlFile);
            
            // Leer una linea del archivo
            String linea = bufferLectura.readLine();
            
            while (linea != null) {
                // Sepapar la linea leída con el separador definido previamente
                String[] campos = linea.split(",");

                int out = Integer.parseInt(campos[0]);
                int in = Integer.parseInt(campos[1]);
                int type = Integer.parseInt(campos[2]);
                double ls = Double.parseDouble(campos[3]);
                double li = Double.parseDouble(campos[4]);
                double sp = Double.parseDouble(campos[5]);

                Control c = new Control(type, Window.channels.get(out));

                c.setIn(in);

                if(type==0){
                    c.setP1(ls);
                    c.setP2(li);
                }else{
                    c.setSp(sp);
                }

                this.controllers.add(c);

                if(in!=404){
                    Window.channels.get(in).setController(c);
                }
             
                // Volver a leer otra línea del fichero
                linea = bufferLectura.readLine();
            }
            bufferLectura.close();

            this.controlFile.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Control> getControllers() {
        return controllers;
    }

    public void updateFile(ArrayList<Control> controllers){
        try{
            //Se crea un nuevo archivo con un nombre distinto
            FileWriter archivo = new FileWriter("controlUpdated.csv");
            String delimiter = ",";
            String NEXT_LINE = "\n";

            for (int i = 0; i < 8; i++) {
                Control c = controllers.get(i);


                if(i!=7){
                    archivo
                        .append(Integer.toString(c.getCanal().getId()-1)).append(delimiter).append(Integer.toString(c.getIn())).append(delimiter).append(Integer.toString(c.getType())).append(delimiter).append(Double.toString(c.getP1())).append(delimiter).append(Double.toString(c.getP2())).append(delimiter).append(Double.toString(c.getSp()))
                        .append(NEXT_LINE);
                }else{
                    archivo
                    .append(Integer.toString(c.getCanal().getId()-1)).append(delimiter).append(Integer.toString(c.getIn())).append(delimiter).append(Integer.toString(c.getType())).append(delimiter).append(Double.toString(c.getP1())).append(delimiter).append(Double.toString(c.getP2())).append(delimiter).append(Double.toString(c.getSp()));

                }

                if(c.getIn()!=404){
                    Window.channels.get(c.getIn()).setController(c);
                }
            }
            this.controllers = controllers;
            
            //Se cierra el archivo creado
            archivo.close();

            File fichero = new File("control.csv");
            //Se abre y se elimina el antiguo archivo
            this.deleteFile(fichero);

            fichero = new File("controlUpdated.csv");
            //Se abre al archivo creado actualizado y se le cambia el nombre
            fichero.renameTo(new File("control.csv"));

            

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPosiciones(ArrayList<Control> controllers) {
        this.controllers = controllers;
    }

    public boolean deleteFile(File file){
        if (file.delete()){
            return true;
        }else{
            return false;
        }
    }

    
}
