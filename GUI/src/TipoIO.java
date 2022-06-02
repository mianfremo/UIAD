public class TipoIO {
    private String nombre;
    private int type;

    public TipoIO(int type){

        this.type = type;

        switch (type) {
            case 0:
                this.nombre = "Entrada";
                break;
            case 1:
                this.nombre = "Salida";
                break;
        }
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getType(){
        return this.type;
    }
}
