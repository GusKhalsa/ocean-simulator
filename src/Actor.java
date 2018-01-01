import java.util.List;

/**
 * Shared characteristics of any actor in a simulation.
 * 
 * @author Olaf Chitil, Gursimran Khalsa
 * @version 05/03/2017
 */
public interface Actor
{

    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to add newly born actors to.
     */
    abstract public void act(List<Actor> newActors);

    /**
     * Check whether the actor is active or not.
     * @return true if the actor is still active.
     */
    public boolean isActive();
    
    /**
     * Return the animal's ocean.
     * @return The animal's ocean.
     */
    abstract public Ocean getOcean();
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    abstract public Location getLocation();
    
}
