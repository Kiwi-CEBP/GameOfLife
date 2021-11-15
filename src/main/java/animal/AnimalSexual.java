package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class AnimalSexual extends Animal{
    Semaphore repSem = new Semaphore(1);

    public AnimalSexual(Universe universe, Cell cell) {
        super(universe, cell);
        animal_index = "S"+ Creator.animal_count++;
    }

    public boolean reproduce(){
        try {
            boolean success = false;
            repSem.acquire();
            //System.out.println(animal_index + " is looking to reproduce");
            if (this.isLookingForPartner()) {
                success = findPartnerAndMate();
            }

            repSem.release();
            return success;

        } catch (InterruptedException e) {
            e.printStackTrace();
            repSem.release();
            return false;
        }
    }

    public boolean tryReproduction(){
        try {
            boolean didReproduce = false;
            repSem.acquire();
            if (this.isLookingForPartner()) {
                giveBirth();
                didReproduce = true;
            }
            repSem.release();
            return didReproduce;

        } catch (InterruptedException e) {
            e.printStackTrace();
            repSem.release();
            return false;
        }
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
            //System.out.println(this.animal_index + " found suitable partner " + a.animal_index);
            if (a.tryReproduction()){
                AnimalSexual child = giveBirth();
                if (child != null)
                    return true;
            }
        }
        return false;
    }
}
