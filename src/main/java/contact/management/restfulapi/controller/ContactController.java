package contact.management.restfulapi.controller;

import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.ContactResponse;
import contact.management.restfulapi.model.CreateContactRequest;
import contact.management.restfulapi.model.UpdateContactRequest;
import contact.management.restfulapi.model.WebResponse;
import contact.management.restfulapi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.codec.ServerSentEvent.builder;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    // controller contact
    @PostMapping(path = "/api/contacts",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    // get contact by id controller
    @GetMapping(path = "/api/contacts/{contactId}",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> get(User user,@PathVariable("contactId")String id) {
        ContactResponse contactResponse = contactService.get(user,id);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    // Controller update contact by id
    @PutMapping(path = "/api/contacts/{contactId}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> update(User user,
                                               @RequestBody UpdateContactRequest request,
                                               @PathVariable("contactId") String contactId){
        request.setId(contactId);
        ContactResponse contactResponse = contactService.update(user, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    // Controller delete contact by id
    @DeleteMapping(path = "/api/contacts/{contactId}",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(User user, @PathVariable("contactId") String contactId){
        contactService.delete(user,contactId);
        return WebResponse.<String>builder()
                .data("Delete contact success")
                .build();
    }
}