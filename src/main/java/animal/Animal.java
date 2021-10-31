package animal;

import universe.Universe;

import java.util.Random;

public class Animal {
    private Universe universe;
    public Animal (Universe universe) {
        this.universe = universe;
    }

    public void live() {
        int rand = new Random().nextInt(100);
        if(rand < 50) {
            universe.removeAnimal(this);
        }
    }
}
