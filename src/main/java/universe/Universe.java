package univers;

import animal.Animal;
import cell.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Univers {
    private Map<List<Integer>, Cell> cells = new HashMap<>();
    private List<Animal> animals = new ArrayList<>();

    public Univers(Map<List<Integer>, Cell> cells,
                   List<Animal> animals) {
        this.cells = cells;
        this.animals = animals;

    }

    public Map<List<Integer>, Cell> getCells() {
        return cells;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
