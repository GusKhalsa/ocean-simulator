import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * A simple predator-prey simulator, based on a rectangular ocean
 * containing fishes , sharks, killer whales and fisherman.
 * 
 * @author David J. Barnes, Michael Kolling, Olaf Chitil and Gursimran Khalsa
 * @version 2016/2/24
 */
public class Simulator
{

    // List of actors in the ocean.
    private final List<Actor> actors;
    // The current state of the ocean.
    private final Ocean ocean;
    // The current step of the simulation.
    private int step;
    // A factory for creating actors - unused as yet.
    private final Factory factory;
    // A view for watching the simulation
    private final View view;
    
    /**
     * Internal class invariants:
     * All actors in list are alive.
     * Same actors on ocean and in list.
     * Locations in ocean and in actors should agree.
     * Simulation and all actors use the same ocean.
     */
    public void sane()
    {
        assert actors != null : "The list of actors is null";
        assert ocean != null : "The ocean is null";
        assert step >= 0 : "Negative step";  
        assert view != null : "The view is null";

        // All actors in the list are alive
        for (Actor actor : actors) {
            assert actor.isActive() : "Dead actor in list";
        }
        
        // First check all actors in the list are in the ocean
        // and ocean and location agree.
        for (Actor actor : actors) {
            assert actor.getOcean() == ocean : 
                "An actor has a different ocean: " + 
                actor.getOcean() + " " + ocean;
            assert ocean.getObjectAt(actor.getLocation()) == actor : 
                "actor not at its location in the ocean";
        }
        
        // Then check all actors in the ocean are in the list;
        // Together ensures that both are the same set of actors.
        for(int row = 0; row < ocean.getDepth(); row++) {
            for(int col = 0; col < ocean.getWidth(); col++) {
                Actor actor = ocean.getObjectAt(new Location(row,col));
                if (actor != null) {
                    assert actors.contains(actor) :
                        "List does not contain an actor on the ocean.";
                }
            }
        }
    }
    
    /**
     * Create a simulation ocean with the given size.
     * @param factory A factory for creating the actors.
     * @param view A view for displaying the simulation.
     * @param depth Depth of the ocean. Must be greater than zero.
     * @param width Width of the ocean. Must be greater than zero.
     */
    public Simulator(Factory factory, View view, int depth, int width)
    {
        assert (width > 0 && depth > 0) : 
            "The dimensions are not greater than zero.";
            
        this.factory = factory;
        this.view = view;

        actors = new ArrayList<Actor>();
        ocean = new Ocean(depth, width);
       
        // Setup a valid starting point.
        reset();
        sane();
    }
    
    /**
     * Current value of the step counter.
     */
    public int getStep()
    {
        return step;
    }
    
    /**
     * Getter for the ocean.
     * Would be nicer to expose only a variant of the ocean with few
     * getter methods itself.
     */
    public Ocean getOcean()
    {
        return ocean;
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole ocean updating the state of each
     * actor.
     */
    public void simulateOneStep()
    {
        sane();
        
        step++;

        // Provide space for newborn actors.
        List<Actor> newActors = new ArrayList<Actor>(); 
        
        // Let all actors act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            it.next().act(newActors);
        }
               
        // Add the newly born actors to the main lists.
        actors.addAll(newActors);
        
        // Remove dead actors.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            if(! it.next().isActive()) {
                it.remove();
            }
        }

        // Update the view of the simulation to the new state.
        view.update(this);
        
        sane();
    }
        
    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable()
    {
        return (actors.stream().map((a) -> a.getClass())
            .collect(Collectors.toSet())).size() 
            > 1;
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        sane();
        
        step = 0;
        actors.clear();
        ocean.clear();
        populate();
        
        // Update the view of the simulation to the new state.
        view.update(this);
        
        sane();
    }
    
    /**
     * Randomly populate the ocean with actors.
     * Pre-condition: the ocean is empty
     */
    private void populate()
    {
        sane(); 
        for(int row = 0; row < ocean.getDepth(); row++) {
            for(int col = 0; col < ocean.getWidth(); col++) {
                Location location = new Location(row, col);
                Actor actor = factory.optionallyCreateActor(ocean, location);
                if(actor != null) {
                    actors.add(actor);  
                }
                // else leave the location empty.
            }
        }
        sane();
   }
}