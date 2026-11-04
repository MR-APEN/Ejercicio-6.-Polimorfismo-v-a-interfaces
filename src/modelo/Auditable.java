package modelo;

public interface Auditable {
    void registrarEvento(String evento);
    String consultarAuditoria();
}
