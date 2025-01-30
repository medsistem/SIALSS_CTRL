package ReportesPuntos.cache;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

/**
 * Permite filtrar todos los archivos del mismo usuario de una sesion pasada.
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public final class FilterSession
        implements DirectoryStream.Filter<Path> {

    private final String session;
    private final String user;
    private final String category;

    public FilterSession(final String session, final String user,
            final String category) {
        this.session = session;
        this.user = user;
        this.category = category;
    }

    @Override
    public boolean accept(Path entry) throws IOException {
        String[] parametros = entry.getFileName().toString().split("_");
        String currentSession = parametros[1];
        String currentUser = parametros[0];
        String currentCategory = parametros[2];

        return (this.user.equals(currentUser) && !this.session.equals(currentSession))
                || (this.user.equals(currentUser) && this.session.equals(currentSession)
                && this.category.equals(currentCategory));
    }
}
