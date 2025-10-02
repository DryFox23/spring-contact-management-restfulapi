package contact.management.restfulapi.controller;

import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.AddressResponse;
import contact.management.restfulapi.model.CreateAddressRequest;
import contact.management.restfulapi.model.UpdateAddressRequest;
import contact.management.restfulapi.model.WebResponse;
import contact.management.restfulapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Controller for get Address
    @GetMapping(path = "/api/contacts/{contactId}/addresses/{addressId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> get(User user,
                                            @PathVariable("contactId") String contactId,
                                            @PathVariable("addressId") String addressId){

        AddressResponse addressResponse = addressService.get(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder()
                .data(addressResponse)
                .build();
    }

    // Controller for delete Address
    @DeleteMapping(path = "/api/contacts/{contactId}/addresses/{addressId}",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(User user,
                                               @PathVariable("contactId") String contactId,
                                               @PathVariable("addressId") String addressId) {

        addressService.delete(user, contactId, addressId);
        return WebResponse.<String>builder()
                .data("Address successfully deleted")
                .build();
    }

    // Controller for update Address
    @PutMapping(path = "/api/contacts/{contactId}/addresses/{addressId}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> update(User user,
                                               @RequestBody UpdateAddressRequest request,
                                               @PathVariable("contactId") String contactId,
                                               @PathVariable("addressId") String addressId) {
        request.setContactId(contactId);
        request.setAddressId(addressId);

        AddressResponse response = addressService.update(user,contactId,addressId, request);
        return WebResponse.<AddressResponse>builder()
                .data(response)
                .build();
    }

    // Controller for get all addresses by contact
    @GetMapping(path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<AddressResponse>> list(User user,
                                                  @PathVariable("contactId") String contactId){

        List<AddressResponse> response = addressService.list(user, contactId);
        return WebResponse.<List<AddressResponse>>builder()
                .data(response)
                .build();
    }
}
