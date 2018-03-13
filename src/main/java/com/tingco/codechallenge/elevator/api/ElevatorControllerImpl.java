package com.tingco.codechallenge.elevator.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import com.tingco.codechallenge.elevator.api.Elevator.Direction;


@Controller
@Scope("singleton")
@Qualifier("singleController")
public class ElevatorControllerImpl implements ElevatorController{
	
	private static List<Elevator> elevators = new ArrayList<>();	
	private static int lastAddedElevatorId = 0;

	@Override
	@Async
	public Elevator requestElevator(int toFloor) {
		Elevator requestedElevator = findClosestFreeElevator(toFloor);

		moveElevator(toFloor, requestedElevator);		
		return requestedElevator;
	}

	@Override
	@Async
	public Elevator requestElevator(Direction requestedDirection, int toFloor) {
		Elevator requestedElevator = findClosestFreeElevator(toFloor);
		
		if(requestedElevator == null)		
				requestedElevator = findClosestMovingElevator(toFloor, requestedDirection);
		
		if(requestedElevator == null) {
			//wait a little bit and try sending a request again
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("request was interupted");
			}
			requestElevator(requestedDirection, toFloor);
		}
		
		moveElevator(toFloor, requestedElevator);
	
		return requestedElevator;
	}
	
	/**
	 * Sending the requested elevator to the specified floor
	 * @param toFloor specified floor to move to
	 * @param requestedElevator elevator that should be moved
	 */
	private void moveElevator(int toFloor, Elevator requestedElevator) {
		if(requestedElevator != null) 	{
			requestedElevator.addFloorToStopAt(Integer.valueOf(toFloor));
			requestedElevator.moveElevator(toFloor);
		}							
	}

	/**
	 * Find the specific elevator on the base of its id
	 * @param id of the searched elevator 
	 * @return a specific elevator is there is one and only one with the specified id
	 */
	private Elevator findElevatorById(int id) {
		return elevators.stream()
				.filter(e -> e.getId() == id)
				.reduce((e1, e2) -> {
		            throw new IllegalStateException("Multiple elements: " + e1 + ", " + e2);
		        })
		        .get();
	}

	/**
	 * Find the closest elevator that is moving on the specified direction and can add toFloor to the list of its stops
	 * @param toFloor specified floor to move to
	 * @param direction specified direction to move in
	 * @return  the closest elevator that is moving on the specified direction and can add toFloor to the list of its stops
	 */
	private Elevator findClosestMovingElevator(int toFloor, Direction direction) {
		return elevators.stream()
				.filter(e -> {
					if(e.getDirection() == direction) {
						if(direction == Direction.UP)
							return e.currentFloor() < toFloor;
						else
							return e.currentFloor() > toFloor;
					} else
						return false;						
				})
				.min(closest(toFloor)).orElse(null);
	}
	
	/**
	 * Find the closest free elevator to the specified floor
	 * @param toFloor specified floor to move to
	 * @return the closest free elevator to the specified floor
	 */
	private Elevator findClosestFreeElevator(int toFloor) {
		return elevators.stream()
				.filter(e -> e.isBusy() == false)
				.min(closest(toFloor)).orElse(null);
	}

	/**
	 * Customized comparator in order to find the closest elevator to the specified floor
	 * @param toFloor specified floor to compare with
	 * @return a comparator that can be used to find the elevator closest to the specified floor
	 */
	private Comparator<Elevator> closest(int toFloor) {
		return new Comparator<Elevator>() {
			@Override
			public int compare(Elevator e1, Elevator e2) {
					return Math.abs(e1.currentFloor() - toFloor) - Math.abs(e2.currentFloor() - toFloor);
			}
		};
	}
	
	@Override
	public List<Elevator> getElevators() {
		return elevators;
	}


	@Override
	@Async
	public void releaseElevator(Elevator elevator) {
		Elevator elevatorToRelease = findElevatorById(elevator.getId());
		elevatorToRelease.setBusy(false);
		elevatorToRelease.setDirection(Direction.NONE);
	}
	
	@Override
	public Elevator addElevatorToList(Elevator elevator) {
		elevator.setId(lastAddedElevatorId++);
		elevators.add(elevator);
		return elevator;
	}
	
	@Override
	public void deleteElevatorFromControlById(int id) {
		elevators.removeIf(e -> (e.getId() == id));
	}

}
