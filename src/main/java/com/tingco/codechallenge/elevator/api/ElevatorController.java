package com.tingco.codechallenge.elevator.api;

import java.util.List;

import com.tingco.codechallenge.elevator.api.Elevator.Direction;


/**
 * Interface for the Elevator Controller.
 *
 * @author Sven Wesley
 *
 */
public interface ElevatorController {

    /**
     * Request an elevator to the specified floor.
     *
     * @param toFloor
     *            addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     */
    Elevator requestElevator(int toFloor);
    
    /**
     * Request an elevator to the specified floor in the specified direction. Version of request that could be expected 
     * in modern buildings with 2 buttons "Up" and "Down"
     *
     * @param direction indicates the direction in which the elevator is requested
     * @param toFloor
     *            addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     */
    Elevator requestElevator(Direction direction, int toFloor);

    /**
     * A snapshot list of all elevators in the system.
     *
     * @return A List with all {@link Elevator} objects.
     */
    List<Elevator> getElevators();

    /**
     * Telling the controller that the given elevator is free for new
     * operations.
     *
     * @param elevator
     *            the elevator that shall be released.
     */
    void releaseElevator(Elevator elevator);

    /**
     *  Adding an elevator to the list of elevators under control of the elevator controller
     * @param elevator to be added to control list
     */
    Elevator addElevatorToList(Elevator elevator);

	/**
	 * Removing an elevator from the control of the elevator controller by providing id 
	 * @param id of the elevator to be deleted
	 */
	void deleteElevatorFromControlById(int id);

}
