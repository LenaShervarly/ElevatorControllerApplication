package com.tingco.codechallenge.elevator;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorImpl;
import com.tingco.codechallenge.elevator.config.ElevatorApplication;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElevatorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
	
	@LocalServerPort
	private int port;
	
	RestTemplate restTemplate = new RestTemplate();

	HttpHeaders headers = new HttpHeaders();

    @Test
    public void simulateAnElevatorShaft() {
    	addElevator(new ElevatorImpl(0));
    	addElevator(new ElevatorImpl(0));
    	addElevator(new ElevatorImpl(0));
		
    	checkElevators();
    	
    	requestElevator();
    }
    
    private void addElevator(ElevatorImpl elevator) {

		HttpEntity<ElevatorImpl> entity = new HttpEntity<ElevatorImpl>(elevator, headers);

		ResponseEntity<ElevatorImpl> response = restTemplate.exchange(
				createURLWithPort("/rest/v1/add"),
				HttpMethod.POST, entity, ElevatorImpl.class);
    }
    
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
    
    @Test
    public void checkElevators() {
    	HttpEntity<List<ElevatorImpl>> entity = new HttpEntity<List<ElevatorImpl>>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/rest/v1/"),
				HttpMethod.GET, entity, String.class);

		String expected = "[{\"id\":0,\"direction\":\"NONE\",\"addressedFloor\":0,\"floorsToStopAt\":[],\"busy\":false},"
				+ "{\"id\":1,\"direction\":\"NONE\",\"addressedFloor\":0,\"floorsToStopAt\":[],\"busy\":false},"
				+ "{\"id\":2,\"direction\":\"NONE\",\"addressedFloor\":0,\"floorsToStopAt\":[],\"busy\":false}]";

		JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    
    @Test
    public void requestElevator() {
    	ElevatorImpl elevator = new ElevatorImpl();
    	HttpEntity<ElevatorImpl> entity = new HttpEntity<ElevatorImpl>(elevator, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/rest/v1/request/{direction}/{toFloor}"),
				HttpMethod.PUT, entity, String.class, "UP", 6);
    
		String expected = "Elevator is sent to floor 6";

		Assert.assertEquals(expected, response.getBody());
    }

}
