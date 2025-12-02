package com.smartmenu.taste_trek.service;



import com.smartmenu.taste_trek.entity.Admin;
import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.entity.Customer;
import com.smartmenu.taste_trek.entity.Staff;
import com.smartmenu.taste_trek.repository.AdminRepo;
import com.smartmenu.taste_trek.repository.AuthenticationRepo;
import com.smartmenu.taste_trek.repository.CustomerRepo;
import com.smartmenu.taste_trek.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    AuthenticationRepo authenticationRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    StaffRepo staffRepo;

    public void saveAuthToken(AuthenticationToken auth) {
        authenticationRepo.save(auth);
    }

    public boolean authenticate(String phone, String authTokenValue)
    {
        AuthenticationToken authToken = authenticationRepo.findByTokenValue(authTokenValue);

        if(authToken == null)
        {
            return false;
        }

        String tokenConnectedPhone = authToken.getCustomer().getPhone();

        return tokenConnectedPhone.equals(phone);
    }

    // In AuthenticationService.java
    public AuthenticationToken generateTokenForCustomer(String phone) {
        Customer customer = customerRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phone));

        AuthenticationToken token = new AuthenticationToken(customer);
        authenticationRepo.save(token);
        return token;
    }
    public AuthenticationToken generateTokenForAdmin(String userId) {
        Admin admin = adminRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found with User ID: " + userId));

        AuthenticationToken token = new AuthenticationToken(admin);
        authenticationRepo.save(token);
        return token;
    }

    public AuthenticationToken generateTokenForStaff(String staffId) {
        Staff staff = staffRepo.findByStaffId(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with Staff ID: " + staffId));

        AuthenticationToken token = new AuthenticationToken(staff);
        authenticationRepo.save(token);
        return token;
    }

}
