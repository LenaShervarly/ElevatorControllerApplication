package com.tingco.codechallenge.elevator.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tingco.codechallenge.elevator.api.Elevator.Direction;

@Component
@Scope("singleton")
public class ElevatorControllerImpl implements ElevatorController{
	
	//private ElevatorController elevatorController = new ElevatorControllerImpl();
	private static List<Elevator> elevators = new ArrayList<>();	
	private static HashMap<Integer, Elevator> elevatorsMap = new HashMap<>();	
	private Queue<Integer> waitingQueue = new SynchronousQueue<>();
	private int waitingFloor = 0;

	//public ElevatorControllerImpl() {}
	
	static {
		elevatorsMap.put(1, new ElevatorImpl(1, 0, 5, false));
		elevatorsMap.put(2, new ElevatorImpl(2, 6, 2,  false));
		elevatorsMap.put(3, new ElevatorImpl(3, 3, 4, false));
		elevators.addAll(elevatorsMap.values());
		//elevators.g
	}
	
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
			
			requestedElevetor.addFloorToStopAt(toFloor);
		}
		
		if(requestedElevetor != null) 	
			requestedElevetor.moveElevator(toFloor);
		
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
				.min(new Comparator<Elevator>() {
			@Override
			public int compare(Elevator e1, Elevator e2) {
				return Math.abs(e1.currentFloor() - toFloor) - Math.abs(e2.currentFloor() - toFloor);
			}
		}).get();
	}
	
	private Elevator findClosestFreeElevator(int toFloor) {
		return elevators.stream()
				.filter(e -> e.isBusy() == false)
				.min(new Comparator<Elevator>() {
			@Override
			public int compare(Elevator e1, Elevator e2) {
				return Math.abs(e1.currentFloor() - toFloor) - Math.abs(e2.currentFloor() - toFloor);
			}
		}).orElse(null);
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
		elevators.add(elevator);
	}
	
	public void deleteElevatorFromControlById(int id) {
		elevators.removeIf(e -> (e.getId() == id));
	}

	public int getRequestedFloor() {
		return waitingFloor;
	}

	public void setRequestedFloor(int requestedFloor) {
		this.waitingFloor = requestedFloor;
	}
}
