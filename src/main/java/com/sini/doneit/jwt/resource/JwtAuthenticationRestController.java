package com.sini.doneit.jwt.resource;

import com.sini.doneit.jwt.JwtTokenUtil;
import com.sini.doneit.jwt.JwtUserDetails;
import com.sini.doneit.model.MessageCode;
import com.sini.doneit.model.PersonalCard;
import com.sini.doneit.model.ResponseMessage;
import com.sini.doneit.model.User;
import com.sini.doneit.repository.PersonalCardJpaRepository;
import com.sini.doneit.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationRestController {

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PersonalCardJpaRepository personalCardJpaRepository;


    @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
            throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        System.out.println("Password: " + authenticationRequest.getPassword());
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        if(checkIfFirstLogin(authenticationRequest.getUsername())){
            return ResponseEntity.ok(new ResponseMessage("Login effettuato con successo per la prima volta"
                    , MessageCode.FIRST_LOGIN, token));
        }
        return ResponseEntity.ok(new ResponseMessage("Login effettuato con successo", MessageCode.SUCCESSFUL_LOGIN, token));
    }

    @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUserDetails user = (JwtUserDetails) jwtInMemoryUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ResponseMessage> handleAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage(), MessageCode.TOKEN_EXCEPTION), HttpStatus.UNAUTHORIZED);
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
    }

    private boolean checkIfFirstLogin(String username){

        User user = userJpaRepository.findByUsername(username);
        return !personalCardJpaRepository.findByUserId(user.getId()).getDone();
    }
}