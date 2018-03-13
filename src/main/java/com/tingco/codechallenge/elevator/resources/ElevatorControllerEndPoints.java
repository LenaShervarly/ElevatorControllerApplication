package com.tingco.codechallenge.elevator.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.ElevatorImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.Elevator.Direction;
/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@EnableAsync(proxyTargetClass=true)
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {
	
	@Autowired
	@Qualifier("singleController")
    private ElevatorController elevatorController;
    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
    /**
     * Get the list of all connected elevators
     * @return list of all connected elevators
     */
    @GetMapping
    public List<Elevator> getAllElevators(){
        return elevatorController.getElevators();
    }
    
    /**
     * Add a new elevator to the list of all connected elevators
     * @param new elevator to be added
     * @return initialized elevator with all default parameters
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Elevator addElevatorToControl(@RequestBody ElevatorImpl elevator) {
    	return elevatorController.addElevatorToList(elevator); 	  
    }
    
    /**
     * Releasing elevator from all possible tasks  
     * @param elevator to be released
     */
    @PutMapping("/release")
    @ResponseStatus(HttpStatus.OK)
    public void releaseElevator(@RequestBody ElevatorImpl elevator) {
    	elevatorController.releaseElevator(elevator);
    }
    
    /**
     * Simple request option to the specified floor
     * @param toFloor to which the elevator is requested
     * @return confirmation that elevator was sent
     */
    @PutMapping("/request/{toFloor}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String requestElevator(@PathVariable("toFloor") int toFloor) {
    	Elevator requestedElevator = elevatorController.requestElevator(toFloor);
    	return "Elevator is sent to floor " + toFloor; 
    }
    
    /**
     * Request an elevator to the specified floor in the specified direction. Version of request that could be expected 
     * in modern buildings with 2 buttons "Up" and "Down"
     *
     * @param direction indicates the direction in which the elevator is requested
     * @param toFloor,  addressed floor as integer.
     * @return confirmation that elevator was sent
     */
    @PutMapping("/request/{direction}/{toFloor}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String requestElevator(@PathVariable("direction") String direction, @PathVariable("toFloor") int toFloor) {
    	elevatorController.requestElevator(Direction.valueOf(direction.toUpperCase()), toFloor);  	
    	return "Elevator is sent to floor " + toFloor; 
    }
    
    /**
	 * Removing an elevator based on its id
	 * @param id of the elevator to be deleted
	 */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElevatorByid(@PathVariable("id") int id) {
    	elevatorController.deleteElevatorFromControlById(id);
    }
}
