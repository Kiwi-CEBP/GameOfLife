package universe;

import animal.Animal;
import org.junit.Test;
import universe.Universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UniverseTest {

    @Test
    public void testPlayTheGame() {
        List<Animal> mockAnimalList = generateMockAnimalList(7);
        Universe universe = new Universe (new HashMap<>(), mockAnimalList);
        universe.playTheGame();

        for (Animal animal : mockAnimalList) {
            verify(animal, atLeast(1)).live();
        }
    }


    //================================= TEST UTILS =================================//

    private List<Animal> generateMockAnimalList(int animalNr) {
        List<Animal> mockAnimalList = new ArrayList<>();
        for (int i = 0; i < animalNr; i++) {
            mockAnimalList.add(mock(Animal.class));
        }

        return mockAnimalList;
    }
}
