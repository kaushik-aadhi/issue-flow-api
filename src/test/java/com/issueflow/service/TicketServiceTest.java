package com.issueflow.service;
import com.issueflow.domain.Priority;import com.issueflow.dto.TicketDtos.CreateTicketRequest;import com.issueflow.entity.AppUser;import com.issueflow.repository.*;import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.*;import org.mockito.junit.jupiter.MockitoExtension;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
 @Mock TicketRepository tickets;@Mock UserRepository users;
 @InjectMocks TicketService service;
 @Test void createPersistsTicket(){AppUser user=new AppUser("Test User","test@example.com","hash");when(users.findByEmailIgnoreCase("test@example.com")).thenReturn(java.util.Optional.of(user));when(tickets.save(any())).thenAnswer(i->i.getArgument(0));var result=service.create("test@example.com",false,new CreateTicketRequest("Printer issue","Printer is offline",Priority.HIGH,null));assertEquals("Printer issue",result.title());assertEquals(Priority.HIGH,result.priority());verify(tickets).save(any());}
}
