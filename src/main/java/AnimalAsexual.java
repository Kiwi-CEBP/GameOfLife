import java.util.List;

public class AnimalAsexual extends Animal{
    public AnimalAsexual(Universe universe, Cell cell) {
        super(universe, cell);
    }

    public boolean isLookingForPartner(){
        return false;
    }

    private Animal giveBirth(){
        List<Cell> emptyCell = getListOfEmptyNeighbors();
        for(Cell c : emptyCell){
            if (c.occupyCell(this)){
                return new AnimalAsexual(universe, c);
            }
        }
        return null;
    }

    public boolean reproduce(){
        Animal child = giveBirth();
        if (child != null)
            return true;
        return false;
    }


}
