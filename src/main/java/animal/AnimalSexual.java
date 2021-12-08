package animal;

import cell.Cell;
import creator.Creator;
import universe.Universe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AnimalSexual extends Animal{
    Semaphore reproductionSemaphore = new Semaphore(1);

    public AnimalSexual(Universe universe, Cell cell) {
        super(universe, cell);
        animalIndex = "S " + Creator.animal_count++;
    }

    public boolean reproduce(){
        boolean success = false;
        System.out.println(animalIndex + " is looking to reproduce");

        for (AnimalSexual partner : findPartners()) {
            if(this.isLookingForPartner()) {
                System.out.println("Attempted mating between " + this.animalIndex + " and " + partner.animalIndex);
                attemptMating(partner);
            }
        }
        System.out.println(animalIndex + " is no longer reproducing");

        waitForMate(success);
        return success;
    }

    private List<AnimalSexual> findPartners() {
        List<AnimalSexual> neighbours = getListOfSexualAnimalNeighbors();
        List<AnimalSexual> partners = new ArrayList<>();
        for (AnimalSexual neighbour : neighbours) {
            System.out.println(this.animalIndex + " found neighbour " + neighbour.animalIndex);
            if (neighbour.isLookingForPartner()){
                partners.add(neighbour);
            }
        }

        return partners;
    }

    private boolean attemptMating(AnimalSexual partner) {
        boolean okPartner1 = false;
        boolean okPartner2 = false;

        if (enterMating(this)) {
            okPartner1 = true;
        }
        if (enterMating(partner)) {
            okPartner2 = true;
        }

        if (okPartner1 && okPartner2) {
            this.giveBirth();
            partner.giveBirth();
            return true;
        }

        if (okPartner1) {
            this.lookingForPartner = true;
        }
        if (okPartner2) {
            partner.lookingForPartner = true;
        }
        return false;
    }


    private boolean enterMating(AnimalSexual animal) {
        try {
            boolean enteredMating = false;
            animal.reproductionSemaphore.acquire();
            System.out.println("Acquired semaphore for animal " + animal.animalIndex);

            if (animal.isLookingForPartner()) {
                System.out.println("Animal entered mating " + animal.animalIndex);
                enteredMating = true;
                animal.lookingForPartner = false;
            }

            animal.reproductionSemaphore.release();
            System.out.println("Released semaphore for animal " + animal.animalIndex);
            return enteredMating;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void waitForMate(boolean didReproduce) {
        if(!didReproduce) {
            try {
                int waitTime = getRandWaitTime();
                System.out.println(animalIndex + " is waiting for mate " + waitTime + " milliseconds");
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    private List<AnimalSexual> getListOfSexualAnimalNeighbors(){
        List<AnimalSexual> animalList = new ArrayList<>();
        for (Cell cell : getListOfNeighbours()) {
            Animal neighbourAnimal = cell.getPresentAnimal();
            if (neighbourAnimal != null)
                if (neighbourAnimal instanceof AnimalSexual) {
                    System.out.print("\t" + animalIndex + " has neighbour on: " + cell.getCoordinates().toString());
                    System.out.println(" " + neighbourAnimal.animalIndex);
                    animalList.add((AnimalSexual)  neighbourAnimal);
                }
        }

        return animalList;
    }

    private int getRandWaitTime() {
        return new Random().nextInt(10) + 3;
    }
}
