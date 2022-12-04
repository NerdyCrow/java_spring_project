package by.slemnev.Clinic.rest;

import by.slemnev.Clinic.Exceptions.UsersValidationException;
import by.slemnev.Clinic.dto.AuthenticationRequestDto;
import by.slemnev.Clinic.dto.UserDto;
import by.slemnev.Clinic.model.User;
import by.slemnev.Clinic.security.jwt.JwtTokenProvider;
import by.slemnev.Clinic.service.DoctorService;
import by.slemnev.Clinic.service.PatientService;
import by.slemnev.Clinic.service.RoleService;
import by.slemnev.Clinic.service.UserService;
import by.slemnev.Clinic.validator.EmailValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(AuthenticationRestControllerV1.class);
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
   private final EmailValidator emailValidator;
    private final UserService userService;
    private final RoleService roleService;
    private  final DoctorService doctorService;
    private  final PatientService patientService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager,
                                          JwtTokenProvider jwtTokenProvider, EmailValidator emailValidator,
                                          UserService userService, RoleService roleService,DoctorService doctorService,
                                          PatientService patientService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailValidator = emailValidator;
        this.userService = userService;
        this.roleService = roleService;
        this.doctorService=doctorService;
        this.patientService=patientService;
    }





    @Operation(summary="Register")
    @PostMapping("Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity Regiser(@Valid @RequestBody UserDto requestDto, BindingResult errors) {

        emailValidator.validate(requestDto,errors);
        log.info("Regiser:/api/v1/auth/Register");
        Map<Object, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            throw new UsersValidationException(errors);
        }
        User user = requestDto.toUser();
        try {

            System.out.println(userService.findByEmail(user.getEmail()).getId());
            response.put("username", "User for this email found");
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }catch (Exception e){
            userService.register(user);
            response.put("username", "user register successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    }
    @Operation(summary="Login" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login",
                    content = {@Content(mediaType = "application/json")})
    })

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {

        String username = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        User user = userService.findByEmail(username);
        Map<Object, Object> response = new HashMap<>();
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        System.out.println(user.getId()+" login");
        String token = jwtTokenProvider.createToken(username, user.getRoles());

            if(patientService.findbyUser(userService.findByEmail(username))==-2L &&
                    doctorService.findbyUser(userService.findByEmail(username))==-2L){
                response.put("who", "adm");
            }
            else{
                if(patientService.findbyUser(userService.findByEmail(username))!=-2L){
                    response.put("who", "pat");
                    response.put("id_patient",patientService.findbyUser(userService.findByEmail(username)));
                }else{
                    response.put("who", "doc");
                    response.put("id_doc",doctorService.findbyUser(userService.findByEmail(username)));
                }
            }
        log.info("login:/api/v1/auth/login");
        response.put("email", username);
        response.put("token", token);
        response.put("ROLE",user.getRoles().get(0).getId());
        response.put("USER_ID",user.getId());
        return ResponseEntity.ok(response);
    }
}
