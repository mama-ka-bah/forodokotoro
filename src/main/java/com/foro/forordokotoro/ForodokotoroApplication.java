package com.foro.forordokotoro;

import com.foro.forordokotoro.Models.Enumerations.ERole;
import com.foro.forordokotoro.Models.Role;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.RoleRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ForodokotoroApplication implements CommandLineRunner {


	@Autowired
	private EmailSenderService senderService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UtilisateursRepository utilisateursRepository;


	public static void main(String[] args) {
		SpringApplication.run(ForodokotoroApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if(roleRepository.findAll().size() == 0){
			roleRepository.save(new Role(ERole.ROLE_USER));
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
			roleRepository.save(new Role(ERole.ROLE_AGRIGULTEUR));
			roleRepository.save(new Role(ERole.ROLE_TRANSPORTEUR));
		}

		if (utilisateursRepository.findAll().size() == 0){
			Set<Role> roles = new HashSet<>();
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
			roles.add(adminRole);
			Utilisateurs utilisateurs = new Utilisateurs("keita@123",
					"kmahamadou858@gmail.com",
					encoder.encode("keita@pass"), "Badinko/kita", "Mahamadou KEITA", true);
			utilisateurs.setRoles(roles);
			utilisateursRepository.save(utilisateurs);
		}
	}

}
