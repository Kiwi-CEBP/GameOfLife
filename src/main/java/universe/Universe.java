package universe;

import animal.Animal;
import cell.Cell;

import java.util.*;

public class Universe {
    private Map<List<Integer>, Cell> cells = new HashMap<>();
    private List<Animal> animals = new ArrayList<>();
    private int maxTurns = 100;

    public Universe() {
    }

    public Universe(int maxTurns) {
        this.maxTurns = maxTurns;
    }

    public void setCells (Map<List<Integer>, Cell> cells) {
        this.cells = cells;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void playTheGame() {
        while((animals.size() > 0) && (maxTurns > 0)) {
            for (Animal animal : animals) {
                animal.live();
            }
            maxTurns--;
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