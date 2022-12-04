package by.slemnev.Clinic.rest;

import by.slemnev.Clinic.Exceptions.UsersValidationException;
import by.slemnev.Clinic.dto.*;
import by.slemnev.Clinic.dto.*;
import by.slemnev.Clinic.model.Doctor;
import by.slemnev.Clinic.model.Patient;
import by.slemnev.Clinic.model.Status;
import by.slemnev.Clinic.model.User;
import by.slemnev.Clinic.repository.*;
import by.slemnev.Clinic.repository.DoctorCrudRepository;
import by.slemnev.Clinic.repository.PatientCrudRepository;
import by.slemnev.Clinic.repository.UserCrudRepository;
import by.slemnev.Clinic.repository.UserRepository;
import by.slemnev.Clinic.service.DoctorService;
import by.slemnev.Clinic.service.PatientService;
import by.slemnev.Clinic.service.ReceptionService;
import by.slemnev.Clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/admin/")
public class AdminRestControllerV1 {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(AdminRestControllerV1.class);
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final ReceptionService receptionService;
    private final PatientCrudRepository patientCrudRepository;
    private final DoctorCrudRepository doctorCrudRepository;
    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    public AdminRestControllerV1(UserRepository userRepository, PatientService patientService, ReceptionService receptionService, PatientCrudRepository patientCrudRepository, DoctorCrudRepository doctorCrudRepository,
                                 UserService userService, UserCrudRepository userCrudRepository, DoctorService doctorService) {
        this.userService = userService;
        this.doctorService=doctorService;
        this.patientService=patientService;
        this.patientCrudRepository=patientCrudRepository;
        this.doctorCrudRepository=doctorCrudRepository;
        this.receptionService=receptionService;
    }
    @Operation(summary="Get user by id")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get user by id",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        AdminUserDto result = AdminUserDto.fromUser(user);
        log.info("getUserById:/api/v1/admin/users/{id}");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary="Get page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get page",
                    content = {@Content(mediaType = "application/json")})
    })
    @RequestMapping( value = "admin/{page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> GetPages(@PathVariable(value = "page") Integer page) {
        List<User> users=userService.getPage(PageRequest.of(page,4)).toList();
        log.info("Post:/api/v1/admin/admin/{page}");

        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    @Operation(summary="Get count page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get page count",
                    content = {@Content(mediaType = "application/json")})
    })
    @RequestMapping(value = "admin/pages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity GetPagesCount(){

        int pagesCount = userService.getPage(PageRequest.of(1,4)).getTotalPages();
        Map<Object, Object> response = new HashMap<>();
        response.put("count", pagesCount);
        log.info("GetPagesCount:/api/v1/admin/admin/pages");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary="Get patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post patient",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "patient")
    public ResponseEntity PostPatient(@RequestBody AdminUserDto requestDto) {
        List<Patient> patient=patientService.getAll();
        StringBuilder json= new StringBuilder("[");
        for (Patient value : patient) {
            json.append("{" + '"' + "home_adress" + '"' + ":" + '"').
                    append(value.getHomeadress()).append('"').append(",").append('"').
                    append("id").append('"').append(":").append('"').append(value.getId()).
                    append('"').append(",").append('"').append("passport").append('"').append(":").
                    append('"').append(value.getPassport()).append('"').append(",").append('"').
                    append("id_user").append('"').append(":").append('"').append(value.getUser().getId()).
                    append('"').append("},");
        }
        json.append("]");
        json = new StringBuilder(json.toString().replace("},]", "}]"));
        System.out.println(json);
        Map<Object, Object> response = new HashMap<>();
        response.put("patients", json.toString());
        log.info("PostPatient:/api/v1/admin/patient");

        return ResponseEntity.ok(response);
    }
    @Operation(summary="Get doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post doctor",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "doctor")
    public ResponseEntity PostDoctor(@RequestBody AdminUserDto requestDto) {
        List<Doctor> doctors=doctorService.getAll();

        StringBuilder json= new StringBuilder("[");
        for (Doctor doctor : doctors) {
            json.append("{" + '"' + "Name_Hospital" + '"' + ":" + '"').
                    append(doctor.getName_Hospital()).append('"').append(",").append('"').
                    append("id").append('"').append(":").append('"').append(doctor.getId()).
                    append('"').append(",").append('"').append("passport").append('"').append(":").
                    append('"').append(doctor.getPassport()).append('"').append(",").append('"').
                    append("id_user").append('"').append(":").append('"').append(doctor.getUser().getId()).
                    append('"').append(",").append('"').append("Specialty").append('"').append(":").append('"').
                    append(doctor.getSpecialty()).append('"').append("},");
        }
        json.append("]");
        json = new StringBuilder(json.toString().replace("},]", "}]"));
        System.out.println(json);
        Map<Object, Object> response = new HashMap<>();
        response.put("doctor", json.toString());
        log.info("PostDoctor:/api/v1/admin/doctor");
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Del user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete user",
                    content = {@Content(mediaType = "application/json")})
    })
    @RequestMapping(value = "delUser/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity DeleteUser(@PathVariable(value = "id") Long id){
        Map<Object, Object> response = new HashMap<>();
        try{
            User user=userService.findById(id);
        if((user.toString().length()==0 )|| !patientService.GetUserInPatient(userService.findById(id))||
        !doctorService.GetUserInDoctor(userService.findById(id))||user.getRoles().toString().contains("ROLE_ADMIN")){
            response.put("userError", "user not found or delete on patient or doctor or this user ADMIN");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        response.put("userError", "user del successfully");
        userService.delete(id);
        }catch (Exception e){
            response.put("userError", "user not found or delete on patient or doctor or this user ADMIN");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        log.info("DeleteUser:/api/v1/admin/doctor");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @Operation(summary="del null reception")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete reception",
                    content = {@Content(mediaType = "application/json")})
    })
    @RequestMapping(value = "delreception", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity DeleteRECEPTION(){
        Map<Object, Object> response = new HashMap<>();
        try{
            receptionService.delete();
            response.put("userError", "reception delete successfully");
        }catch (Exception e){
            response.put("userError", "error delete");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        log.info("DeleteRECEPTION:/api/v1/admin/delreception");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @Operation(summary="Add doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "add doctor",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("addDoctor")
    public ResponseEntity RegiserDoctor(@Valid @RequestBody DoctorDto requestDto, BindingResult errors) {
        Map<Object, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            throw new UsersValidationException(errors);
        }
        try {
            Doctor doctor = requestDto.toDoctor();
            User user = userService.findById(doctor.getId());
            if(user.getRoles().toString().contains("ROLE_ADMIN")){
                response.put("userError", "user ADMIN this user cannot be added");
            }
          else  if (doctorService.GetUserInDoctor(user) && patientService.GetUserInPatient(user)) {
                user.setStatus(Status.ACTIVE);
                doctor.setUser(user);
                SimpleMailMessage message = new SimpleMailMessage();
                Doctor doctor1 = doctorService.register(doctor);
                System.out.println(user.getEmail());
                message.setTo(user.getEmail());
                message.setSubject("BSTU");
                message.setText("Welcome to the hospital");
                this.emailSender.send(message);
                System.out.println(doctor1.toString());
                response.put("userError", "user add successfully");
            } else {
                response.put("userError", "user not found or user include on doctor or patient");
            }

        }catch (Exception e){
            response.put("userError", "user not found or user include on doctor or patient or check email");
        }
        log.info("RegiserDoctor:/api/v1/admin/addDoctor");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary="Del doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete user",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("delDoctor")
    public ResponseEntity DelDoctor(@Valid @RequestBody DeleteDoctorDto requestDto, BindingResult errors) {
        Map<Object, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            throw new UsersValidationException(errors);
        }
        try {
            Doctor doctor = requestDto.toDoctor();
            Doctor doctors = doctorService.findById(doctor.getId());
            User user = userService.findById(doctors.getUser().getId());
            if (user != null) {
                doctorService.delete(doctor.getId());
                response.put("userError", "user delete successfully");
            } else {
                response.put("userError", "user not found ");
            }
        }catch (Exception e){
            response.put("userError", "user not found ");
        }
        log.info(" DelDoctor:/api/v1/admin/delDoctor");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @Operation(summary="Update doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update user",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("updateDoctor")
    public ResponseEntity UpdateDoctor(@Valid @RequestBody UpdateDoctorDto requestDto, BindingResult errors) {
        Map<Object, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            throw new UsersValidationException(errors);
        }
        try{
        Doctor doctor=requestDto.toDoctor();
        Doctor doctors=doctorService.findById(doctor.getId());
        User user=userService.findById(doctors.getUser().getId());
            doctor.setUser(user);
            doctorCrudRepository.save(doctor);
            System.out.println("update прошло успешно");
            response.put("userError", "user update successfully");
        }catch (Exception e){
            response.put("userError", "user not found or user include on doctor or patient");
        }

        log.info("UpdateDoctor:/api/v1/admin/updateDoctor");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary="Update patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update patient",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("updatePatient")
    public ResponseEntity UpdatePatient(@Valid @RequestBody UpdatePatientDto requestDto, BindingResult errors) {
    Map<Object, Object> response = new HashMap<>();
    if(errors.hasErrors()){
        throw new UsersValidationException(errors);
    }
    Patient patient=requestDto.toPatient();
    Patient patients=patientService.findById(patient.getId());
    try{
    User user=userService.findById(patients.getUser().getId());
    if(user != null){
            patient.setUser(user);
            patientCrudRepository.save(patient);
            System.out.println("Изменение прошло успешно");
            response.put("userError", "operation completed successfully");
            System.out.println(patients.toString()+"||Patient");
        }
        else{
            response.put("userError", "user not found or user include on doctor or patient");
        }
    }catch (Exception e){
        response.put("userError", "user not found or user include on doctor or patient");
    }
        log.info("UpdatePatient:/api/v1/admin/updatePatient");
    return new ResponseEntity<>(response, HttpStatus.OK);
}
    @Operation(summary="Del patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete patient",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("delPatient")
    public ResponseEntity DelPatient(@Valid @RequestBody DeletePatientDto requestDto, BindingResult errors) {
        Map<Object, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            throw new UsersValidationException(errors);
        }
        try {
            Patient patient = requestDto.toPatient();
            Patient patients = patientService.findById(patient.getId());
            User user = userService.findById(patients.getUser().getId());
            patientService.delete(patient.getId());
            System.out.println("Удаление прошло успешно");
            response.put("userError", "user delete successfully");
        }catch (Exception e){
            response.put("userError", "user not found ");
        }
        log.info("DelPatient:/api/v1/admin/delPatient");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary="Add patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "add patient",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("addPatient")
    public ResponseEntity RegiserPatient(@Valid @RequestBody PatientDto requestDto, BindingResult errors) {
        Map<Object, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            throw new UsersValidationException(errors);
        }
        try {
            Patient patient = requestDto.toPatient();
            User user = userService.findById(patient.getId());
            if(user.getRoles().toString().contains("ROLE_ADMIN")){
                response.put("userError", "user ADMIN this user cannot be added");
            }
          else  if (doctorService.GetUserInDoctor(user) && patientService.GetUserInPatient(user)) {
                response.put("userError", "user add successfully");
                user.setStatus(Status.ACTIVE);
                patient.setUser(user);
                SimpleMailMessage message = new SimpleMailMessage();
                Patient patient1 = patientService.register(patient);
                message.setTo(user.getEmail());
                //message.setTo(user1.getEmail());
                message.setSubject("BSTU");
                message.setText("Welcome to the hospital");
                this.emailSender.send(message);
                System.out.println(patient1.toString());
            } else {
                response.put("userError", "user not found or user include on doctor or patient");
            }
        }
        catch (Exception e){
            response.put("userError", "user not found or user include on doctor or patient");
        }

        log.info("RegiserPatient:/api/v1/admin/addPatient");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
