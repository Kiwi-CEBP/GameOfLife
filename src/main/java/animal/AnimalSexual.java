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
    String mateIndex = "";

    public AnimalSexual(Universe universe, Cell cell) {
        super(universe, cell);
        animalIndex = "S " + Creator.animal_count++;
    }

    public boolean reproduce(){
        boolean success = false;
        //System.out.println(animalIndex + " is looking to reproduce on "+ "[" + occupiedCell.getCoordinates().x + "," + occupiedCell.getCoordinates().y + "]");

        List<AnimalSexual> partners = findPartners();
        for (int i = 0; i < partners.size(); i++) {
            if(this.isLookingForPartner().equals(ReproductionState.TRUE)) {
                //System.out.println("Attempted mating between " + this.animalIndex + " and " + partners.get(i).animalIndex);
                attemptMating(partners.get(i));
            }
            else if(this.isLookingForPartner().equals(ReproductionState.WAIT)){
                partners.add(partners.get(i));
            }
        }
        //System.out.println(animalIndex + " is no longer reproducing on "+ "[" + occupiedCell.getCoordinates().x + "," + occupiedCell.getCoordinates().y + "]");

        waitForMate(success);
        return success;
    }

    private List<AnimalSexual> findPartners() {
        List<AnimalSexual> neighbours = getListOfSexualAnimalNeighbors();
        List<AnimalSexual> partners = new ArrayList<>();
        for (AnimalSexual neighbour : neighbours) {
            //System.out.println(this.animalIndex + " found neighbour " + neighbour.animalIndex);
            if (neighbour.isLookingForPartner().equals(ReproductionState.TRUE)){
                partners.add(neighbour);
            }
        }

        return partners;
    }

    private boolean attemptMating(AnimalSexual partner) {
        boolean okPartner1;
        boolean okPartner2;

        okPartner1 = this.enterMating(partner, this);
        if (!okPartner1 || !partner.mateIndex.equals(this.animalIndex)) {
            return false;
        }
        okPartner2 = this.enterMating(this, partner);
        if (!okPartner2 || !this.mateIndex.equals(partner.animalIndex)) {
            partner.lookingForPartner = ReproductionState.TRUE;
            return false;
        }
        System.out.println(this.animalIndex + " Animals " + this.animalIndex + " and " + partner.animalIndex + " mated!!!");
        this.giveBirth();
        partner.giveBirth();
        return true;
    }


    private boolean enterMating(AnimalSexual animal, AnimalSexual mate) {
        try {
            boolean enteredMating = false;
            animal.reproductionSemaphore.acquire();
            //System.out.println(this.animalIndex + "    Acquired semaphore for animal " + animal.animalIndex);

            if (animal.isLookingForPartner().equals(ReproductionState.TRUE)) {
                //System.out.println(animal.animalIndex + "    Animal entered mating " + mate.animalIndex);
                enteredMating = true;
                animal.mateIndex = mate.animalIndex;
                animal.lookingForPartner = ReproductionState.WAIT;
            } else if(animal.isLookingForPartner().equals(ReproductionState.WAIT)) {
                enteredMating = true;
            }

            animal.reproductionSemaphore.release();
            //System.out.println(this.animalIndex + "    Released semaphore for animal " + animal.animalIndex);

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
        if(!this.isLookingForPartner().equals(ReproductionState.WAIT)){
            return null;
        }
        growth = 0;
        List<Cell> emptyCell = getListOfEmptyNeighbours();
        for(Cell cell : emptyCell){
            AnimalSexual newAnimal = new AnimalSexual(universe, cell);
            if (cell.getPresentAnimal() == newAnimal){
                lookingForPartner = ReproductionState.FALSE;
                universe.addAnimal(newAnimal);
                return newAnimal;
            }
        }
        System.out.println(animalIndex + " no place to place baby");
        lookingForPartner = ReproductionState.TRUE;
        return null;
    }

    private List<AnimalSexual> getListOfSexualAnimalNeighbors(){
        List<AnimalSexual> animalList = new ArrayList<>();
        for (Cell cell : getListOfNeighbours()) {
            Animal neighbourAnimal = cell.getPresentAnimal();
            if (neighbourAnimal != null)
                if (neighbourAnimal instanceof AnimalSexual) {
//                    System.out.println("\t" + animalIndex + " has neighbour on: " +
//                            "[" + cell.getCoordinates().x + "," + cell.getCoordinates().y + "]" +
//                            " " + neighbourAnimal.animalIndex);
                    animalList.add((AnimalSexual)  neighbourAnimal);
                }
        }

        return animalList;
    }

    private int getRandWaitTime() {
        return new Random().nextInt(10) + 3;
    }
}
