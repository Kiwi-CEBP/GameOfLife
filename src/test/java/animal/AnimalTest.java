package animal;

import cell.Cell;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import universe.Universe;

import static org.junit.Assert.*;

public class AnimalTest {
    AnimalSexual as1;
    AnimalSexual as2;
    AnimalAsexual aa;


    /*@Before
    public void setUp() {
        Universe universe= new Universe();
        Cell cell = new Cell();
        as1 = new AnimalSexual(universe, cell);
        as2 = new AnimalSexual(universe, cell);
        aa = new AnimalAsexual(universe, cell);
    }

    @Test
    public void testConstructor() {
        assertNotNull("Sexual Animal null", as1);
        assertNotNull("Sexual Animal null", as2);
        assertNotNull("Asexual Animal null", aa);
    }

    @Ignore
    @Test
    public void testLive(){
        try{
            as1.live();
            as2.live();
            aa.live();
        }catch(Exception e){
            fail("Live fail" + e);
        }
    }

    @Test
    public void testLookingForPartner(){
        assertFalse(aa.isLookingForPartner());
        assertNotNull(as1.isLookingForPartner());
        assertNotNull(as2.isLookingForPartner());
    }*/
}
