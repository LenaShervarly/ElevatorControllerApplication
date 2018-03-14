package com.tingco.codechallenge.elevator.api;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.tingco.codechallenge.elevator.api.Elevator.Direction;


@Component
public class ElevatorImpl implements Elevator{
	
		private int id;	
		private volatile int currentFloor;
		private volatile boolean isBusy;
		private volatile Direction direction;
		private volatile int addressedFloor;
		private List<Integer> floorsToStopAt;
		
		public ElevatorImpl() {}
		
		public ElevatorImpl(int currentFloor) {
			this.currentFloor = currentFloor;
			isBusy = false;
			direction = Direction.NONE;
			addressedFloor = 0;
			floorsToStopAt = Collections.synchronizedList(new LinkedList<Integer>());
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
				setDirection(Direction.NONE);
				return;
			}
			
			if(floorsToStopAt.size() > 0) {	
	
					setAddressedFloor(floorsToStopAt.get(0));		
					if(currentFloor > addressedFloor) {
							setDirection(Direction.DOWN);
					} else {
							setDirection(Direction.UP);
					}
					
					if(!isBusy)
						setBusy(true);
					
					//Imitating movement of the elevator, in future should be definied based on distase between floors and speed of the elevator
					while(currentFloor != toFloor) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							System.out.println(" InterruptedException was catched  inside moveElevator(), while making a couple of stops " + e);
						}
						if(direction == Direction.UP)
							currentFloor ++;
						else 
							currentFloor --;
					}
	
					synchronized (floorsToStopAt) {
						floorsToStopAt.remove(0);
					}
					currentFloor = addressedFloor;
					
					if(floorsToStopAt.contains(currentFloor)) {
						synchronized (floorsToStopAt) {
							floorsToStopAt.removeIf(e -> e.intValue() == currentFloor);
						}
					}
					
					if(floorsToStopAt.size() > 0) 
						moveElevator(floorsToStopAt.get(0)) ;
			}
		}
		
		@Override
		public int getNumberOfPassedFloorsTill(Direction direction, int toFloor) {
			if(currentFloor == toFloor) 
				return 0;
			
			if(this.direction == direction) {
				if(direction == Direction.UP && currentFloor < toFloor)
					return  toFloor - currentFloor;
					
				else if(direction == Direction.DOWN && currentFloor > toFloor)
					return currentFloor - toFloor;
			}
				return Math.abs(addressedFloor - currentFloor) + Math.abs(addressedFloor-toFloor);
		}
		
		@Override
		public void addFloorToStopAt(Integer floor, Direction direction) {
			if(floor == null)
				return;
			
			int indexOfNewDirection = 0;
			
			if(floorsToStopAt == null)
				floorsToStopAt = Collections.synchronizedList(new LinkedList<Integer>());
			
			if(floorsToStopAt.size() > 0) {	
				if(this.direction == direction) {
					
					if(direction == Direction.UP) {
						if(currentFloor < floor) {
							for(int i = 0; i < floorsToStopAt.size(); i++) {
								if(floorsToStopAt.get(i) < floor) 
									indexOfNewDirection ++;
							}
						} else {
							indexOfNewDirection = floorsToStopAt.size();
							for(int i = floorsToStopAt.size() -1; i >= 0 ; i--) {
								if(floorsToStopAt.get(i) < floor) 
									indexOfNewDirection --;
							}
						}
					} 
					
					if(direction == Direction.DOWN && currentFloor > floor) {
						for(int i = 0; i < floorsToStopAt.size(); i++) {
							if(floorsToStopAt.get(i) > floor) 
								indexOfNewDirection ++;
						}
					} else {
						indexOfNewDirection = floorsToStopAt.size();
						for(int i = floorsToStopAt.size() -1; i >= 0 ; i--) {
							if(floorsToStopAt.get(i) > floor) 
								indexOfNewDirection --;
						}
					}
				} else if (floorsToStopAt.size() > 1) {
					for(int i = 0; i < floorsToStopAt.size()-1; i++) {
						if(this.direction == Direction.DOWN && floorsToStopAt.get(i) - floorsToStopAt.get(i+1)>0
								|| this.direction == Direction.UP && floorsToStopAt.get(i+1) - floorsToStopAt.get(i)>0)
							indexOfNewDirection ++;
						
						if(indexOfNewDirection < floorsToStopAt.size())
							for(int j = indexOfNewDirection; j < floorsToStopAt.size()-1; j++) {
								if(this.direction == Direction.DOWN && floorsToStopAt.get(j) - floorsToStopAt.get(j+1)>0
										|| this.direction == Direction.UP && floorsToStopAt.get(j+1) - floorsToStopAt.get(j)>0)
									indexOfNewDirection ++;
							}
					}
				}
			}
			synchronized (floorsToStopAt) {
				floorsToStopAt.add(indexOfNewDirection, floor);
			}
		}
	
		@Override
		 public List<Integer> getFloorsToStopAt() {
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
