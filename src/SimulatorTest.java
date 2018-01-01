import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class SimulatorTest.
 * 
 * None of the tests contains any assertion. Hence none can finish with a failed assertion.
 * However, they run repeatable executions of the simulations. Thus the numerous assertion
 * statements in program for pre- and post-conditions and invariants are being exercised.
 *
 * @author  Olaf Chitil
 * @version 2016/02/25
 */
public class SimulatorTest
{
    /**
     * Default constructor for test class SimulatorTest
     */
    public SimulatorTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void tinySimulation()
    {
        Simulator simulato1 = new Simulator(null, new MockView(), 1, 1);
        simulato1.simulateOneStep();
        simulato1.simulateOneStep();
    }

    @Test
    public void smallSimulation()
    {
        Simulator simulato1 = new Simulator(null, new MockView(), 10, 10);
        simulato1.reset();
        simulato1.simulateOneStep();
        simulato1.reset();
        simulato1.simulateOneStep();
        simulato1.simulateOneStep();
    }
}



