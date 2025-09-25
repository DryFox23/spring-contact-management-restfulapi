package contact.management.restfulapi.controller;

import contact.management.restfulapi.model.RegisterUserRequest;
import contact.management.restfulapi.model.WebResponse;
import contact.management.restfulapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest registerUserRequest){
        userService.register(registerUserRequest);
        return WebResponse.<String>builder().data("success").build();
    }
}
