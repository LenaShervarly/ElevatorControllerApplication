package com.tingco.codechallenge.elevator.api;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Component;


@Component
public class ElevatorImpl implements Elevator{
	
		private int id;	
		private int currentFloor;
		private boolean isBusy;
		private Direction direction;
		private int addressedFloor;
		private SortedSet<Integer> floorsToStopAt;
		
		public ElevatorImpl() {}
		
		public ElevatorImpl(int currentFloor) {
			this.currentFloor = currentFloor;
			isBusy = false;
			direction = Direction.NONE;
			addressedFloor = 0;
			floorsToStopAt = new TreeSet<>();
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
			if(currentFloor == toFloor)
				return;
			
			if(floorsToStopAt.size() > 0) {									
					if(currentFloor > addressedFloor) {
							setAddressedFloor(floorsToStopAt.last());		
							setDirection(Direction.DOWN);
					} else {
							setAddressedFloor(floorsToStopAt.first());					
							setDirection(Direction.UP);
					}
					if(!isBusy)
						setBusy(true);
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					floorsToStopAt.remove(addressedFloor);
					currentFloor = addressedFloor;
					moveElevator(toFloor) ;
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentFloor = toFloor;
		}
		
		@Override
		public void addFloorToStopAt(Integer floor) {
			if(floorsToStopAt == null)
				floorsToStopAt = new TreeSet<>();
			if(floor != null)
				floorsToStopAt.add(floor);
		}
	
		@Override
		 public SortedSet<Integer> getFloorsToStopAt() {
			return floorsToStopAt;
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
	
		@Override
		public void setDirection(Direction direction) {
			this.direction = direction;
		}
	
		@Override
		public void setCurrentFloor(int currentFloor) {
			this.currentFloor = currentFloor;
		}
	
		@Override
		public void setAddressedFloor(int addressedFloor) {
			this.addressedFloor = addressedFloor;
		}
	
		@Override
		public void setId(int id) {
			this.id = id;
		}
	
		@Override
		public void setBusy(boolean isBusy) {
			this.isBusy = isBusy;
		}
}
