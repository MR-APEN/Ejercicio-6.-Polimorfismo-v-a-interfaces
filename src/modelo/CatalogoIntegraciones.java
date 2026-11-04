package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public final class CatalogoIntegraciones implements Iterable<IntegracionAPI> {
    private final ArrayList<IntegracionAPI> integraciones = new ArrayList<>();

    public boolean agregar(IntegracionAPI integracion) {
        if (integracion == null || buscar(integracion.getId()) != null) { return false; }
        integraciones.add(integracion);
        return true;
    }

    public IntegracionAPI buscar(String id) {
        if (id == null) { return null; }
        String normalizado = id.trim().toUpperCase(Locale.ROOT);
        for (IntegracionAPI integracion : integraciones) {
            if (integracion.getId().equals(normalizado)) { return integracion; }
        }
        return null;
    }

    public void ordenarPorLatencia() { Collections.sort(integraciones); }
    public int cantidad() { return integraciones.size(); }

    @Override
    public Iterator<IntegracionAPI> iterator() {
        return Collections.unmodifiableList(integraciones).iterator();
    }
}
