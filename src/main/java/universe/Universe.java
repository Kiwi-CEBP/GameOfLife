package universe;

import animal.Animal;
import cell.Cell;

import java.util.List;
import java.util.Map;

public class Universe {
    private Map<List<Integer>, Cell> cells;
    private List<Animal> animals;

    public Map<List<Integer>, Cell> getCells() {
        return cells;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
