import java.util.List;
/**
 * Details of the ocean.
 * 
 * @author Gursimran Khalsa
 * @version 07/03/2017
 */
public interface OceanView
{
    abstract public int getDepth();
    
    abstract public int getWidth();
    
    abstract public Actor getObjectAt(Location location);
    
    abstract public Actor getObjectAt(int row, int col);
    
    abstract public List<Location> getFreeAdjacentLocations(Location location);
}
