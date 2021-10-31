package universe;

import animal.Animal;
import cell.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Universe {
    private Map<List<Integer>, Cell> cells = new HashMap<>();
    private List<Animal> animals = new ArrayList<>();

    public Universe(Map<List<Integer>, Cell> cells,
                    List<Animal> animals) {
        this.cells = cells;
        this.animals = animals;

    }

    public void playTheGame() {
        for (Animal animal : animals) {
            animal.live();
        }
    }

    public void addAnimal (Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal (Animal animal) {
        animals.remove(animal);
    }

    public Map<List<Integer>, Cell> getCells() {
        return cells;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
