package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal implements Runnable{

    //TODO Move these to a utils.Config class
    private static final int TIME_UNTIL_STARVE_TO_DEATH = 10;
    private static final int TIME_TO_REMAIN_FULL = 4;
    private static final int MIN_GROWTH_UNTIL_REPRODUCE = 10;
    private static final int MAX_FOOD_TO_PLACE = 5;
    private static final int MIN_FOOD_TO_PLACE = 1;

    protected Universe universe;
    private Cell occupiedCell;
    protected Map<Point,Cell> neighbourCells;
    private int timeUntilStarve = TIME_UNTIL_STARVE_TO_DEATH;
    private int timeFull = TIME_TO_REMAIN_FULL;
    protected int growth = 0;
    protected boolean lookingForPartner = false;
    private boolean alive = true;

    protected String animal_index;

    public Animal(Universe universe, Cell cell) {
        this.universe = universe;
        occupiedCell = cell;
        cell.occupyCell(this);
        neighbourCells =  occupiedCell.getNeighbours();
    }
    @Override
    public void run() {
        System.out.println(animal_index+" alive");
        live();
    }

    private void live(){
        while(alive) {
            timeFull--;
            try {
                Creator.semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move();
            Creator.semaphore.release();
            eat();
            if (timeFull <= 0)
                timeUntilStarve--;

            reproduce();

            if (timeUntilStarve == 0) {
                alive = false;
            }
        }
        die();
    }

    private boolean move(){
        List<Cell> emptyCells = getListOfEmptyNeighbours();
        for (Cell c : emptyCells) {
            if (c.occupyCell(this)){
                occupiedCell = c;
                neighbourCells = occupiedCell.getNeighbours();
                System.out.println(animal_index+" move "+c.getCoordinates());
                return true;
            }
        }
        return false;
    }

    protected List<Cell> getListOfEmptyNeighbours(){
        List<Cell> emptyCell = new ArrayList<Cell>();
        Iterator<Map.Entry<Point,Cell>> itr = neighbourCells.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<Point,Cell> entry = itr.next();
            if(entry.getValue().isEmpty())
                emptyCell.add(entry.getValue());
        }
        return emptyCell;
    }

    private void eat(){
        if(occupiedCell.giveFood()) {
            timeUntilStarve = TIME_UNTIL_STARVE_TO_DEATH;
            timeFull = TIME_TO_REMAIN_FULL;
            growth++;

            if(growth>= MIN_GROWTH_UNTIL_REPRODUCE) {
                lookingForPartner = true;
            }
            System.out.println(animal_index+" eat");
        }
    }

    public boolean reproduce(){
        System.out.println(animal_index+" reproduce");
        return false;
    }

    public boolean isLookingForPartner(){
        return lookingForPartner;
    }

    private void die(){
        int foodToPlace = ThreadLocalRandom.current().nextInt(MIN_FOOD_TO_PLACE, MAX_FOOD_TO_PLACE + 1);
        int totalFood = foodToPlace;

        occupiedCell.placeFood();

        Iterator<Map.Entry<Point,Cell>> itr = neighbourCells.entrySet().iterator();
        while(itr.hasNext() && foodToPlace > 0) {
            Map.Entry<Point,Cell> entry = itr.next();
            if(entry.getValue().placeFood())
                foodToPlace--;
        }
        System.out.println(animal_index+" die => food"+(totalFood-foodToPlace));
        occupiedCell.freeCell();
        universe.removeAnimal(this);
    }
}