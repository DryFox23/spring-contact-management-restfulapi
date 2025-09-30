package contact.management.restfulapi.controller;

import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.AddressResponse;
import contact.management.restfulapi.model.CreateAddressRequest;
import contact.management.restfulapi.model.WebResponse;
import contact.management.restfulapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(path = "/api/contacts/{contactId}/addresses")
    public WebResponse<AddressResponse> create(User user,
                                               @RequestBody CreateAddressRequest request,
                                               @PathVariable("contactId") String contactId) {
        request.setContactId(contactId);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder()
                .data(addressResponse)
                .build();
    }
}
