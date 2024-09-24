import kareltherobot.*;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.Map;

class Racer extends Robot implements Runnable {
    public static Set<Point> occupiedPositions = new HashSet<>();
    private static volatile boolean flag = false;
    private int number; // Número de robot
    private String id; // Identificador del robot color
    private int maxBeepers; // Máximo de beepers que puede llevar
    private int[] deliveryPosition; // Posición de entrega
    private int currentStreet; // Calle actual
    private int currentAvenue; // Avenida actual
    private int followStreet; // Calle a seguir
    private int followAvenue; // Avenida a seguir
    private int startStreet; // Calle de inicio
    private int startAvenue; // Avenida de inicio
    private int beepersInTheBag; // Beepers en la bolsa
    private boolean[][] laberinto; // Laberinto
    private Point beeperLocation = new Point(8, 19); // Posición fija del beeper
    private String parada = "parada1";
    private static final Semaphore semaphorep1 = new Semaphore(1);
    private static final Semaphore semaphorep2 = new Semaphore(1);
    private static final Semaphore semaphorep3 = new Semaphore(1);
    private static final Semaphore semaphorep4 = new Semaphore(1);

    public Racer(int street, int avenue, Direction direction, int beepers, Color color, int maxBeepers, String id,
            int number, boolean[][] laberinto) {

        super(street, avenue, direction, beepers, color);
        this.id = id;
        this.number = number;
        this.maxBeepers = maxBeepers;
        this.deliveryPosition = new int[] { maxBeepers, 1 };
        this.currentStreet = street;
        this.currentAvenue = avenue;
        this.followStreet = street;
        this.followAvenue = avenue + 1;
        this.startStreet = street;
        this.startAvenue = avenue;
        this.beepersInTheBag = 0;
        this.laberinto = laberinto;
        World.setupThread(this);
        synchronized (occupiedPositions) {
            occupiedPositions.add(new Point(currentStreet, currentAvenue));
        }
    }

    public void moveToLocation(Point inicio, Point fin) {
        Laberinto lab = new Laberinto();
        List<Point> ruta = lab.buscarRuta(laberinto, inicio, fin);
        if (ruta != null && !ruta.isEmpty()) {
            System.out.println("Ruta transformada encontrada:");
            for (Point p : ruta) {
                System.out.println("(" + p.x + ", " + p.y + ")");
            }
        } else {
            System.out.println("No se encontró un camino.");
        }

        if (ruta == null || ruta.isEmpty()) {
            System.out.println("La ruta está vacía o es nula.");
            return;
            //sexo anal con caballos
        }

        for (int i = 1; i < ruta.size(); i++) {
            Point currentPoint = new Point(getCurrentPosition()[0], getCurrentPosition()[1]);
            Point nextPoint = ruta.get(i);
            String direction = getDirection(currentPoint, nextPoint);
            turnTo(direction);
            move();
        }
    }

    public void recogerBeeper() {
        moveToLocation(new Point(currentStreet, currentAvenue), beeperLocation);
        if (nextToABeeper()) {
            pickBeeper();
        }
    }

    // Implementación de las instrucciones para las paradas
    public void irAParadaAleatoria() {
        Random random = new Random();

        // Paradas definidas
        Point parada1 = new Point(16, 6); // Parada 1
        Point parada2 = new Point(12, 7); // Parada 2
        Point parada3 = new Point(8, 8); // Parada 3
        Point parada4 = new Point(18, 19); // Parada 4

        int paradaAleatoria = random.nextInt(4) + 1;

        // Movimiento según parada asignada al robot
        switch (paradaAleatoria) {
            case 1:
                // Parada 1: (16, 6) → (14, 3) → (6, 7) → (19, 1)
                parada = "parada1";
                moveToLocation(new Point(currentStreet, currentAvenue), parada1);
                putBeeper();
                moveToLocation(parada1, new Point(14, 3));
                moveToLocation(new Point(14, 3), new Point(6, 7));
                moveToLocation(new Point(6, 7), new Point(19, 1));
                break;
            case 2:
                // Parada 2: (9, 8) → (9, 13) → (10, 14) → (12, 7) → (6, 6) → (10, 10) → (19, 1)
                parada = "parada2";
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(9, 8));
                moveToLocation(new Point(9, 8), new Point(9, 13));
                moveToLocation(new Point(9, 13), new Point(10, 16));
                moveToLocation(new Point(10, 16), parada2);
                putBeeper();
                moveToLocation(parada2, new Point(6, 6));
                moveToLocation(new Point(6, 6), new Point(6, 10));
                moveToLocation(new Point(6, 10), new Point(19, 1));
                break;
            case 3:
                // Parada 3: (7, 8) → (10, 10) → (19, 1)
                parada = "parada3";
                moveToLocation(new Point(currentStreet, currentAvenue), parada3);
                putBeeper();
                moveToLocation(parada3, new Point(10, 10));
                moveToLocation(new Point(10, 10), new Point(19, 1));
                break;
            case 4:
                // Parada 4: (9, 8) → (9, 13) → (18, 19) → (19, 1)
                parada = "parada4";
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(9, 8));
                moveToLocation(new Point(9, 8), new Point(9, 13));
                moveToLocation(new Point(9, 13), parada4);
                putBeeper();
                moveToLocation(parada4, new Point(19, 1));
                break;
        }
    }
    public static synchronized void setFlag(boolean value) {
        flag = value;
    }

    public static synchronized boolean getFlag() {
        return flag;
    }

    public void realizarTareas() {
        boolean firsttime = true;
        while (true) {
            if (firsttime) {
                moveToLocation(new Point(currentStreet, currentAvenue), beeperLocation);
                firsttime = false;
            } else {
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(1, 19));
                if (flag) {
                    break;
                }
                moveToLocation(new Point(currentStreet, currentAvenue), beeperLocation);
            }
            if (nextToABeeper()) {
                recogerBeeper();
                if(!nextToABeeper()) {
                    setFlag(true);
                }
            }
            if (flag) {
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(6, 8));
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(6, 8));
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(18, 10));
                moveToLocation(new Point(currentStreet, currentAvenue), new Point(19, 1));
            } else {
                irAParadaAleatoria();
            }
        }
        moveToLocation(new Point(currentStreet, currentAvenue), new Point(startStreet, startAvenue));
        turnOff();
    }

    private String getDirection(Point current, Point next) {
        if (next.x > current.x) {
            return "NORTH";
        } else if (next.x < current.x) {
            return "SOUTH";
        } else if (next.y > current.y) {
            return "EAST";
        } else if (next.y < current.y) {
            return "WEST";
        } else {
            throw new IllegalArgumentException("Los puntos son iguales; no se puede determinar la dirección.");
        }
    }

    private void turnTo(String direction) {
        while (!facingDirection(direction)) {
            turnLeft();
        }
    }

    private boolean facingDirection(String direction) {
        switch (direction) {
            case "NORTH":
                followStreet = currentStreet + 1;
                followAvenue = currentAvenue;
                return facingNorth();
            case "SOUTH":
                followStreet = currentStreet - 1;
                followAvenue = currentAvenue;
                return facingSouth();
            case "EAST":
                followStreet = currentStreet;
                followAvenue = currentAvenue + 1;
                return facingEast();
            case "WEST":
                followStreet = currentStreet;
                followAvenue = currentAvenue - 1;
                return facingWest();
            default:
                return false;
        }
    }

    public int[] getCurrentPosition() {
        return new int[] { currentStreet, currentAvenue };
    }

    private void updatePosition() {
        if (facingNorth()) {
            currentStreet++;
        } else if (facingSouth()) {
            currentStreet--;
        } else if (facingEast()) {
            currentAvenue++;
        } else if (facingWest()) {
            currentAvenue--;
        }
    }

    private static final Map<String, Point[]> semaforoPuntos = Map.of(
        "parada1", new Point[]{new Point(18, 5), new Point(18, 7)}, // Espera y desbloqueo
        "parada2", new Point[]{new Point(10, 8), new Point(10, 6)},
        "parada3", new Point[]{new Point(6, 7), new Point(6, 9)},
        "parada4", new Point[]{new Point(10, 16), new Point(10, 14)}
    );

    private Semaphore getSemaforo() {
        switch (parada) {
            case "parada1":
                return semaphorep1;
            case "parada2":
                return semaphorep2;
            case "parada3":
                return semaphorep3;
            case "parada4":
                return semaphorep4;
            default:
                throw new IllegalStateException("Parada no válida");
        }
    }
    
    private Point getPuntoEspera() {
        return semaforoPuntos.get(parada)[0];
    }
    
    private Point getPuntoDesbloqueo() {
        return semaforoPuntos.get(parada)[1];
    }

    public void bloquearParada() {
        try {
            Semaphore semaforo = getSemaforo();
            semaforo.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liberarParada() {
        try {
            Semaphore semaforo = getSemaforo();
            semaforo.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void move() {

        System.out.println(
                "Robot " + id + " (" + number + ") en la posición (" + currentStreet + ", " + currentAvenue + ")");

        Point nextPosition = new Point(followStreet, followAvenue);
        Point currentPosition = new Point(currentStreet, currentAvenue);

        if (currentPosition.equals(getPuntoEspera())) {
            System.out.println("Robot " + id + " está en el punto de espera. Intentando adquirir el semáforo...");
            bloquearParada(); // Intenta adquirir el semáforo correspondiente
            System.out.println("Semáforo adquirido. Robot " + id + " continúa.");
        }

        synchronized (occupiedPositions) {
            // Verifica si la posición está ocupada
            if (occupiedPositions.contains(nextPosition)) {
                System.out
                        .println("La posición (" + followStreet + ", " + followAvenue + ") está ocupada. Esperando...");
                // Espera hasta que la posición esté libre
                while (occupiedPositions.contains(nextPosition)) {
                    try {
                        occupiedPositions.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Mueve el robot a la nueva posición
            occupiedPositions.remove(new Point(currentStreet, currentAvenue)); // Libera la posición actual
            super.move();
            updatePosition();
            occupiedPositions.add(nextPosition); // Marca la nueva posición como ocupada

            // Notifica a los demás robots que la posición está libre
            occupiedPositions.notifyAll();
        }

        if (new Point(currentStreet, currentAvenue).equals(getPuntoDesbloqueo())) {
            System.out.println("Robot " + id + " ha llegado al punto de desbloqueo. Liberando semáforo...");
            liberarParada(); // Libera el semáforo correspondiente
            System.out.println("Semáforo liberado. Robot " + id + " continúa.");
        }

    }

    @Override
    public void run() {
        realizarTareas();
    }
}