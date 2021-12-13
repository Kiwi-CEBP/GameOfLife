package cell;
import animal.*;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Cell {
    private Point coordinates;
    private Map<Point,Cell> neighbours;
    private Animal presentAnimal;
    private boolean food;
    private boolean occupied;
    private Semaphore cellSemaphore = new Semaphore(1);

    public Cell(Point coordinates){
        this.coordinates = coordinates;
        presentAnimal = null;
        food = false;
        occupied = false;
    }

    public void setNeighbours(Map<Point,Cell> neighbours){
        this.neighbours = neighbours;
    }

    public boolean giveFood(){
        if(food){
            food = false;
            return true;
        }
        return false;
    }

    public boolean placeFood(){
        if(!food){
            food = true;
            return true;
        }
        return false;
    }

    public Map<Point,Cell> getNeighbours(){
        return neighbours;
    }

    public Point getCoordinates(){
        return coordinates;
    }

    //TODO Acquire semaphore when checking animal
    public Animal getPresentAnimal(){
        return presentAnimal;
    }

    public boolean occupyCell(Animal animal){
        try {
            cellSemaphore.acquire();
            if (occupied) {
                cellSemaphore.release();
                return false;
            }

            occupied = true;
            presentAnimal = animal;

            cellSemaphore.release();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmpty(){
        return !occupied;
    }

    public void freeCell(){
        presentAnimal = null;
        occupied = false;
    }
}