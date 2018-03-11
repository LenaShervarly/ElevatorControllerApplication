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

    void setBusy(boolean isBusy);
    
    void setId(int id);
    
    void setAddressedFloor(int addressedFloor) ;
    
    void setCurrentFloor(int currentFloor);
    
    void setDirection(Direction direction);
    
    void addFloorToStopAt(Integer floor);

	SortedSet<Integer> getFloorsToStopAt();
}
