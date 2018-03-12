package com.tingco.codechallenge.elevator.resources;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.Elevator.Direction;
import com.tingco.codechallenge.elevator.api.ElevatorControllerImpl;
import com.tingco.codechallenge.elevator.api.ElevatorImpl;
import com.tingco.codechallenge.elevator.config.ElevatorApplication;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ElevatorApplication.class)
//@SuppressWarnings("deprecation")
//@SpringApplicationConfiguration(classes = ElevatorApplication.class)
@WebMvcTest(value = ElevatorControllerEndPoints.class, secure = false)
public class ElevatorControllerEndPointsTest {

    @Autowired
    private ElevatorControllerEndPoints endPoints;
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ElevatorControllerImpl elevatorControllerImpl;

//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(endPoints).build();
//    }
    
    @Test
    public void ping() throws Exception {
        Assert.assertEquals("pong", endPoints.ping());
    }
    
    @Test
    public void getAllElevators() {
    	List<Elevator> elevators =  new ArrayList<>();
        Assert.assertEquals(elevators, endPoints.getAllElevators());
    }

    @Test
    public void addElevatorToControl() throws Exception {
    	ElevatorImpl mockElevator = new ElevatorImpl(0);
    	String exampleElevatorJson = "{ \"currentFloor\": 0 }";
    	Mockito.when(elevatorControllerImpl.addElevatorToControl(Mockito.any(Elevator.class))).thenReturn(mockElevator);
    	RequestBuilder requestBuilderAdd = MockMvcRequestBuilders
				.post("/rest/v1/add")
				.accept(MediaType.APPLICATION_JSON).content(exampleElevatorJson)
				.contentType(MediaType.APPLICATION_JSON);
    	
    	MvcResult result =  mockMvc.perform(requestBuilderAdd).andReturn();
    	System.out.println("result:" + result.getResponse().getContentAsString());
    	MockHttpServletResponse response = result.getResponse();
    	Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
   }
    
    @Test
    public void releaseElevator() {
    	ElevatorImpl elevator = new ElevatorImpl();
    	endPoints.releaseElevator(elevator);
   }
    
    @Test
    public void requestElevatorUp() {
    	String direction = "up";
    	int toFloor = 5;
    	String response = endPoints.requestElevator(direction, toFloor);
    }
    
    @Test
    public void requestElevatorDown() {
    	String direction = "down";
    	int toFloor = 0;
    	String response = endPoints.requestElevator(direction, toFloor);
    }
    
    @Test
    public void deleteElevatorByid() {
    	int id = 0;
    	endPoints.deleteElevatorByid(id);
    }
}
