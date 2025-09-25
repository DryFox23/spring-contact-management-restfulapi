package contact.management.restfulapi.service;

import contact.management.restfulapi.model.LoginUserRequest;
import contact.management.restfulapi.model.RegisterUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    public void validate(Object request) {
        Set<ConstraintViolation<Object>> violations = validator.validate(request);
        if(violations.size() != 0){
            //error
            throw new ConstraintViolationException(violations);
        }
    }
}
