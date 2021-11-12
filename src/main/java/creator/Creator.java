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

public class Creator {
    public static int animal_count = 0;
    private final static int NUMBER_OF_THREADS = 20;
    public final int MAP_HEIGHT = 10;
    public final int MAP_WIDTH = 10;
    public final int INITIAL_FOOD_NUMBER = 8;
    public final int INITIAL_ANIMAL_NUMBER = 8;
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
        universe.playTheGame();
        service.shutdown();
    }

    private void createCells() {
        for(int x=0; x<MAP_HEIGHT; x++)
            for(int y=0; y<MAP_WIDTH; y++) {
                Point p = new Point(x,y);
                cells.put(p, new Cell(p));
            }
    }

    private void setCellNeighbours() {
        for(int x=0; x<MAP_HEIGHT; x++)
            for(int y=0; y<MAP_WIDTH; y++) {
                Map<Point, Cell> neighbours = new HashMap<>();
                if(x>0 && y>0){
                    Point p = new Point(x-1,y-1);
                    neighbours.put(p, cells.get(p));
                }
                if(x<MAP_HEIGHT-1 && y<MAP_WIDTH-1) {
                    Point p = new Point(x+1,y+1);
                    neighbours.put(p, cells.get(p));
                }
                if(x>0) {
                    Point p = new Point(x-1, y);
                    neighbours.put(p, cells.get(p));
                }
                if(y>0) {
                    Point p = new Point(x, y-1);
                    neighbours.put(p, cells.get(p));
                }
                if(x>0 && y < MAP_WIDTH-1){
                    Point p = new Point(x-1, y+1);
                    neighbours.put(p, cells.get(p));
                }
                if(x<MAP_HEIGHT-1 && y > 0){
                    Point p = new Point(x+1, y-1);
                    neighbours.put(p, cells.get(p));
                }
                if(x<MAP_HEIGHT-1) {
                    Point p = new Point(x+1, y);
                    neighbours.put(p, cells.get(p));
                }
                if(y>MAP_WIDTH-1) {
                    Point p = new Point(x, y+1);
                    neighbours.put(p, cells.get(p));
                }
                cells.get(new Point(x, y)).setNeighbours(neighbours);
            }
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
            if(Math.random() < 0.5){
                a =new AnimalAsexual(universe, c);
            } else {
                a = new AnimalSexual(universe, c);
            }
            animals.add(a);
        }
    }
}
