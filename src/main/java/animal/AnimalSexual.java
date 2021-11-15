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
import java.util.concurrent.TimeUnit;

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
            if (this.isLookingForPartner()) {
                System.out.println(animal_index + " is looking to reproduce");
                success = findPartnerAndMate();
                System.out.println(animal_index + " is no longer reproducing");
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
            repSem.tryAcquire(5, TimeUnit.MILLISECONDS);
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
        List<AnimalSexual> animalList = new ArrayList<>();
        System.out.println(animal_index + " on cell " + this.occupiedCell.getCoordinates().toString() + " has neighbours on:");
        for (Cell cell : getListOfNeighbours()) {
            if (!cell.isEmpty())
                if (cell.getPresentAnimal() instanceof AnimalSexual) {
                    System.out.println(animal_index + ": " + cell.getCoordinates().toString());
                    animalList.add((AnimalSexual) cell.getPresentAnimal());
                }
        }

        return animalList;
    }

    private boolean findPartnerAndMate(){
        List<AnimalSexual> animals = getListOfAnimalNeighbors();
        for (AnimalSexual a : animals) {
            System.out.println(this.animal_index + " found suitable partner " + a.animal_index);
            if (a.tryReproduction()){
                System.out.println(this.animal_index + " reproduced with " + a.animal_index);
                AnimalSexual child = giveBirth();
                if (child != null)
                    return true;
            }
        }
        return false;
    }
}
