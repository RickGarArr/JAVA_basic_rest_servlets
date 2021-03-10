package datos.resultados;

public class Resultado {
    private boolean exitoso;
    private String mensaje;
    
    public Resultado(boolean bandera, String mensaje){
        this.exitoso = bandera;
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
}
