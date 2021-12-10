package cell;
import animal.*;

import java.awt.*;
import java.util.Map;

public class Cell {
    private Point coordinates;
    private Map<Point,Cell> neighbours;
    private Animal presentAnimal;
    private boolean food;
    private boolean occupied;

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
        if(occupied){
            return false;
        }
        occupied = true;
        presentAnimal = animal;
        return true;
    }

    public boolean isEmpty(){
        return !occupied;
    }

    public void freeCell(){
        presentAnimal = null;
        occupied = false;
    }
}