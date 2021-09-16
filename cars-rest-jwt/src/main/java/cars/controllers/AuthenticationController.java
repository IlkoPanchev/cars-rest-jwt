package cars.controllers;

import cars.constants.GlobalConstants;
import cars.entities.users.model.*;
import cars.entities.users.service.UserService;
import cars.events.logout.OnUserLogoutSuccessEventPublisher;
import cars.jwt.JwtProvider;
import cars.jwt.JwtResponse;
import cars.utils.message.MessageResponse;
import cars.entities.users.details.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final OnUserLogoutSuccessEventPublisher onUserLogoutSuccessEventPublisher;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService, ModelMapper modelMapper, OnUserLogoutSuccessEventPublisher onUserLogoutSuccessEventPublisher) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.onUserLogoutSuccessEventPublisher = onUserLogoutSuccessEventPublisher;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserAddBindingModel userAddBindingModel) {

        if(this.userService.existByUsername(userAddBindingModel.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(GlobalConstants.USERNAME_EXISTS));
        }

        if (this.userService.existsByEmail(userAddBindingModel.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(GlobalConstants.EMAIL_EXISTS));
        }

        UserAddServiceModel userAddServiceModel = this.modelMapper
                .map(userAddBindingModel, UserAddServiceModel.class);

        UserViewServiceModel userViewServiceModel = this.userService.addUser(userAddServiceModel);

        JwtResponse jwtResponse = authenticateUser(userAddBindingModel.getUsername(), userAddBindingModel.getPassword());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginBindingModel userLoginBindingModel) {

        JwtResponse jwtResponse = authenticateUser(userLoginBindingModel.getUsername(), userLoginBindingModel.getPassword());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String headerAuth){

            String authToken = headerAuth.substring(7);

            this.onUserLogoutSuccessEventPublisher.publishLogout(authToken);

            return ResponseEntity.ok(new MessageResponse(GlobalConstants.USER_LOGOUT));

    }



    private JwtResponse authenticateUser(String username, String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }
}
