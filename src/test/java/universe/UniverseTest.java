package universe;

import animal.Animal;
import org.junit.Test;
import universe.Universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class UniverseTest {

    @Test
    public void testUniverseAnimalCreation() {
        Universe universe = new Universe(new HashMap<>(), generateMockAnimalList(7));
        List<Animal> animals = universe.getAnimals();
        assertEquals(7, animals.size());
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
