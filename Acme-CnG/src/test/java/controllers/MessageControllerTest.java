package controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.MessageService;
import domain.Message;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
//Test donde se prueba el método findMyMessages en MessageController
//Se comprueba que la vista message/list es creada correctamente
//.andExpect(forwardedUrl("/views/message/list.jsp"))
public class MessageControllerTest {
	
	
	 	@Mock
	 	private MessageService messageService;
	 
	    @InjectMocks
	    private MessageController messageController;
	 
	    private MockMvc mockMvc;
	    
	    @Before
	    public void setup() {
	        MockitoAnnotations.initMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
	    }
	    
	    @Test
	    public void testFindMyMessages() throws Exception {
	    	
	    	Message ms1 = new Message();
	    	Message ms2 = new Message();
	    	when(messageService.findMyMessages()).thenReturn(Arrays.asList(ms1, ms2));
	 
	        mockMvc.perform(get("/message/list"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("message/list"))
	                .andExpect(model().attribute("ms", hasSize(2)));
	    }
}
