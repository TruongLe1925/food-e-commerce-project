package com.myproject.e_commerce.restController.AuthenticationRestController;

import com.myproject.e_commerce.dto.InstrospectRequestDTO;
import com.myproject.e_commerce.dto.RequestDTO;
import com.myproject.e_commerce.response.AuthenticationResponse;
import com.myproject.e_commerce.response.InstrospectResponse;
import com.myproject.e_commerce.service.AuthenticateService.AuthenticateService;
import com.nimbusds.jose.JOSEException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("api/auth")
public class AuthenticationRestController {
    private final AuthenticateService authenticateService;
    public AuthenticationRestController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody RequestDTO  requestDTO) {
        AuthenticationResponse authenticate = authenticateService.authenticate(requestDTO);
        return authenticate;
    }
    @PostMapping("/instrospect")
    public InstrospectResponse instrospect(@RequestBody InstrospectRequestDTO instrospectRequestDTO) {
        try {
            return authenticateService.instrospect(instrospectRequestDTO);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
