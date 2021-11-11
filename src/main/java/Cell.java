import java.util.List;
import java.awt.Point;

public class Cell {
    private Point coordinates;
    private List<Cell> neighbours;
    private Animal presentAnimal;
    private boolean food;
    private boolean occupied;

    public Cell(Point coordinates){
        this.coordinates = coordinates;
        presentAnimal = null;
        food = false;
        occupied = false;
    }

    public setNeighbours(List<Cell> Neighbours){
        this.neighbours = Neighbours;
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

    public List<Cell> getNeighbours(){
        List<Cell> neighbours
    }

    public Point getCoordinates(){
        return coordinates;
    }

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
    }
}
