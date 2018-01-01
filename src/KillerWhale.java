import java.util.List;
import java.util.Iterator;
/**
 * Killer whales eat sharks, move, breed and die.
 * 
 * @author Gursimran Khalsa(gk264)
 * @version 09/03/2017
 */
public class KillerWhale extends Animal
{
    // Characteristics shared by all whales (static oceans).
    
    // The age at which a whale can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a whale can live.
    private static final int MAX_AGE = 160;
    // The likelihood of a whale breeding.
    private static final double BREEDING_PROBABILITY = 0.25;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The food value of a single shark. In effect, this is the
    // number of steps a whale can go before it has to eat again.
    private static final int Shark_FOOD_VALUE = 6;
    
    // Individual characteristics (instance oceans).
    // The Whales' food level, which is increased by eating sharks.
    private int foodLevel;
    
    /** 
     * Internal class invariants:
     * Those of superclass plus food level within sensible range.
     */
    public void sane()
    {
        super.sane();
        assert 0 <= foodLevel && foodLevel <= Shark_FOOD_VALUE :
            "Food level " + foodLevel + " outside range";
    }

    /**
     * Create a Whale. A Whale can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Whale will have random age and hunger level.
     * @param ocean The ocean currently occupied.
     * @param location The location within the ocean.
     */
    public KillerWhale(boolean randomAge, Ocean ocean, Location location)
    {
        super(ocean, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(Shark_FOOD_VALUE-1)+1;
        }
        else {
            foodLevel = Shark_FOOD_VALUE;
        }
        
        sane();
    }
    
    /**
     * This is what the whale does most of the time: it hunts for
     * sharks. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param ocean The ocean currently occupied.
     * @param newWhales A list to add newly born whales to.
     */
    public void act(List<Actor> newWhales)
    {
        sane();
        
        incrementAge();
        incrementHunger();
        if(isActive()) {
            giveBirth(newWhales);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getOcean().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
        
        sane();
    }
    
    /**
     * Creates kille whale actor. 
     * @param randomAge If true a random age is assigned to the actor.
     * @param ocean The ocean currently occupied. 
     * @param location The location to assign to the actor in the ocean.
     */
    public Actor createActor(boolean randomAge, Ocean ocean, Location location)
    {
        return new KillerWhale(randomAge, ocean, location);
    }
    
    /**
     * Make this whale more hungry. This could result in the whales' death.
     */
    private void incrementHunger()
    {
        sane();
        
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
        
        sane();
    }
    
    /**
     * Tell the whale to look for sharks adjacent to its current location.
     * Only the first live shark is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        sane();
        
        Ocean ocean = getOcean();
        List<Location> adjacent = ocean.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Actor animal = ocean.getObjectAt(where);
            if(animal instanceof Shark) {
                Shark Shark = (Shark) animal;
                if(Shark.isActive()) { 
                    Shark.setDead();
                    foodLevel = Shark_FOOD_VALUE;
                    // Remove the dead shark from the ocean.
                    sane();
                    return where;
                }
            }
        }
        sane();
        return null;
    }
        
    /**
     * Return the maximal age of the Shark.
     * @return The maximal age of the Shark.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Return the breeding age of the Shark.
     * @return The breeding age of the Shark.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Return the breeding probability of the Shark.
     * @return The breeding probability of the Shark.
     */
    
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Return the maximal litter size of the Shark.
     * @return The maximal litter size of the Shark.
     */
    
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
