package com.foro.forordokotoro;

import com.foro.forordokotoro.Models.Enumerations.ERole;
import com.foro.forordokotoro.Models.Notifications;
import com.foro.forordokotoro.Models.Role;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Repository.RoleRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.services.EmailSenderService;
import com.foro.forordokotoro.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
public class ForodokotoroApplication implements CommandLineRunner {
	@Autowired
	private EmailSenderService senderService;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UtilisateursRepository utilisateursRepository;

	@Autowired
	NotificationRepository notificationRepository;

	public static void main(String[] args) {
		SpringApplication.run(ForodokotoroApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if(roleRepository.findAll().size() == 0){
			roleRepository.save(new Role(ERole.ROLE_USER));
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
			roleRepository.save(new Role(ERole.ROLE_SUPERADMIN));
			roleRepository.save(new Role(ERole.ROLE_AGRIGULTEUR));
			roleRepository.save(new Role(ERole.ROLE_TRANSPORTEUR));
		}

		if (utilisateursRepository.findAll().size() == 0){
			Set<Role> roles = new HashSet<>(); //pour l'admin
			Set<Role> roles1 = new HashSet<>(); //pour le super admin

			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);

			Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPERADMIN);;

			roles.add(adminRole);

			//super admin a deux roles
			roles1.add(adminRole);
			roles1.add(superAdminRole);

			//le super admin
			Utilisateurs superadmin = new Utilisateurs("82644606",
					"kmahamadou858@gmail.com",
					encoder.encode("keit@pass"), "Badinko/kita", "Mahamadou KEITA", true, false, false);
			superadmin.setRoles(roles1);



			//l'admin
			Utilisateurs admin = new Utilisateurs("74629747",
					"ksamake18@gmail.com",
					encoder.encode("kadi@pass"), "Molobala", "Kadiditou SAMAKE", true, false, false);
			superadmin.setRoles(roles1);
			admin.setRoles(roles);

			utilisateursRepository.save(superadmin);
			utilisateursRepository.save(admin);
				try {
					String messageSuperAdmin = "Bonjour KEITA Mahamadou nous sommes heureux de vous annoncer les cooordonnées de votre compte super admin sur Forodôkôtôrô:\n password: keit@pass \n avec le numero de téléphone: 82644606";
					String messageAdmin = "Bonjour SAMAKE Kadidiatou nous sommes heureux de vous annoncer les cooordonnées de votre compte admin sur Forodôkôtôrô ou en tant qu'une professionnelle de l'agricultuture:\n password: kadi@pass \n avec le numero de téléphone: 74629747";
					senderService.sendSimpleEmail("mamakabah1999@gmail.com", "Creation de votre compte Super admin sur Forodôkôtôrô", messageSuperAdmin);
					senderService.sendSimpleEmail("ksamake18@gmail.com", "Creation de votre compte admin sur Forodôkôtôrô", messageAdmin);

					Notifications notifSuperAdmin = new Notifications("Creation de votre compte Super admin sur Forodôkôtôrô", messageSuperAdmin, new Date(), false);
					notifSuperAdmin.setUserid(superadmin);
					Notifications notifAdmin = new Notifications("Creation de votre compte admin sur Forodôkôtôrô", messageAdmin, new Date(), false);
					notifAdmin.setUserid(admin);

					notificationRepository.save(notifSuperAdmin);
					notificationRepository.save(notifAdmin);
				}catch (Exception e){
					System.out.println(e);
				}
		}
	}

}
