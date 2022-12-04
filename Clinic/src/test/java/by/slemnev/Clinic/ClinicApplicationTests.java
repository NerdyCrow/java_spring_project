package by.slemnev.Clinic;

import by.slemnev.Clinic.model.*;
import by.slemnev.Clinic.repository.DoctorRepository;
import by.slemnev.Clinic.repository.PatientRepository;
import by.slemnev.Clinic.repository.ReceptionRepository;
import by.slemnev.Clinic.repository.UserRepository;
import by.slemnev.Clinic.security.jwt.JwtTokenProvider;
import by.slemnev.Clinic.model.Doctor;
import by.slemnev.Clinic.model.Patient;
import by.slemnev.Clinic.model.Reception;
import by.slemnev.Clinic.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class ClinicApplicationTests {
	@MockBean
	private DoctorRepository doctorRepository;

	@MockBean
	private PatientRepository patientRepository;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private ReceptionRepository receptionRepository;

	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	WebApplicationContext webApplicationContext;


	@Test
	public void testGetUsersRepository() {
		List<User> users = Arrays.asList(
				new User(),
				new User()
		);
		when(userRepository.findAll()).thenReturn(users);

		Assert.assertEquals(userRepository.findAll(), users);
	}

		    @Test
	    public void testGetPatientsRepository() {
				List<Patient> patients = Arrays.asList(
						new Patient(),
						new Patient()
				);
				when(patientRepository.findAll()).thenReturn(patients);

				Assert.assertEquals(patientRepository.findAll(), patients);
			}

	@Test
	public void testGetDoctorsRepository() {
		List<Doctor> doctors = Arrays.asList(
				new Doctor(),
				new Doctor()
		);
		when(doctorRepository.findAll()).thenReturn(doctors);

		Assert.assertEquals(doctorRepository.findAll(), doctors);
	}
	@Test
	public void testGetReceptionRepository() throws Exception {
		List<Reception> receptions = Arrays.asList(
				new Reception(),
				new Reception()
		);
		when(receptionRepository.findAll()).thenReturn(receptions);

		Assert.assertEquals(receptionRepository.findAll(), receptions);
	}
}
