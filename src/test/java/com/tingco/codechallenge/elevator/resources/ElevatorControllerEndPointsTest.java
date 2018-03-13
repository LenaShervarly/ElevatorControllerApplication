package com.tingco.codechallenge.elevator.resources;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
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
@WebMvcTest(value = ElevatorControllerEndPoints.class, secure = false)
public class ElevatorControllerEndPointsTest {

    @Autowired
    private ElevatorControllerEndPoints endPoints;
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ElevatorControllerImpl elevatorControllerImpl;

    private Elevator mockElevator = new ElevatorImpl(0);
    private String busyElevatorJson = "{ \"currentFloor\": 0,  \"id\": 0, \"direction\": \"UP\", \"addressedFloor\": 5, "
			+ "\"busy\": true }";
    private String newElevatorJson = "{ \"currentFloor\": 0 }";
    private int toFloor = 5;
    
    @Test
    public void ping() throws Exception {
        Assert.assertEquals("pong", endPoints.ping());
    }
    
    @Test
    public void getAllElevators() throws Exception {
    	List<Elevator> elevators =  new ArrayList<>();

    	String expected = "[]";
    	
    	Mockito.when(elevatorControllerImpl.getElevators()).thenReturn(elevators);
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/v1/");
    	
    	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    	
    	MockHttpServletResponse response = result.getResponse();
    	
    	JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);

        Assert.assertEquals(expected, response.getContentAsString());
    }

    @Test
    public void addElevatorToControl() throws Exception {
    	
    	Mockito.when(elevatorControllerImpl.addElevatorToList(Mockito.any(Elevator.class))).thenReturn(mockElevator);
    	
    	RequestBuilder requestBuilderAdd = MockMvcRequestBuilders
				.post("/rest/v1/add")
				.accept(MediaType.APPLICATION_JSON).content(newElevatorJson)
				.contentType(MediaType.APPLICATION_JSON);
    	
    	MvcResult result =  mockMvc.perform(requestBuilderAdd).andReturn();

    	MockHttpServletResponse response = result.getResponse();
    	Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
   }
    
    @Test
    public void releaseElevator() throws Exception {

    	Mockito.doNothing().when(elevatorControllerImpl).releaseElevator(Mockito.any(ElevatorImpl.class)); 
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
    			.put("/rest/v1/release")
    			.accept(MediaType.APPLICATION_JSON).content(busyElevatorJson)
				.contentType(MediaType.APPLICATION_JSON);
    	
    	MvcResult result =  mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();
    	
    	Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
   }
    
    @Test
    public void requestElevatorUp() throws Exception {
    	
    	String expectedResponse = "Elevator is sent to floor " + toFloor; 
    	
    	Mockito.when(elevatorControllerImpl.requestElevator(Mockito.any(Direction.class), Mockito.anyInt())).thenReturn(mockElevator);
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/rest/v1/request/up/" + toFloor);
    	
    	MvcResult result =  mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	Assert.assertEquals(expectedResponse, response.getContentAsString());
    }
    
    @Test
    public void requestSimpleElevator() throws Exception {
    	
    	Mockito.when(elevatorControllerImpl.requestElevator(Mockito.anyInt())).thenReturn(mockElevator);
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/rest/v1/request/" + toFloor);
    	
    	MvcResult result =  mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    
    @Test
    public void deleteElevatorByid() throws Exception {
    	int id = 0;

    	Mockito.doNothing().when(elevatorControllerImpl).deleteElevatorFromControlById(Mockito.anyInt()); 
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/v1/delete/" + id);
    	
    	MvcResult result =  mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();
    	
    	Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
