import kareltherobot.*;



public class Main {
    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.showSpeedControl(true);
        World.setVisible(true);
        String worldText = World.asText(" ");
        KarelWorldParser map = new KarelWorldParser(worldText);
        boolean[][] laberinto = map.matrix;

        int numRobots = 8;

        Racer[] racers = RobotFactory.createRobots(numRobots, laberinto);

        System.out.println("Start the searching:  " + '\n');

        // Crear hilos para cada robot y ejecutarlos
        Thread[] threads = new Thread[numRobots];
        for (int i = 0; i < numRobots; i++) {
            threads[i] = new Thread(racers[i]); // Crear un hilo para cada robot
            threads[i].start(); // Iniciar el hilo
        }

        // Esperar a que todos los hilos terminen
        for (int i = 0; i < numRobots; i++) {
            try {
                threads[i].join(); // Espera a que cada hilo termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All robots finished their tasks.");
    }
}