package com.tingco.codechallenge.elevator.api;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Component;


@Component
public class ElevatorImpl implements Elevator{
	
		private int id;	
		private volatile int currentFloor;
		private volatile boolean isBusy;
		private volatile Direction direction;
		private volatile int addressedFloor;
		private SortedSet<Integer> floorsToStopAt;
		
		public ElevatorImpl() {}
		
		public ElevatorImpl(int currentFloor) {
			this.currentFloor = currentFloor;
			isBusy = false;
			direction = Direction.NONE;
			addressedFloor = 0;
			floorsToStopAt = Collections.synchronizedSortedSet(new TreeSet<Integer>());
		}
		

		@Override
		public Direction getDirection() {
			return direction;
		}
	

		@Override
		public int getAddressedFloor() {
			return addressedFloor;
		}
	

		@Override
		public int getId() {
			return id;
		}
	

		@Override
		public void moveElevator(int toFloor) {
			if(currentFloor == toFloor) {
				setBusy(false);
				return;
			}
			
			addressedFloor = toFloor;
			
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
					
					//Imitating movement of the elevator, in future should be definied based on distase between floors and speed of the elevator
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						System.out.println(" InterruptedException was catched  inside moveElevator(), while making a couple of stops " + e);
					}
					
					floorsToStopAt.remove(addressedFloor);
					currentFloor = addressedFloor;
					moveElevator(toFloor) ;
			}
			currentFloor = toFloor;
		}
		
		@Override
		public void addFloorToStopAt(Integer floor) {
			if(floorsToStopAt == null)
				floorsToStopAt = Collections.synchronizedSortedSet(new TreeSet<Integer>());
			if(floor != null)
				floorsToStopAt.add(floor);
		}
	
		@Override
		 public SortedSet<Integer> getFloorsToStopAt() {
			return floorsToStopAt;
		}

		
		@Override
		public boolean isBusy() {
			return isBusy;
		}
	
		
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
