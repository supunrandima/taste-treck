package com.smartmenu.taste_trek.service;



import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.repository.AuthenticationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    AuthenticationRepo authenticationRepo;

    public void saveAuthToken(AuthenticationToken auth) {
        authenticationRepo.save(auth);
    }

    public boolean authenticate(String email, String authTokenValue)
    {
        AuthenticationToken authToken = authenticationRepo.findByTokenValue(authTokenValue);

        if(authToken == null)
        {
            return false;
        }

        String tokenConnectedEmail = authToken.getCustomer().getEmail();

        return tokenConnectedEmail.equals(email);
    }
}
