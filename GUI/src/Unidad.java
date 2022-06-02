public class Unidad {
    private int id;
    private String nombre;
    private String magnitud;
    public static String[] arrayTemp = {"°C","°F","K","°Ra"};
    public static String[] arrayPres = {"Pa","psi","atm","bar", "mm Hg"};
    public static String[] arrayCaudal = {"m³/s","gal/min","kg/min","m/s"};
    public static String[] arrayNivel = {"ft","in","cm","m"};

    public static String[] detUnidad(String magnitud){
        switch (magnitud) {
            case "Temperatura":
                return arrayTemp;
            case "Presión":
                return arrayPres;
            case "Caudal":
                return arrayCaudal;
            case "Nivel":
                return arrayNivel;
        }
        
        return null;
        
    }


    public Unidad(int id){
        this.id = id;
        switch (id) {
            //Unidades de Temperatura
            case 0:
                this.nombre = arrayTemp[0];
                this.magnitud = "Temperatura";
                break;
            case 1:
                this.nombre = arrayTemp[1];
                this.magnitud = "Temperatura";
                break;
            case 2:
                this.nombre = arrayTemp[2];
                this.magnitud = "Temperatura";
                break;
            case 3:
                this.nombre = arrayTemp[3];
                this.magnitud = "Temperatura";
                break;

            //Unidades de Presión
            case 4:
                this.nombre = arrayPres[0];
                this.magnitud = "Presión";
                break;
            case 5:
                this.nombre = arrayPres[1];
                this.magnitud = "Presión";
                break;
            case 6:
                this.nombre = arrayPres[2];
                this.magnitud = "Presión";
                break;
            case 7:
                this.nombre = arrayPres[3];
                this.magnitud = "Presión";
                break;
            case 8:
                this.nombre = arrayPres[4];
                this.magnitud = "Presión";
                break;
            

            //Unidades de Caudal
            case 9:
                this.nombre = arrayCaudal[0];
                this.magnitud = "Caudal";
                break;
            case 10:
                this.nombre = arrayCaudal[1];
                this.magnitud = "Caudal";
                break;
            case 11:
                this.nombre = arrayCaudal[2];
                this.magnitud = "Caudal";
                break;
            case 12:
                this.nombre = arrayCaudal[3];
                this.magnitud = "Caudal";
                break;


            //Unidades de Nivel
            case 13:
                this.nombre = arrayNivel[0];
                this.magnitud = "Nivel";
                break;
            case 14:
                this.nombre = arrayNivel[1];
                this.magnitud = "Nivel";
                break;
            case 15:
                this.nombre = arrayNivel[2];
                this.magnitud = "Nivel";
                break;
            case 16:
                this.nombre = arrayNivel[3];
                this.magnitud = "Nivel";
                break;
            
            case 17:
                this.nombre = "NC";
                this.magnitud = "Presión";
                break;
        }
    }

    public Unidad(String nombre, String magnitud){
        this.nombre = nombre;
        this.magnitud = magnitud;
        switch (nombre) {
            case "°C":
                this.id = 0;
                break;
            case "°F":
                this.id = 1;
                break;
            case "K":
                this.id = 2;
                break;
            case "°Ra":
                this.id = 3;
                break;


            case "Pa":
                this.id = 4;
                break;
            case "psi":
                this.id = 5;
                break;
            case "atm":
                this.id = 6;
                break;
            case "bar":
                this.id = 7;
                break;
            case "mm Hg":
                this.id = 8;
                break;
            

            case "m³/s":
                this.id = 9;
                break;
            case "gal/min":
                this.id = 10;
                break;
            case "kg/min":
                this.id = 11;
                break;
            case "m/s":
                this.id = 12;
                break;


            case "ft":
                this.id = 13;
                break;
            case "in":
                this.id = 14;
                break;
            case "cm":
                this.id = 15;
                break;
            case "m":
                this.id = 16;
                break;
            
        }
        
    }

    public void setId(int id){
        this.id = id;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getId(){
        return this.id;
    }

    public String getMagnitud(){
        return this.magnitud;
    }
}
