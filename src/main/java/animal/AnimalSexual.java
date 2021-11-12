package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AnimalSexual extends Animal{
    public AnimalSexual(Universe universe, Cell cell) {
        super(universe, cell);
        animal_index = "S"+ Creator.animal_count++;
    }

    /*public boolean reproduce(){
        return findPartnerAndMate();
    }*/

    public boolean isLookingForPartner(){
        if(super.isLookingForPartner()){
            giveBirth();
            return true;
        }
        return false;
    }

    private AnimalSexual giveBirth(){
        growth = 0;
        lookingForPartner = false;

        List<Cell> emptyCell = getListOfEmptyNeighbours();
        for(Cell c : emptyCell){
            if (c.occupyCell(this)){
                AnimalSexual newAnimal = new AnimalSexual(universe, c);
                universe.addAnimal(newAnimal);
                return newAnimal;
            }
        }
        return null;
    }

    private List<AnimalSexual> getListOfAnimalNeighbors(){
        List<AnimalSexual> animalList = new ArrayList<AnimalSexual>();
        Iterator<Map.Entry<Point,Cell>> itr = neighbourCells.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<Point,Cell> entry = itr.next();
            if(!entry.getValue().isEmpty())
                if(entry.getValue().getPresentAnimal() instanceof AnimalSexual)
                    animalList.add((AnimalSexual) entry.getValue().getPresentAnimal());
        }
        return animalList;
    }

    private boolean findPartnerAndMate(){
        List<AnimalSexual> animals = getListOfAnimalNeighbors();
        for (AnimalSexual a : animals) {
            if (a.isLookingForPartner()){
                a.reproduce();
                AnimalSexual child = giveBirth();
                if (child != null)
                    return true;
            }
        }
        return false;
    }
}
