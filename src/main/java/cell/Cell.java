package cell;
import animal.*;
import java.util.List;
import java.util.Map;

public class Cell {

    public boolean giveFood() {
        return true;
    }

    public boolean placeFood() {
        return true;
    }

    public Map<List<Integer>, Cell> getNeighbours() {
        return null;
    }

    public List<Integer> getCoordinates() {
        return null;
    }

    public Animal getPresentAnimal() {
        return null;
    }

    public boolean occupyCell(Animal animal) {
        return true;
    }

    public boolean freeCell() {
        return true;
    }

    public boolean isEmpty() {
        return true;
    }
}