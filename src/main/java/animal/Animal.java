package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

enum ReproductionState {
    TRUE,
    FALSE,
    WAIT
}

public abstract class Animal implements Runnable{

    //TODO Move these to a utils.Config class
    private static final int TIME_UNTIL_STARVE_TO_DEATH = 10;
    private static final int TIME_TO_REMAIN_FULL = 4;
    private static final int MIN_GROWTH_UNTIL_REPRODUCE = 10;
    private static final int MAX_FOOD_TO_PLACE = 5;
    private static final int MIN_FOOD_TO_PLACE = 1;

    protected Universe universe;
    protected Cell occupiedCell;
    private int timeUntilStarve = TIME_UNTIL_STARVE_TO_DEATH;
    private int timeFull = TIME_TO_REMAIN_FULL;
    protected int growth = 0;
    protected ReproductionState lookingForPartner = ReproductionState.FALSE;
    private boolean alive = true;

    protected String animalIndex;

    public Animal(Universe universe, Cell cell) {
        this.universe = universe;
        occupiedCell = cell;
        cell.occupyCell(this);
    }
    @Override
    public void run() {
        System.out.println(animalIndex +" alive at " + "[" + occupiedCell.getCoordinates().x + "," + occupiedCell.getCoordinates().y + "]");
        live();
    }

    private void live(){
        while(alive) {
            timeFull--;

            move();

            eat();

            if (timeFull <= 0) {
                timeUntilStarve--;
            }

            if (this.isLookingForPartner().equals(ReproductionState.TRUE) || (this instanceof AnimalAsexual && growth >= MIN_GROWTH_UNTIL_REPRODUCE)) {
                reproduce();
            }

            if (timeUntilStarve == 0) {
                alive = false;
            }
        }
        die();
    }

    private boolean move(){
        Cell currentCell = this.occupiedCell;
        List<Cell> emptyCells = getListOfEmptyNeighbours();
        Collections.shuffle(emptyCells);

        for (Cell c : emptyCells) {
            if (c.occupyCell(this)){
                currentCell.freeCell();
                occupiedCell = c;
                System.out.println(animalIndex + " move " + "[" + c.getCoordinates().x + "," + c.getCoordinates().y + "]" );
                return true;
            }
        }
        return false;
    }

    protected List<Cell> getListOfNeighbours() {
        List<Cell> neighbourCells = new ArrayList<>();
        occupiedCell.getNeighbours().forEach((point, cell) -> neighbourCells.add(cell));

        return neighbourCells;
    }

    protected List<Cell> getListOfEmptyNeighbours(){
        List<Cell> emptyCell = new ArrayList<>();

        for (Cell neighbourCell : getListOfNeighbours()) {
            if(neighbourCell.isEmpty())
                emptyCell.add(neighbourCell);
        }

        return emptyCell;
    }

    private void eat(){
        if(occupiedCell.giveFood()) {
            timeUntilStarve = TIME_UNTIL_STARVE_TO_DEATH;
            timeFull = TIME_TO_REMAIN_FULL;
            growth++;

            if(growth>= MIN_GROWTH_UNTIL_REPRODUCE) {
                lookingForPartner = ReproductionState.TRUE;
            }
            System.out.println(animalIndex +" eat");
        }
    }

    public boolean reproduce(){
        System.out.println(animalIndex +" reproduce");
        return false;
    }

    public ReproductionState isLookingForPartner(){
        return lookingForPartner;
    }

    private void die(){
        int foodToPlace = ThreadLocalRandom.current().nextInt(MIN_FOOD_TO_PLACE, MAX_FOOD_TO_PLACE + 1);
        int totalFood = foodToPlace;

        occupiedCell.placeFood();

        for (Cell cell : getListOfNeighbours()) {
            if (foodToPlace <= 0)
                break;

            if(cell.placeFood())
                foodToPlace--;
        }
        System.out.println(animalIndex +" die => food " + (totalFood - foodToPlace));
        occupiedCell.freeCell();
        universe.removeAnimal(this);

        Thread.currentThread().interrupt();
    }
}