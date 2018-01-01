import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kolling, Olaf Chitil and Gursimran Khalsa (gk264)
 * @version 2017/02/22
 */
public abstract class Animal implements Actor
{
    // Characteristics shared by all animals (static oceans).
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    // The animal's age.
    private int age;
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's ocean.
    private final Ocean ocean;
    // The animal's position in the ocean.
    private Location location;
    
    /**
     * Internal class invariants:
     * Age not negative.
     * Animals turn at most maximum age plus 1.
     * Alive animals are at most of maximum age.
     * Ocean and location are not null.
     */
    public void sane()
    {
        assert age >= 0 : "The age is negative";
        assert age <= getMaxAge() + 1 : "Too old"; 
        assert ocean != null : "The ocean is null";
        assert location != null : "The location is null";
    }
    
    /**
     * Create a new animal at location in ocean.
     * 
     * @param ocean The ocean currently occupied, not null.
     * @param location The location within the ocean.
     */
    public Animal(Ocean ocean, Location location)
    {
        assert ocean != null : "Ocean is null";
        assert ocean.inside(location) : "Location is not within the ocean";
        
        age = 0;
        alive = true;
        this.ocean = ocean;
        setLocation(location);
        
        this.sane();
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to add newly born animals to.
     */
    abstract public void act(List<Actor> newActors);
    
    /**
     * Set the animal's age to the given value.
     * Age is not negative.
     * @param a New age.
     */
    public void setAge(int a) 
    {
        assert a >= 0 : "Setting age negative";
        
        sane();
        age = a;
        sane();
    }

    /**
     * Increase the age. This could result in the animal's death.
     */
    public void incrementAge()
    {
        sane();
        
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
        
        sane();
    }
    
    /**
     * An animal can breed if it has reached the breeding age.
     * @return Whether the animal can breed.
     */
    public boolean canBreed()
    {
        sane();
        return age >= getBreedingAge();
    }  
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        sane();
        
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        
        sane();
        return births;
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isActive()
    {
        sane();
        
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the ocean.
     * Works whether the animal is already dead or not.
     */
    public void setDead()
    {
        sane();
        
        if (alive) {
            alive = false;
            ocean.clear(location);
        }
        
        sane();
    }
    
    abstract public Actor createActor(boolean randomAge, Ocean ocean, Location location);
    
     /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to add newly born rabbits to, not null.
     */
    protected void giveBirth(List<Actor> newAnimals)
    {
        assert newAnimals != null : "New rabbits list is null";
        
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Ocean ocean = getOcean();
        List<Location> free = ocean.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            newAnimals.add(createActor(false, ocean, loc));
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        sane();
        
        return location;
    }
    
    /**
     * Return the animal's ocean.
     * @return The animal's ocean.
     */
    public Ocean getOcean()
    {
        sane();
        
        return ocean;
    }
    
    /**
     * Place the animal at the new location in the given ocean.
     * @param newLocation The animal's new location, within ocean.
     */
    public void setLocation(Location newLocation)
    {
        assert ocean.inside(newLocation) : "Location is not within the ocean";
        // sane();  no, because this method is also used in constructor
        
        if(location != null) {
            ocean.clear(location);
        }
        location = newLocation;
        ocean.place(this, newLocation);
        
        sane();
    }
    
    /**
     * Return the breeding probability of this animal.
     * @return The breeding probability of this animal.
     */
    
    abstract public double getBreedingProbability();
    
    /**
     * Return the maximal litter size of this animal.
     * @return The maximal litter size of this animal.
     */
    
    abstract public int getMaxLitterSize();

    /** 
     * Return the breeding age of this animal.
     * @return The breeding age of this animal.
     */
    abstract public int getBreedingAge();
    
    /**
     * Return the maximal age of this animal.
     * @return The maximal age of this animal.
     */
    abstract public int getMaxAge();
    
}
