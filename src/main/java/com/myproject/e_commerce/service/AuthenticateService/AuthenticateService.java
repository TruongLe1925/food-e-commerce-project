package com.myproject.e_commerce.service.AuthenticateService;

import com.myproject.e_commerce.dto.InstrospectRequestDTO;
import com.myproject.e_commerce.dto.RequestDTO;
import com.myproject.e_commerce.response.AuthenticationResponse;
import com.myproject.e_commerce.response.InstrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticateService {
    AuthenticationResponse authenticate(RequestDTO requestDTO);
    InstrospectResponse instrospect(InstrospectRequestDTO instrospectRequestDTO) throws JOSEException, ParseException;
}
