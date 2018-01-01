
/**
 * The interface for creating the initial population in a simulation.
 * 
 * @author Olaf Chitil and Gursimran Khalsa
 * @version 2017/03/08
 */
public interface Factory
{
    /**
     * Optionally create an actor.
     * Whether an actor is created will depend upon probabilities
     * of actor creation.
     * @param ocean The ocean of actors.
     * @param location The location to occupy.
     * @return A newly created Actor, or null if none is created.
     */
    public Actor optionallyCreateActor(Ocean ocean, Location location);
    
    /**
     * Associate colors with the actor classes.
     */
    public void setupColors(SimulatorView view);
    
}
