import java.util.Iterator;
import java.util.List;

/**
 * A class representing the animal known as Honey Badger These tiny creatures
 * will eat literally anything nearby. They also don't get hungry or get eaten
 * by other animals They exist purely because god forgot to add downsides to
 * being a honey badger other than they don't live as long as the foxes
 * 
 * @author Kenny Castro-Monroy
 * @version 2021.04.18
 */
public class HoneyBadger extends Animal {
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a fox can live.
    private static final int MAX_AGE = 75;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 15;

    /**
     * Constructor for objects of class HoneyBadger
     */
    public HoneyBadger(Field field, Location location) {
        super(field, location);
    }

    /**
     * This is what honey badgers do, they just wreck everything because god tried
     * to tell them to be kind and they bit him.
     * 
     * @param field    The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Animal> newFoxes) {
        incrementAge();
        if (isAlive()) {
            giveBirth(newFoxes);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    private void giveBirth(List<Animal> newFoxes) {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            HoneyBadger young = new HoneyBadger(field, loc);
            newFoxes.add(young);
        }
    }

    /**
     * Look for any animal adjacent to the current location. Only the first live
     * animal is eaten.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Animal animal = (Animal) field.getObjectAt(where);
            if (animal != null) {
                if (animal.isAlive()) {
                    animal.setDead();
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge() {
        int age = getAge();
        setAge(++age);
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Returns the breeding age of the animal
     * 
     * @return the breeding age
     */
    @Override
    public int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Returns the chance for the animal to breed
     * 
     * @return the chance for the animal to breed
     */
    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Returns the maximum size for a litter
     * 
     * @return the maximum size for a litter
     */
    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

}
