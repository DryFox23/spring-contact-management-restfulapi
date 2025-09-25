package contact.management.restfulapi.service;

import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.LoginUserRequest;
import contact.management.restfulapi.model.TokenResponse;
import contact.management.restfulapi.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password Wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            //login success
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next300days());
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password Wrong");
        }
    }

    private Long next300days(){
        return System.currentTimeMillis() + (300L * 24L * 60L * 60L * 1000L);
    }
}
