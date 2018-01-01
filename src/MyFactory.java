import java.awt.Color;
import java.util.Random;
/**
 * A class responsible for creating the initial population
 * of animals in the simulation.
 * 
 * @author David J. Barnes, Michael Kolling, Olaf Chitil and Gursimran Khalsa
 * @version 07/03/2017
 */
public class MyFactory implements Factory
{     
    // Constants representing configuration information for the simulation.
    // The probability that a Shark will be created in any given grid position.
    private static final double Shark_CREATION_PROBABILITY = 0.015;
    // The probability that a Fish will be created in any given grid position.
    private static final double Fish_CREATION_PROBABILITY = 0.08;    
    //The probability that a whale will be created in any given grid position.
    private static final double Whale_CREATION_PROBABILITY = 0.01;
    //The probability that a fisherman will arrive in the ocean in any given grid position.
    private static final double Fisherman_CREATION_PROBABILITY = 0.01;
    /**
     * Optionally create an actor.
     * Whether an actor is created will depend upon probabilities
     * of actor creation.
     * @param ocean The ocean of actors.
     * @param location The location to occupy.
     * @return A newly created Actor, or null if none is created.
     */
    public Actor optionallyCreateActor(Ocean ocean, Location location)
    {
        Random rand = Randomizer.getRandom();
        if(rand.nextDouble() <= Shark_CREATION_PROBABILITY) {
            Actor Shark = new Shark(true, ocean, location);
            return Shark;
        }
        else if(rand.nextDouble() <= Fish_CREATION_PROBABILITY) {
            Actor Fish = new Fish(true, ocean, location);
            return Fish;
        }else if(rand.nextDouble() <= Whale_CREATION_PROBABILITY){
            Actor Whale = new KillerWhale(true, ocean, location);
            return Whale;
        }else if(rand.nextDouble() <= Fisherman_CREATION_PROBABILITY) {
            Actor Fisherman = new Fisherman(ocean, location);
            return Fisherman;
        }
        return null;
    }
    
    /**
     * Associate colors with the simulation actors..
     */
    public void setupColors(SimulatorView view)
    {
        assert view != null : "Simulator view is null";

        view.setColor(Fish.class, Color.yellow);
        view.setColor(Shark.class, Color.red);
        view.setColor(KillerWhale.class, Color.black);
        view.setColor(Fisherman.class, Color.blue);
    }
}
