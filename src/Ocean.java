import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represent a rectangular grid of Ocean positions.
 * Each position is able to store a single animal.
 * 
 * @author David J. Barnes, Michael Kolling, Olaf Chitil and Gursimran Khalsa
 * @version 07/03/2017
 */
public class Ocean implements OceanView
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The depth and width of the Ocean.
    private int depth, width;
    // Storage for the animals.
    private final Actor[][] Ocean;

    /**
     * Represent a ocean of the given dimensions.
     * @param depth The depth of the ocean; positive.
     * @param width The width of the ocean; positive.
     */
    public Ocean(int depth, int width)
    {
        assert depth > 0 : "Depth not positive";
        assert width > 0 : "Width not positive";
        
        this.depth = depth;
        this.width = width;
        Ocean = new Actor[depth][width];
        clear();
    }
    
    /**
     * Empty the ocean.
     */
    public void clear()
    {
        for(int row = 0; row < depth; row++) {
            for(int col = 0; col < width; col++) {
                Ocean[row][col] = null;
            }
        }
    }
    
    /**
     * Clear the given location. It is inside the ocean.
     * @param location The location to clear.
     */
    public void clear(Location location)
    {
        assert inside(location) : "Location not within the Ocean";
        
        Ocean[location.getRow()][location.getCol()] = null;
    }
    
    /**
     * Place an animal at the given location, inside the ocean.
     * At the given location the ocean is free.
     * @param animal The animal to be placed, not null.
     * @param row Row coordinate of the location.
     * @param col Column coordinate of the location.
     */
    public void place(Actor animal, int row, int col)
    {
        assert animal != null : "Animal is null";
        assert inside(new Location(row,col)) : "Location not within Ocean";
        assert getObjectAt(row,col) == null : "Ocean location is free";
        
        place(animal, new Location(row, col));
    }
    
    /**
     * Place an animal at the given location.
     * @param animal The animal to be placed, not null.
     * @param location Where to place the animal, inside the Ocean.
     */
    public void place(Actor animal, Location location)
    {
        assert animal != null : "Animal is null";
        assert inside(location) : "Location not within Ocean";
        assert getObjectAt(location) == null : "Ocean location is free";
        
        Ocean[location.getRow()][location.getCol()] = animal;
    }
    
    /**
     * Return the animal at the given location, if any.
     * @param location Where, inside the Ocean.
     * @return The animal at the given location, or null if there is none.
     */
    public Actor getObjectAt(Location location)
    {
        assert inside(location) : "Location not within Ocean";
        
        return getObjectAt(location.getRow(), location.getCol());
    }
    
    /**
     * Return the animal at the given location (inside the Ocean), if any.
     * @param row The desired row.
     * @param col The desired column.
     * @return The animal at the given location, or null if there is none.
     */
    public Actor getObjectAt(int row, int col)
    {
        assert inside(new Location(row,col)) : "Location not within Ocean";

        return Ocean[row][col];
    }
    
    /**
     * Generate a random location that is adjacent to the
     * given location, or is the same location.
     * The returned location will be within the valid bounds
     * of the Ocean.
     * @param location The location from which to generate an adjacency, inside the Ocean.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location)
    {
        assert inside(location) : "Location not within Ocean";

        List<Location> adjacent = adjacentLocations(location);
        Location newLocation = adjacent.get(0);
        
        assert inside(newLocation) : "New location not within Ocean";
        return newLocation;
    }
    
    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this, inside the Ocean.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        assert inside(location) : "Location not within Ocean";

        List<Location> free = new LinkedList<Location>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            if(getObjectAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }
    
    /**
     * Try to find a free location that is adjacent to the
     * given location. If there is none, return null.
     * The returned location will be within the valid bounds
     * of the Ocean.
     * @param location The location from which to generate an adjacency, inside the Ocean.
     * @return A valid location within the grid area or null.
     */
    public Location freeAdjacentLocation(Location location)
    {
        assert inside(location) : "Location not within Ocean";

        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if(free.size() > 0) {
            return free.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies, inside the Ocean.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location)
    {
        assert inside(location) : "Location not within Ocean";
    
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<Location>();
        if(location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return the depth of the Ocean.
     * @return The depth of the Ocean.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the Ocean.
     * @return The width of the Ocean.
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Check whether given location is within the Ocean (which includes not being null).
     * @return The decision.
     */
    public boolean inside(Location location)
    {
        return location != null && location.getRow() < depth && location.getCol() < width;
    }
}
