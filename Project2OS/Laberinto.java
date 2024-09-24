import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Laberinto {
    private static final int[] row = { -2, 2, 0, 0 };
    private static final int[] col = { 0, 0, -2, 2 };

    private static boolean esValido(boolean[][] laberinto, boolean[][] visitado, int fila, int columna, int actualFila,
            int actualColumna) {
        int n = laberinto.length;
        if (fila < 0 || columna < 0 || fila >= n || columna >= n || !laberinto[fila][columna]
                || visitado[fila][columna]) {
            return false;
        }
        int intermedioFila = (fila + actualFila) / 2;
        int intermedioColumna = (columna + actualColumna) / 2;
        return laberinto[intermedioFila][intermedioColumna];
    }

    public static LinkedList<Point> bfs(boolean[][] laberinto, Point inicio, Point fin) {
        int n = laberinto.length;
        boolean[][] visitado = new boolean[n][n];
        Queue<Point> cola = new LinkedList<>();
        cola.add(inicio);
        visitado[inicio.x][inicio.y] = true;
        Point[][] predecesor = new Point[n][n];
        while (!cola.isEmpty()) {
            Point actual = cola.poll();
            if (actual.x == fin.x && actual.y == fin.y) {
                return reconstruirCamino(predecesor, inicio, fin);
            }
            for (int i = 0; i < 4; i++) {
                int nuevoX = actual.x + row[i];
                int nuevoY = actual.y + col[i];

                if (esValido(laberinto, visitado, nuevoX, nuevoY, actual.x, actual.y)) {
                    cola.add(new Point(nuevoX, nuevoY));
                    visitado[nuevoX][nuevoY] = true;
                    predecesor[nuevoX][nuevoY] = actual;
                }
            }
        }

        return null;
    }

    private static LinkedList<Point> reconstruirCamino(Point[][] predecesor, Point inicio, Point fin) {
        LinkedList<Point> camino = new LinkedList<>();
        Point paso = fin;
        while (paso != null) {
            camino.addFirst(paso);
            paso = predecesor[paso.x][paso.y];
        }
        if (camino.getFirst().x == inicio.x && camino.getFirst().y == inicio.y) {
            return camino;
        } else {
            return null;
        }
    }

    public List<Point> buscarRuta(boolean[][] laberinto, Point inicio, Point fin) {
        int pos_inicio_x = (int) (-2 * inicio.x + 40);
        int pos_inicio_y = 2 * inicio.y - 1;
        int pos_fin_x = (int) (-2 * fin.x + 40);
        int pos_fin_y = 2 * fin.y - 1;

        if (!laberinto[pos_inicio_x][pos_inicio_y] || !laberinto[pos_fin_x][pos_fin_y]) {
            throw new PuntoInvalidoException("Punto de inicio o fin no es válido en el laberinto.");
        }

        Point inicio_transformado = new Point(pos_inicio_x, pos_inicio_y);
        Point fin_transformado = new Point(pos_fin_x, pos_fin_y);

        LinkedList<Point> camino = bfs(laberinto, inicio_transformado, fin_transformado);
        List<Point> rutaTransformada = new LinkedList<>();

        if (camino != null) {
            for (Point p : camino) {
                int pos_x = (int) (-0.5 * p.x + 20);
                int pos_y = (p.y / 2) + 1;
                rutaTransformada.add(new Point(pos_x, pos_y));
            }
        }

        return rutaTransformada;
    }
}
