package com.tingco.codechallenge.elevator.api;

import java.util.SortedSet;

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
        UP("up"), DOWN("down"), NONE("none");
        
        private String name;
     
		private Direction(String name) {
        	this.name = name;
        }
		
		   public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
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
     * Adding a floor to the set of all floors, where elevator is requested to stop
     * @param floor requested to stop at
     */
    void addFloorToStopAt(Integer floor);

    /**
     * A snapshot set of all floors evevator is requested to stop on its way.
     * @return SortedSet of all floors, where elevator is requested to stop
     */
	SortedSet<Integer> getFloorsToStopAt();
}
