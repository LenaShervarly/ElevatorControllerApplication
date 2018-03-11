package com.tingco.codechallenge.elevator.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tingco.codechallenge.elevator.api.Elevator.Direction;

@Controller
@Scope("singleton")
public class ElevatorControllerImpl implements ElevatorController{
	
	private static List<Elevator> elevators = new ArrayList<>();	
	private int waitingFloor = 0;


	/**
     * Request an elevator to the specified floor.
     *
     * @param toFloor
     *            addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     */
	@Override
	public Elevator requestElevator(int toFloor) {
		Elevator requestedElevetor = findClosestFreeElevator(toFloor);
		
		if(requestedElevetor == null) {		
			if(waitingFloor > toFloor)
				requestedElevetor = findClosestMovingElevator(toFloor, Direction.DOWN);
			else
				requestedElevetor = findClosestMovingElevator(toFloor, Direction.UP);
		}
		
		if(requestedElevetor != null) 	{
			requestedElevetor.addFloorToStopAt(Integer.valueOf(toFloor));
			requestedElevetor.moveElevator(toFloor);
		}
					
		elevators.set(requestedElevetor.getId(), requestedElevetor);
		return requestedElevetor;
	}
	
	private Elevator findElevatorById(int id) {
		return elevators.stream()
				.filter(e -> e.getId() == id)
				.reduce((e1, e2) -> {
		            throw new IllegalStateException("Multiple elements: " + e1 + ", " + e2);
		        })
		        .get();
	}

	private Elevator findClosestMovingElevator(int toFloor, Direction direction) {
		return elevators.stream()
				.filter(e -> e.getDirection() == direction)
				.min(closest(toFloor)).get();
	}
	
	private Elevator findClosestFreeElevator(int toFloor) {
		return elevators.stream()
				.filter(e -> e.isBusy() == false)
				.min(closest(toFloor)).orElse(null);
	}

	private Comparator<Elevator> closest(int toFloor) {
		return new Comparator<Elevator>() {
			@Override
			public int compare(Elevator e1, Elevator e2) {
					return Math.abs(e1.currentFloor() - toFloor) - Math.abs(e2.currentFloor() - toFloor);
			}
		};
	}
	/**
     * A snapshot list of all elevators in the system.
     *
     * @return A List with all {@link Elevator} objects.
     */
	@Override
	public List<Elevator> getElevators() {
		return elevators;
	}

	/**
     * Telling the controller that the given elevator is free for new
     * operations.
     *
     * @param elevator
     *            the elevator that shall be released.
     */
	@Override
	public void releaseElevator(Elevator elevator) {
		Elevator elevatorToRelease = findElevatorById(elevator.getId());
		elevatorToRelease.setBusy(false);
		elevatorToRelease.setDirection(Direction.NONE);
		elevators.set(elevator.getId(), elevatorToRelease);
	}
	
	public void addElevatorToControl(Elevator elevator) {
		int newId = elevators.size();
		elevator.setId(newId);
		elevators.add(elevator);
	}
	
	public void deleteElevatorFromControlById(int id) {
		elevators.removeIf(e -> (e.getId() == id));
	}

	public int getWaitingFloor() {
		return waitingFloor;
	}

	public void setWaitingFloor(int requestedFloor) {
		this.waitingFloor = requestedFloor;
	}
}
