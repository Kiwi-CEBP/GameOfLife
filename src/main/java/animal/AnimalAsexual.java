package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.util.List;

public class AnimalAsexual extends Animal{
    public AnimalAsexual(Universe universe, Cell cell) {
        super(universe, cell);
        animalIndex = "A " + Creator.animal_count++;
    }

    @Override
    public boolean reproduce(){
        System.out.println(animalIndex + " reproduce");
        AnimalAsexual child = giveBirth();
        return child != null;
    }

    @Override
    public ReproductionState isLookingForPartner(){
        return ReproductionState.FALSE;
    }

    private AnimalAsexual giveBirth(){
        growth = 0;
        List<Cell> emptyCell = getListOfEmptyNeighbours();
        for(Cell cell : emptyCell){
            AnimalAsexual newAnimal = new AnimalAsexual(universe, cell);
            if (cell.getPresentAnimal() == newAnimal){
                universe.addAnimal(newAnimal);
                return newAnimal;
            }
        }
        return null;
    }
}
