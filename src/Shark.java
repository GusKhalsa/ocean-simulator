import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a Shark.
 * Sharkes age, move, eat fishes, and die.
 * 
 * @author David J. Barnes, Michael Kolling and Gursimran Khalsa
 * @version 08/03/2017
 */
public class Shark extends Animal
{
    // Characteristics shared by all Sharkes (static oceans).
    
    // The age at which a Shark can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a Shark can live.
    private static final int MAX_AGE = 160;
    // The likelihood of a Shark breeding.
    private static final double BREEDING_PROBABILITY = 0.35;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The food value of a single Fish. In effect, this is the
    // number of steps a Shark can go before it has to eat again.
    private static final int Fish_FOOD_VALUE = 9;
    
    // Individual characteristics (instance oceans).
    // The Shark's food level, which is increased by eating Fishs.
    private int foodLevel;
    
    /** 
     * Internal class invariants:
     * Those of superclass plus food level within sensible range.
     */
    public void sane()
    {
        super.sane();
        assert 0 <= foodLevel && foodLevel <= Fish_FOOD_VALUE :
            "Food level " + foodLevel + " outside range";
    }

    /**
     * Create a Shark. A Shark can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Shark will have random age and hunger level.
     * @param ocean The ocean currently occupied.
     * @param location The location within the ocean.
     */
    public Shark(boolean randomAge, Ocean ocean, Location location)
    {
        super(ocean, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(Fish_FOOD_VALUE-1)+1;
        }
        else {
            foodLevel = Fish_FOOD_VALUE;
        }
        
        sane();
    }
    
    /**
     * This is what the Shark does most of the time: it hunts for
     * Fishs. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newSharkes A list to add newly born Sharkes to.
     */
    public void act(List<Actor> newSharkes)
    {
        sane();
        
        incrementAge();
        incrementHunger();
        if(isActive()) {
            giveBirth(newSharkes);            
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
     * Creates a shark actor.
     * @param randomAge A random age is assigned if true.
     * @param ocean The ocean currently occupied
     * @param The location in the ocean to assign to actor.
     */
    public Actor createActor(boolean randomAge, Ocean ocean, Location location)
    {
        return new Shark(randomAge, ocean, location);
    }
    
    /**
     * Make this Shark more hungry. This could result in the Shark's death.
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
     * Tell the Shark to look for Fishs adjacent to its current location.
     * Only the first live Fish is eaten.
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
            if(animal instanceof Fish) {
                Fish Fish = (Fish) animal;
                if(Fish.isActive()) { 
                    Fish.setDead();
                    foodLevel = Fish_FOOD_VALUE;
                    // Remove the dead Fish from the ocean.
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
