package com.myproject.e_commerce.service.AuthenticateService;

import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dto.InstrospectRequestDTO;
import com.myproject.e_commerce.dto.RequestDTO;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.exception.exception.AccessDeniedException;
import com.myproject.e_commerce.exception.exception.UserNotFoundException;
import com.myproject.e_commerce.repository.UserRepository;
import com.myproject.e_commerce.response.AuthenticationResponse;
import com.myproject.e_commerce.response.InstrospectResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Value("${secretKey}")
    protected String secretKey;
    public AuthenticateServiceImpl(PasswordEncoder passwordEncoder,UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public AuthenticationResponse authenticate(RequestDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername()).
                orElseThrow(()->new UserNotFoundException("Khong Ton Tai User Nay"));
        boolean authenticated = passwordEncoder.matches(requestDTO.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AccessDeniedException("Khong co quyen vao");
        }
        try {
            String token = generateToken(requestDTO.getUsername());
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(authenticated)
                    .build();
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InstrospectResponse instrospect(InstrospectRequestDTO instrospectRequestDTO) throws JOSEException, ParseException {
        String token = instrospectRequestDTO.getToken();
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        return  InstrospectResponse.builder()
                .valid(verified && expiration.after(new Date()))
                .build();
    }

    //GenerateTokenForJWT
    private String generateToken(String username) throws KeyLengthException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<String> authorities = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority().name())
                .collect(Collectors.toList());
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("com.myproject.e_commerce")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("username",username)
                .claim("roles",authorities)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject  jwsObject =  new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create Token",e);
            throw new RuntimeException(e);
        }

    }
}
