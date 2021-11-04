package animal;

import cell.Cell;
import universe.Universe;

import java.util.List;

public class AnimalAsexual extends Animal{
    public AnimalAsexual(Universe universe, Cell cell) {
        super(universe, cell);
    }

    public boolean reproduce(){
        Animal child = giveBirth();
        if (child != null)
            return true;
        return false;
    }

    public boolean isLookingForPartner(){
        return false;
    }

    private Animal giveBirth(){
        List<Cell> emptyCell = getListOfEmptyNeighbours();
        for(Cell c : emptyCell){
            if (c.occupyCell(this)){
                Animal newAnimal = new AnimalAsexual(universe, c);
                universe.addAnimal(newAnimal);
                return newAnimal;
            }
        }
        return null;
    }
}
