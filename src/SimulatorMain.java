
/**
 * Main class for any simulation.
 * Constructing an object runs the whole simulation.
 * 
 * @author Olaf Chitil
 * @version 2017/02/22
 */
public class SimulatorMain
{
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 100;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 100;
    // Number of step for long-running simulation
    private static final int LONG_STEPS = 500;

    private Simulator simulator;
    private SimulatorView view;
    
    public SimulatorMain(Factory factory)
    {
        this(factory, DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param factory A factory for creating the actors.
     * @param depth Depth of the ocean. Must be greater than zero.
     * @param width Width of the ocean. Must be greater than zero.
     */
    public SimulatorMain(Factory factory, int depth, int width)
    {
        assert (width > 0 && depth > 0) : 
            "The dimensions are not greater than zero.";

        view = new SimulatorView(depth, width);
        factory.setupColors(view);
        simulator = new Simulator(factory, view, depth, width);
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period.
     */
    public void runLongSimulation()
    {
        simulate(LONG_STEPS);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && simulator.isViable(); step++) {
            simulator.simulateOneStep();
        }
    }

}
