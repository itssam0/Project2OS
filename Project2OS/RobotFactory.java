import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

class RobotFactory implements Directions {
    public static Racer[] createRobots(int numRobots, boolean[][] laberinto) {
        List<Color> colores = new ArrayList<>();
        colores.add(Color.blue);
        colores.add(Color.red);
        colores.add(Color.green);
        colores.add(Color.yellow);

        List<String> colorNames = new ArrayList<>();
        colorNames.add("blue");
        colorNames.add("red");
        colorNames.add("green");
        colorNames.add("yellow");

        int[] quantity_of_beepers = { 1, 2, 4, 8 };

        Point[] parqueadero = {
                new Point(7, 12), new Point(7, 18),
                new Point(6, 12), new Point(6, 18), 
                new Point(5, 12), new Point(5, 18),
                new Point(4, 12), new Point(4, 18),
                new Point(3, 12),
                new Point(2, 12)
        };

        Racer[] racers = new Racer[numRobots];
        Random random = new Random();
        List<Point> posicionesDisponibles = new ArrayList<>(Arrays.asList(parqueadero)); // Convierte el array en una
                                                                                         // lista

        for (int i = 0; i < numRobots; i++) {
            int indexAleatorio = random.nextInt(posicionesDisponibles.size());
            Point posicionInicial = posicionesDisponibles.remove(indexAleatorio);

            int colorIndex = i % colores.size();
            racers[i] = new Racer(posicionInicial.x, posicionInicial.y, East, 0, colores.get(colorIndex),
                    quantity_of_beepers[colorIndex],
                    colorNames.get(colorIndex), i, laberinto);
        }
        return racers;
    }
}

class PuntoInvalidoException extends RuntimeException {
    public PuntoInvalidoException(String mensaje) {
        super(mensaje);
    }
}

class KarelWorldParser {
    public String worldText;
    public boolean[][] matrix = new boolean[40][40];

    public KarelWorldParser(String worldText) {
        this.worldText = worldText;
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (i == 39 || j == 0) {
                    matrix[i][j] = false;
                } else {
                    matrix[i][j] = true;
                }
            }
        }
        String[] tokens = worldText.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("eastwestwalls")) {
                int street = (Integer.parseInt(tokens[i + 1]) * 2);
                int avenue = (Integer.parseInt(tokens[i + 2]) * 2) - 1;
                fillMatrix(street, avenue);
                fillMatrix(street, avenue + 1);
                fillMatrix(street, avenue - 1);
            } else if (tokens[i].equals("northsouthwalls")) {
                int avenue = (Integer.parseInt(tokens[i + 1]) * 2);
                int street = (Integer.parseInt(tokens[i + 2]) * 2) - 1;
                fillMatrix(street, avenue);
                fillMatrix(street + 1, avenue);
                fillMatrix(street - 1, avenue);
            }
        }
        printMatrix();
    }

    public void fillMatrix(int street, int avenue) {
        if (street >= 0 && street < 40 && avenue >= 0 && avenue < 40) {
            matrix[39 - street][avenue] = false;
        } else {
            System.out.println("Error: Índice fuera de límites. Street: " + street + ", Avenue: " + avenue);
        }
    }

    public void printMatrix() {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (matrix[i][j]) {
                    System.out.print("  ");
                } else {
                    System.out.print("1 ");
                }
            }
            System.out.println();
        }
    }
}