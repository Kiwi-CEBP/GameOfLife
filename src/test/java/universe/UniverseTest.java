package universe;

import animal.Animal;
import org.junit.Ignore;
import org.junit.Test;
import universe.Universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UniverseTest {

    @Ignore
    @Test
    public void testPlayTheGame() {
        Universe universe = new Universe ();
        List<Animal> mockAnimalList = generateMockAnimalList(universe, 7);
        universe.setAnimals(mockAnimalList);
        universe.playTheGame();

        for (Animal animal : mockAnimalList) {
            verify(animal, atLeast(1)).live();
        }
    }


    //================================= TEST UTILS =================================//

    private List<Animal> generateMockAnimalList(Universe universe, int animalNr) {
        List<Animal> mockAnimalList = new ArrayList<>();
        for (int i = 0; i < animalNr; i++) {
            Animal mockAnimal = mock(Animal.class);
            when(mockAnimal.live()).then(universe.removeAnimal(mockAnimal));

            mockAnimalList.add(mockAnimal);
        }

        return mockAnimalList;
    }
}
