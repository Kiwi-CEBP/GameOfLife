package univers;

import animal.Animal;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniversTest {

    @Test
    public void testUniverseAnimalCreation() {


        Univers univers = new Univers();
        List<Animal> animals = univers.getAnimals();
        assertEquals(7, animals.size());
    }
}
