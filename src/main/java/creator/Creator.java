package creator;

import animal.Animal;
import animal.AnimalAsexual;
import animal.AnimalSexual;
import cell.Cell;
import universe.Universe;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Creator {
    public static int animal_count = 0;
    private final static int NUMBER_OF_THREADS = 20;
    public final int MAP_HEIGHT = 10;
    public final int MAP_WIDTH = 10;
    public final int INITIAL_FOOD_NUMBER = 10000;
    public final int INITIAL_ANIMAL_NUMBER = 3;
    public static final int GAME_TIME_SECONDS = 5;
    Universe universe = new Universe();
    private Map<Point, Cell> cells = new HashMap<>();
    private List<Animal> animals = new ArrayList<>();
    public final static Semaphore semaphore = new Semaphore(1);
    public final static ExecutorService service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Creator() {
        createCells();
        setCellNeighbours();
        setInitialFood();
        universe.setCells(cells);
        createAnimals();
        universe.setAnimals(animals);
    }

    public void startGame(){
        universe.playTheGame();
        waitForEndGame();
    }

    private void createCells() {
        for(int x=0; x<MAP_HEIGHT; x++)
            for(int y=0; y<MAP_WIDTH; y++) {
                Point p = new Point(x,y);
                cells.put(p, new Cell(p));
            }
    }

    private void setCellNeighbours() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Point cellCoordinates = new Point(i, j);

                Map<Point, Cell> neighbours = new HashMap<>();
                for (int x = i - 1; x <= i + 1; x++)
                    for (int y = j - 1 ; y <= j + 1; y++) {
                        Point p = new Point(x, y);

                        if (validCoordinates(x, y) && !cellCoordinates.equals(p)) {
                            neighbours.put(p, cells.get(p));
                        }

                        cells.get(cellCoordinates).setNeighbours(neighbours);
                    }
            }
        }
    }

    private boolean validCoordinates(int x, int y) {
        if (x < 0 || x >= MAP_HEIGHT)
            return false;
        if (y < 0 || y >= MAP_WIDTH)
            return false;

        return true;
    }

    private void setInitialFood() {
        Random rand = new Random();
        for(int i=0; i<INITIAL_FOOD_NUMBER; i++) {
            cells.get(new Point(rand.nextInt(MAP_HEIGHT), rand.nextInt(MAP_WIDTH))).placeFood();
        }
    }

    private void createAnimals() {
        Random rand = new Random();
        for(int i=0; i<INITIAL_ANIMAL_NUMBER; i++) {
            Cell c = cells.get(new Point(rand.nextInt(MAP_HEIGHT), rand.nextInt(MAP_WIDTH)));
            Animal a;
            if(Math.random() > 1){
                a =new AnimalAsexual(universe, c);
            } else {
                a = new AnimalSexual(universe, c);
            }
            animals.add(a);
        }
    }

    public static void waitForEndGame() {
        try{
            service.awaitTermination(GAME_TIME_SECONDS, TimeUnit.SECONDS);
            service.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
