package com.tingco.codechallenge.elevator.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tingco.codechallenge.elevator.api.ElevatorControllerImpl;
import com.tingco.codechallenge.elevator.api.ElevatorImpl;

import org.springframework.http.MediaType;
import com.tingco.codechallenge.elevator.api.Elevator;
/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {
	
	@Autowired
    private ElevatorControllerImpl elevatorController;
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
    public void addElevatorToControl(@RequestBody ElevatorImpl elevator) {
    	 elevatorController.addElevatorToControl(elevator);
    }
    
    @PutMapping("/release")
    public @ResponseBody String releaseElevator(@RequestBody ElevatorImpl elevator) {
    	elevatorController.releaseElevator(elevator);
    	return "Released elevator #" + elevator.getId();    
    }
    
    @PutMapping("/request/{fromFloor}/{toFloor}")
    public @ResponseBody String requestElevator(@PathVariable("fromFloor") int fromFloor, @PathVariable("toFloor") int toFloor) {
    	elevatorController.setWaitingFloor(fromFloor);
    	Elevator requestedElevator = elevatorController.requestElevator(toFloor);
    	return String.format("Elevator #%d sent to floor %d", requestedElevator.getId(), toFloor) + requestedElevator.getFloorsToStopAt();
    }
    
    @DeleteMapping("/delete/{id}")
    public @ResponseBody String deleteElevatorByid(@PathVariable("id") int id) {
    	elevatorController.deleteElevatorFromControlById(id);
    	return "Deleted elevator #" + id;    	
    }
}
