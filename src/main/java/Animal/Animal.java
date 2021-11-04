package animal;

import cell.Cell;
import universe.Universe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal {

    private static final int TIME_UNTIL_STARVE_TO_DEATH = 10;
    private static final int TIME_TO_REMAIN_FULL = 4;
    private static final int MIN_GROWTH_UNTIL_REPRODUCE = 10;
    private static final int MAX_FOOD_TO_PLACE = 5;
    private static final int MIN_FOOD_TO_PLACE = 1;

    protected Universe universe;
    private Cell occupiedCell;
    protected Map<List<Integer>,Cell> neighbourCells;
    private int timeUntilStarve = TIME_UNTIL_STARVE_TO_DEATH;
    private int timeFull = TIME_TO_REMAIN_FULL;
    protected int growth = 0;
    protected boolean lookingForPartner = false;
    private boolean alive = true;

    public Animal(Universe universe, Cell cell) {
        this.universe = universe;
        occupiedCell = cell;
        cell.occupyCell(this);
        neighbourCells =  occupiedCell.getNeighbours();
    }

    public boolean isLookingForPartner(){
        return lookingForPartner;
    }

    private boolean move(){
        List<Cell> emptyCells = getListOfEmptyNeighbors();
        for (Cell c : emptyCells) {
            if (c.occupyCell(this)){
                occupiedCell = c;
                neighbourCells = occupiedCell.getNeighbours();
                return true;
            }
        }
        return false;
    }

    protected List<Cell> getListOfEmptyNeighbors(){
        List<Cell> emptyCell = new ArrayList<Cell>();
        Iterator<Map.Entry<List<Integer>,Cell>> itr = neighbourCells.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<List<Integer>,Cell> entry = itr.next();
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
        }
    }

    private void die(){
        int foodToPlace = ThreadLocalRandom.current().nextInt(MIN_FOOD_TO_PLACE, MAX_FOOD_TO_PLACE + 1);

        occupiedCell.placeFood();

        Iterator<Map.Entry<List<Integer>,Cell>> itr = neighbourCells.entrySet().iterator();
        while(itr.hasNext() && foodToPlace > 0) {
            Map.Entry<List<Integer>,Cell> entry = itr.next();
            if(entry.getValue().placeFood())
                foodToPlace--;
        }
        occupiedCell.freeCell();
        universe.removeAnimal(this);
    }

    public boolean reproduce(){
        return false;
    }

    public void live(){
        timeFull--;
        move();
        eat();
        if(timeFull <= 0 )
            timeUntilStarve--;
        if(growth>= MIN_GROWTH_UNTIL_REPRODUCE) {
            reproduce();
        }
        if(timeUntilStarve == 0){
            alive = false;
        }

        if(!alive) {
            die();
        }
    }
}
