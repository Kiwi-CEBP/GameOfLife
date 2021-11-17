package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class AnimalSexual extends Animal{
    Semaphore reproductionSemaphore = new Semaphore(1);

    public AnimalSexual(Universe universe, Cell cell) {
        super(universe, cell);
        animal_index = Thread.currentThread().getId() + " S " + Creator.animal_count++;
    }

    public boolean reproduce(){
        try {
            boolean success = false;
            reproductionSemaphore.acquire();
            if (this.isLookingForPartner()) {
                System.out.println(animal_index + " is looking to reproduce");
                success = findPartnerAndMate();
                System.out.println(animal_index + " is no longer looking for mate");
            }
            reproductionSemaphore.release();

            waitForMate(success);
            return success;

        } catch (InterruptedException e) {
            e.printStackTrace();
            reproductionSemaphore.release();
            return false;
        }
    }

    private void waitForMate(boolean didReproduce) {
        if(this.isLookingForPartner() && !didReproduce) {
            try {
                System.out.println(animal_index + " is waiting for mate 100 milliseconds");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tryReproduction(){
        try {
            boolean didReproduce = false;
            if(reproductionSemaphore.tryAcquire(getRandWaitTime(), TimeUnit.MILLISECONDS)) {
                if (this.isLookingForPartner()) {
                    giveBirth();
                    didReproduce = true;
                }
                reproductionSemaphore.release();
            }
            return didReproduce;

        } catch (InterruptedException e) {
            e.printStackTrace();
            reproductionSemaphore.release();
            return false;
        }
    }

    private AnimalSexual giveBirth(){
        growth = 0;
        lookingForPartner = false;

        List<Cell> emptyCell = getListOfEmptyNeighbours();
        for(Cell cell : emptyCell){
            if (cell.occupyCell(this)){
                AnimalSexual newAnimal = new AnimalSexual(universe, cell);
                universe.addAnimal(newAnimal);
                return newAnimal;
            }
        }
        return null;
    }

    private List<AnimalSexual> getListOfAnimalNeighbors(){
        List<AnimalSexual> animalList = new ArrayList<>();
        for (Cell cell : getListOfNeighbours()) {
            if (!cell.isEmpty())
                if (cell.getPresentAnimal() instanceof AnimalSexual) {
                    System.out.println("\t" + animal_index + " has neighbour on: " + cell.getCoordinates().toString());
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

    private int getRandWaitTime() {
        return new Random().nextInt(75) + 25;
    }
}
