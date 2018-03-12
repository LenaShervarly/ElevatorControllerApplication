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
import org.springframework.http.ResponseEntity;
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
    
    @GetMapping
    public List<Elevator> getAllElevators(){
        return elevatorController.getElevators();
    }
    
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Elevator addElevatorToControl(@RequestBody ElevatorImpl elevator) {
    	return elevatorController.addElevatorToControl(elevator); 	  
    }
    
    @PutMapping("/release")
    public @ResponseBody String releaseElevator(@RequestBody ElevatorImpl elevator) {
    	elevatorController.releaseElevator(elevator);
    	return "Released elevator #" + elevator.getId();    
    }
    
    @PutMapping("/request/{toFloor}")
    public @ResponseBody String requestElevator(@PathVariable("toFloor") int toFloor) {
    	Elevator requestedElevator = elevatorController.requestElevator(toFloor);
    	return String.format("Elevator #%d sent to floor %d", requestedElevator.getId(), toFloor);
    }
    
    @PutMapping("/request/{direction}/{toFloor}")
    public @ResponseBody String requestElevator(@PathVariable("direction") String direction, @PathVariable("toFloor") int toFloor) {
    	Elevator requestedElevator = elevatorController.requestElevator(Direction.valueOf(direction.toUpperCase()), toFloor);
    	if(requestedElevator == null)
    		return "elevator is null";
    	else if(requestedElevator.getId()>=0)
    		return "id is " + requestedElevator.getId();
    	else
    		return "Elevator is sent to floor " + toFloor; // + requestedElevator.getId();//String.format("Elevator #%d sent to floor %d", requestedElevator.getId(), toFloor) + requestedElevator.getFloorsToStopAt();
    }
    
    @DeleteMapping("/delete/{id}")
    public @ResponseBody String deleteElevatorByid(@PathVariable("id") int id) {
    	elevatorController.deleteElevatorFromControlById(id);
    	return "Deleted elevator #" + id;    	
    }
}
