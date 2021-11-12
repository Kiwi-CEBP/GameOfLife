package universe;

import animal.Animal;
import cell.Cell;
import creator.Creator;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Universe {
    private Map<Point, Cell> cells = new HashMap<>();
    private List<Animal> animals = new ArrayList<>();
    private int maxTurns = 100;

    public Universe() {
    }

    public Universe(int maxTurns) {
        this.maxTurns = maxTurns;
    }

    public void setCells (Map<Point, Cell> cells) {
        this.cells = cells;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void playTheGame() {
        for (Animal a: animals) {
            Creator.service.execute(a);
        }
    }

    public void addAnimal (Animal animal) {
        animals.add(animal);
        Creator.service.execute(animal);
    }

    public void removeAnimal (Animal animal) {
        animals.remove(animal);
    }

    public Map<Point, Cell> getCells() {
        return cells;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}