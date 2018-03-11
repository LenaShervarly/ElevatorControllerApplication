package com.tingco.codechallenge.elevator.api;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

@Service
public class ElevatorImpl implements Elevator{
	
		private Direction direction;
		private int currentFloor;
		private int addressedFloor;
		private int id;
		private boolean isBusy;
		private SortedSet<Integer> floorsToStopAt = new TreeSet<>();
		
		public ElevatorImpl() {}
		
		public ElevatorImpl(int id, int currentFloor, int addressedFloor, boolean isBusy) {
			this.id = id;
			this.currentFloor = currentFloor;
			this.addressedFloor = addressedFloor;
			this.isBusy = isBusy;
			direction = Direction.NONE;
		}
		/**
	     * Tells which direction is the elevator going in.
	     *
	     * @return Direction Enumeration value describing the direction.
	     */
		@Override
		public Direction getDirection() {
			return direction;
		}
	
		/**
	     * If the elevator is moving. This is the target floor.
	     *
	     * @return primitive integer number of floor
	     */
		@Override
		public int getAddressedFloor() {
			return addressedFloor;
		}
	
		/**
	     * Get the Id of this elevator.
	     *
	     * @return primitive integer representing the elevator.
	     */
		@Override
		public int getId() {
			return id;
		}
	
	    /**
	     * Command to move the elevator to the given floor.
	     *
	     * @param toFloor
	     *            int where to go.
	     */
		@Override
		public void moveElevator(int toFloor) {
				if(!isBusy()) {
						setAddressedFloor(toFloor);
						if(currentFloor == toFloor)
								return;
						if(currentFloor > toFloor)
								setDirection(Direction.UP);
						else
								setDirection(Direction.DOWN);
						setBusy(true);
				}	
		}
		
		public void addFloorToStopAt(int floor) {
			floorsToStopAt.add(floor);
		}
	
		 /**
	     * Check if the elevator is occupied at the moment.
	     *
	     * @return true if busy.
	     */
		@Override
		public boolean isBusy() {
			return isBusy;
		}
	
		/**
	     * Reports which floor the elevator is at right now.
	     *
	     * @return int actual floor at the moment.
	     */
		@Override
		public int currentFloor() {
			return currentFloor;
		}
	
		public void setDirection(Direction direction) {
			this.direction = direction;
		}
	
		public void setCurrentFloor(int currentFloor) {
			this.currentFloor = currentFloor;
		}
	
		public void setAddressedFloor(int addressedFloor) {
			this.addressedFloor = addressedFloor;
		}
	
		public void setId(int id) {
			this.id = id;
		}
	
		public void setBusy(boolean isBusy) {
			this.isBusy = isBusy;
		}
}
