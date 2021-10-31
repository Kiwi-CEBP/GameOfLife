import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AnimalSexual extends Animal{
    public AnimalSexual(Universe universe, Cell cell) {
        super(universe, cell);
    }

    public boolean isLookingForPartner(){
        if(super.isLookingForPartner()){
            giveBirth();
            return true;
        }
        return false;
    }

    private Animal giveBirth(){
        growth = 0;
        lookingForPartner = false;

        List<Cell> emptyCell = getListOfEmptyNeighbors();
        for(Cell c : emptyCell){
            if (c.occupyCell(this)){
                return new AnimalSexual(universe, c);
            }
        }
        return null;
    }

    protected List<Animal> getListOfAnimalNeighbors(){
        List<Animal> animalList = new ArrayList<Animal>();
        Iterator<Map.Entry<List<Integer>,Cell>> itr = neighbourCells.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<List<Integer>,Cell> entry = itr.next();
            if(!entry.getValue().isEmpty())
                if(entry.getValue().getPresentAnimal() instanceof AnimalSexual)
                    animalList.add(entry.getValue().getPresentAnimal());
        }
        return animalList;
    }

    public boolean findPartnerAndMate(){
        List<Animal> animals = getListOfAnimalNeighbors();
        for (Animal a : animals) {
            if (a.isLookingForPartner()){
                a.reproduce();
                Animal child = giveBirth();
                if (child != null)
                    return true;
            }
        }
        return false;
    }

    public boolean reproduce(){
        this.reproduce();
        return findPartnerAndMate();
    }



}
