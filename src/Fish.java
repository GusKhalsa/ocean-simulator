import java.util.List;
import java.util.Random;

/**
 * A simple model of a Fish.
 * Fishs age, move, breed, and die.
 * 
 * @author David J. Barnes, Michael Kolling and Gursimran Khalsa
 * @version 08/03/2017
 */
public class Fish extends Animal
{
    // Characteristics shared by all Fishess (static oceans).

    // The age at which a Fish can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a Fish can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a Fish breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    
    /**
     * Create a new Fish. A Fish may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Fish will have a random age.
     * @param ocean The ocean currently occupied.
     * @param location The location within the ocean.
     */
    public Fish(boolean randomAge, Ocean ocean, Location location)
    {
        super(ocean, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the Fish does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newFishs A list to add newly born Fishes to.
     */
    public void act(List<Actor> newFishes)
    {
        incrementAge();
        if(isActive()) {
            giveBirth(newFishes);            
            // Try to move into a free location.
            Location newLocation = getOcean().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Creates a fish actor.
     * @param randomAge If true a random age is assigned to the actor.
     * @param ocean The ocean currently occupied. 
     * @param location The location to assign to the actor in the ocean.
     */
    public Actor createActor(boolean randomAge, Ocean ocean, Location location)
    {
        return new Fish(randomAge, ocean, location);
    }
        
    /**
     * Return the maximal age of the Fish.
     * @return The maximal age of the Fish.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Return the breeding age of the Fish.
     * @return The breeding age of the Fish.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Return the breeding probability of the Fish.
     * @return The breeding probability of the Fish.
     */
    
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Return the maximal litter size of the Fish.
     * @return The maximal litter size of the Fish.
     */
    
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
}
