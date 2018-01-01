import java.util.List;
import java.util.Iterator;
/**
 * Fisherman class which describes the characterstics of fisherman.
 * Fisherman moves, catches fish, dies.
 * 
 * @author Gursimran Khalsa
 * @version 09/03/2017
 */
public class Fisherman implements Actor
{
    // The number of steps before the fisherman has to catch again.
    private static final int Fish_CATCH_VALUE = 10;
    
    // The entities ocean
    private final Ocean ocean;
    // Position of the fisherman.
    private Location location;
    //If the fisherman is present in the ocean area or not.
    private boolean alive;
    // Fishermans fish catching level. 
    private int catchLevel = 50;
    //bait to use to catch fish
    private int bait = 50;
    
    //Didn't extend to animal due to not needing the breeding attributes so
    //didn't know how to avoid using the breeding methods.
    /**
     * Creates a fisherman.
     * @param ocean The ocean currently occupied.
     * @param location The location within the ocean.
     */
    public Fisherman(Ocean ocean, Location location)
    {
        this.ocean = ocean;
        setLocation(location);
        alive = true;
    }
    
    /**
     * The purpose of the fisherman and what it does in the ocean. 
     * It catches fish (Was going to add other features to make the simulation
     * more detailed such as avoiding sharks and whales or keeping track and displaying
     * the amount of fish the fishermen have caught in total but due to time constraints
     * I wasn't able to implement exactly what I wanted.)
     * @param newFishermen A list of newly created fisherman actors.
     */
    public void act(List<Actor> newFishermen)
    {
        incrementCatch();
        if(isActive()) {
            Location newLocation = findFish();
            if(newLocation == null) {
                newLocation = getOcean().freeAdjacentLocation(getLocation());
            }
            
            if(newLocation != null) {
                setLocation(newLocation);
            }else {
                setDead();
            }
        }
    }
    
    /**
     * Creates a fisherman actor to put in the ocean.
     * @param ocean The ocean currently occupied.
     * @param location The location in the ocean to assign to actor.
     */
    public Actor createActor(Ocean ocean, Location location)
    {
        return new Fisherman(ocean, location);
    }
    
    /**
     * Make the fisherman catch more fish. If the catch level is 0 then it
     * will remove the fisherman from the ocean.
     */
    private void incrementCatch(){
        catchLevel--;
        if(catchLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Allows the fisherman to locate the fish by looking for fishes adjacent to it's location.
     * @return The location of the fish that was caught.
     */
    private Location findFish()
    {
        Ocean ocean = getOcean();
        List<Location> adjacent = ocean.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Actor animal = ocean.getObjectAt(where);
            if(animal instanceof Fish) {
                Fish Fish = (Fish) animal;
                if(bait >= 0) {
                    if(Fish.isActive()){
                        bait--;
                        Fish.setDead();
                        catchLevel = Fish_CATCH_VALUE;
                        return where;
                    }
                }else{
                    setDead();
                }
            }
        }
        return null;
    }
    
    /**
     * Return the location of the fisherman.
     * @return The location of the fisherman.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the fisherman at the new location in the ocean.
     * @param newLocation The fisherman's new location, within ocean.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            ocean.clear(location);
        }
        location = newLocation;
        ocean.place(this, newLocation);
    }
    
    /**
     * Returns the ocean currently occupied.
     * @return The ocean currently occupied.
     */
    public Ocean getOcean()
    {
        return ocean;
    }
    
    /**
     * Returns the amount of bait the fisherman has.
     * @return The amount of bait the fisherman has.
     */
    public int getBait() 
    {
        return bait;
    }
    
    /**
     * Check whether the fisherman is alive or not.
     * @return The value of the alive field. True if still alive.
     */
    public boolean isActive()
    {
        return alive;
    }
    
    /**
     * Remove the fisherman from the ocean.
     * Works whether the fisherman is already dead or not.
     */
    public void setDead()
    {
        if(alive) {
            alive = false;
            ocean.clear(location);
        }
    }
    
}

