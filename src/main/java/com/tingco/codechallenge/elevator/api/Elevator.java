package com.tingco.codechallenge.elevator.api;

import java.util.List;


/**
 * Interface for an elevator object.
 *
 * @author Sven Wesley
 *
 */
public interface Elevator {

    /**
     * Enumeration for describing elevator's direction.
     */
    enum Direction {
        UP, DOWN, NONE;
    }

    /**
     * Tells which direction is the elevator going in.
     *
     * @return Direction Enumeration value describing the direction.
     */
    Direction getDirection();

    /**
     * If the elevator is moving. This is the target floor.
     *
     * @return primitive integer number of floor
     */
    int getAddressedFloor();

    /**
     * Get the Id of this elevator.
     *
     * @return primitive integer representing the elevator.
     */
    int getId();

    /**
     * Command to move the elevator to the given floor.
     *
     * @param toFloor
     *            int where to go.
     */
    void moveElevator(int toFloor);

    /**
     * Check if the elevator is occupied at the moment.
     *
     * @return true if busy.
     */
    boolean isBusy();

    /**
     * Reports which floor the elevator is at right now.
     *
     * @return int actual floor at the moment.
     */
    int currentFloor();
    
    /**
     * Set that the elevator is occupied at the moment.
     * @param isBusy, true if busy.
     */
    void setBusy(boolean isBusy);
    
    /**
     * Set the Id of this elevator.
     * @param primitive integer id number of the elevator
     */
    void setId(int id);
    
    /**
     * Set the target floor of the elevator.
     *
     * @param primitive integer number of the addressedFloor
     */
    void setAddressedFloor(int addressedFloor) ;
    
    /**
     * Set which floor the elevator is at right now.
     * @param primitive integer number of the currentFloor
     */
    void setCurrentFloor(int currentFloor);
    
    /**
     * Set the direction, the elevator going in.
     *
     * @param direction, enumeration value describing the direction.
     */
    void setDirection(Direction direction);
    
    /**
     * Adding a floor to the set of all floors, where elevator is requested to stop. There are a couple of main rules for movement of the elevator:
     * 1) if the elevator is going to pass through the requested floor, it will add request to the first third of the list 
     * 2) if the elevator is requested in the opposite direction than it's request is placed in the second third of the list
     * 3)if the elevator is requested in the same direction, but has already passed the floor, than it's added to the last third of the list 
     * All 3 parts of the list are sorted  in ascending or descending order depending on movement of the elevator 
     * 
     * @param floor requested to stop at
     * @param direction at which elevator is requested to 
     */
    void addFloorToStopAt(Integer floor, Direction direction);

    /**
     * A snapshot set of all floors evevator is requested to stop on its way.
     * @return SortedSet of all floors, where elevator is requested to stop
     */
	List<Integer> getFloorsToStopAt();

	/**
	 * Calculating distanse an elevator should pass until the destination
	 * @param requestedDirection destination in which request is made
	 * @param toFloor destination floor of the request
	 * @return the minimum number of floors on the way to the requested floor
	 */
	int getNumberOfPassedFloorsTill(Direction requestedDirection, int toFloor);

}
