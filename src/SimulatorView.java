import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes, Michael Kolling, Olaf Chitil and Gursimran Khalsa
 * @version 07/03/2017
 */
public class SimulatorView extends JFrame implements View
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private GenerateOcean oceanView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private OceanStats stats;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new OceanStats();
        colors = new LinkedHashMap<Class, Color>();

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);
        
        oceanView = new GenerateOcean(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(oceanView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the simulation.
     */
    public void update(Simulator simulator)
    {
        showStatus(simulator.getStep(), simulator.getOcean());
    }
   
    /**
     * Show the current status of the ocean.
     * @param step Which iteration step it is.
     * @param ocean The ocean whose status is to be displayed.
     */
    private void showStatus(int step, OceanView ocean)
    {
        if(!isVisible()) {
            setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        
        oceanView.preparePaint();

        for(int row = 0; row < ocean.getDepth(); row++) {
            for(int col = 0; col < ocean.getWidth(); col++) {
                Object animal = ocean.getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    oceanView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    oceanView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();
        
        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(ocean));
        oceanView.repaint();
    }

    /**
     * Provide a graphical view of a rectangular ocean. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the ocean.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class GenerateOcean extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image oceanImage;

        /**
         * Create a new oceanView component.
         */
        public GenerateOcean(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                oceanImage = oceanView.createImage(size.width, size.height);
                g = oceanImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this ocean in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The ocean view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(oceanImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(oceanImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(oceanImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
