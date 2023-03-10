package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.ERole;
import com.foro.forordokotoro.Repository.RoleRepository;
import com.foro.forordokotoro.Repository.TransporteurRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.request.OtpRequest;
import com.foro.forordokotoro.Utils.response.OtpResponse;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.Utils.request.LoginRequest;
import com.foro.forordokotoro.Utils.request.SignupRequest;
import com.foro.forordokotoro.Utils.response.JwtResponse;
import com.foro.forordokotoro.Utils.response.MessageResponse;
import com.foro.forordokotoro.security.jwt.JwtUtils;
import com.foro.forordokotoro.security.services.UtilisateurService;
import com.foro.forordokotoro.security.services.UtilisateursDetails;
import com.foro.forordokotoro.services.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private EmailSenderService senderService;

  @Autowired
  UtilisateursRepository utilisateursRepository;

  @Autowired
  TransporteurRepository transporteurRepository;

  @Autowired
  RoleRepository roleRepository;

  //encoder du password
  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  UtilisateurService utilisateurService;

  //@Valid assure la validation de l'ensemble de l'objet
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {


    /*
     AuthenticationManager est comme un coordinateur o?? vous pouvez enregistrer plusieurs fournisseurs et,
     en fonction du type de demande, il enverra une demande d'authentification au bon fournisseur.
     */

    //authenticate effectue l'authentification avec la requ??te.

     /*
       AuthenticationManager utilise DaoAuthenticationProvider(avec l'aide de
       UserDetailsService& PasswordEncoder) pour valider l'instance de UsernamePasswordAuthenticationToken,
       puis renvoie une instance enti??rement remplie Authenticationen cas d'authentification r??ussie.
     */

    Authentication authentication = authenticationManager.authenticate(
            //on lui fournit un objet avec username et password fournit par l'admin
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    /*
      SecurityContext et SecurityContextHolder sont deux classes fondamentales de Spring Security .
      Le SecurityContext est utilis?? pour stocker les d??tails de l'utilisateur
      actuellement authentifi??, ??galement appel?? principe. Donc, si vous devez obtenir
      le nom d'utilisateur ou tout autre d??tail d'utilisateur, vous devez d'abord obtenir
      ce SecurityContext . Le SecurityContextHolder est une classe d'assistance qui permet
      d'acc??der au contexte de s??curit??.
     */

    //on stocke les informations de connexion de l'utilisateur actuelle souhaiter se connecter dans SecurityContextHolder
    SecurityContextHolder.getContext().setAuthentication(authentication);

    //on envoie encore les infos au generateur du token
    String jwt = jwtUtils.generateJwtToken(authentication);

    //on recupere les infos de l'user
    UtilisateursDetails utilisateursDetails = (UtilisateursDetails) authentication.getPrincipal();

    //on recupere les roles de l'users
    List<String> roles = utilisateursDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    log.info("conexion controlleur");
    System.err.println("roles: " + roles);

    Utilisateurs user =  utilisateursRepository.findByUsername(utilisateursDetails.getUsername()).get();

    if(roles.contains("ROLE_TRANSPORTEUR")){

      Transporteurs transporteurs = transporteurRepository.findById(utilisateursDetails.getId()).get();
System.err.println("je suis transporteur "+ transporteurs);
      //on retourne une reponse, contenant l'id username, e-mail et le role du collaborateur
      return ResponseEntity.ok(new JwtResponse(jwt,
              utilisateursDetails.getId(),
              utilisateursDetails.getUsername(),
              utilisateursDetails.getEmail(),
              roles,
              utilisateursDetails.getAdresse(),
              utilisateursDetails.getPhoto(),
              utilisateursDetails.getNomcomplet(),
              transporteurs.getDisponibilite(),
              transporteurs.getNumeroplaque(),
              user.getEtat(),
              user.getSesouvenir(),
              transporteurs.getNombrecontact()
      ));

    } else {
      System.err.println("je suis autre");

      //on retourne une reponse, contenant l'id username, e-mail et le role du collaborateur
      return ResponseEntity.ok(new JwtResponse(jwt,
              utilisateursDetails.getId(),
              utilisateursDetails.getUsername(),
              utilisateursDetails.getEmail(),
              roles,
              utilisateursDetails.getAdresse(),
              utilisateursDetails.getPhoto(),
              utilisateursDetails.getNomcomplet(),
              user.getEtat(),
              user.getSesouvenir()
      ));
    }

  }

  //@PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/signup")//@valid s'assure que les donn??es soit valid??es
  public ResponseEntity<?> registerUser(@Valid  @RequestParam(value = "donneesuser") String donneesuser) throws IOException {

    //converssion du string re??u en classe SignupRequest
    SignupRequest signUpRequest = new JsonMapper().readValue(donneesuser, SignupRequest.class);

    //signUpRequest.setPhoto("nomfile");

    if (utilisateursRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.ok(new Reponse("Ce numero existe d??j??", 0));
    }

    if (utilisateursRepository.existsByEmail(signUpRequest.getEmail())) {

      //confectionne l'objet de retour ?? partir de ResponseEntity(une classe de spring boot) et MessageResponse
      return ResponseEntity.ok(new Reponse("Cet email existe d??j??", 0));
    }

    System.out.println(signUpRequest.getNomcomplet());
    String monpass = signUpRequest.getPassword();
    // Create new user's account
    Utilisateurs utilisateurs = new Utilisateurs(signUpRequest.getUsername(),
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()), signUpRequest.getAdresse(),
                signUpRequest.getNomcomplet(), signUpRequest.getPhoto()
            );

    //on recupere le role de l'user dans un tableau ordonn?? de type string
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      System.out.println("####################################" + signUpRequest.getRole() + "###########################################");

      //on recupere le role de l'utilisateur
      Role userRole = roleRepository.findByName(ERole.ROLE_USER);
      roles.add(userRole);//on ajoute le role de l'user ?? roles
    } else {
      strRoles.forEach(role -> {//on parcours le role
        switch (role) {
        case "admin"://si le role est ?? ??gale ?? admin
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
          roles.add(adminRole);

          String messageAdmin = "Bonjour "+ utilisateurs.getNomcomplet() +" nous sommes heureux de vous annoncer les cooordonn??es de votre compte administrateur sur Forod??k??t??r?? :\n password: + "  +monpass+" \n avec le numero de t??l??phone: " + utilisateurs.getUsername();
          //senderService.sendSimpleEmail(utilisateurs.getEmail(), "Creation de votre compte professionnel sur Forod??k??t??r??", messageAdmin);
          break;

          case "professionnel"://si le role est ?? ??gale ?? professionnel
            Role professionnelRole = roleRepository.findByName(ERole.ROLE_PROFESSIONNEL);
            roles.add(professionnelRole);
            String messageProf = "Bonjour "+ utilisateurs.getNomcomplet() +" nous sommes heureux de vous annoncer les cooordonn??es de votre compte professionnel sur Forod??k??t??r?? ou en tant qu'un professionnel de l'agricultuture:\n password: + "  +monpass+" \n avec le numero de t??l??phone: " + utilisateurs.getUsername();
            //senderService.sendSimpleEmail(utilisateurs.getEmail(), "Creation de votre compte professionnel sur Forod??k??t??r??", messageProf);
            break;
        default://dans le cas ??cheant

          //on recupere le role de l'utilisateur
          Role userRole = roleRepository.findByName(ERole.ROLE_USER);
          roles.add(userRole);
        }
      });
    }

    //on ajoute le role au collaborateur
    utilisateurs.setRoles(roles);
    utilisateurs.setEtat(true);
    utilisateurs.setEnligne(false);
    utilisateursRepository.save(utilisateurs);

    return ResponseEntity.ok(new MessageResponse("Utilisateur enregistr?? avec succ??s!"));
  }

  @PatchMapping("/modifierutilisateur/{id}")
  public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Utilisateurs utilisateurs) {

    System.out.println("test test test test test: " + utilisateurs.getPassword());

    if(utilisateurs.getPassword() != null)
      utilisateurs.setPassword(encoder.encode(utilisateurs.getPassword()));

    return utilisateurService.modifierUtilisateur(id, utilisateurs);
  }

  @PatchMapping("/modifierprofil/{id}")
  public ResponseEntity<?> updateUser(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
          @PathVariable Long id) throws IOException {

    String type = "user";


    //recupere le nom de l'image
    String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
    System.out.println(nomfile);

    return utilisateurService.modifierProfil(id, nomfile, type, file);
  }

  @GetMapping("/utilisatuersactive")
  public List<Utilisateurs> recupererUtilisateurActives(){
    return utilisateurService.recupererUtilisateurActive();
  }

  @PostMapping("/motdepasseoublier")
  public ResponseEntity<?> motDePasseOublier(@RequestBody OtpRequest otpRequest){
    if(utilisateursRepository.existsByEmail(otpRequest.getEmail())){

      Utilisateurs utilisateurs = utilisateursRepository.findByEmail(otpRequest.getEmail());

      //declaration d'un objet random
      Random random = new Random();

      //ici on genere un nombre aleatoire comprise entre 0 et 9999
      int randomCode = random.nextInt(9000) + 1000;

      //ici on ajoute des devant pour completer sa taille ?? 3 dans le cas ou sa taille ne vaut pas trois
      //String.format("%04d", randomCode);

      OtpResponse otpResponse = new OtpResponse(randomCode, "code generer avec succ??s", 1);
      otpResponse.setIduser(utilisateurs.getId());
      otpResponse.setValidite(true);

     senderService.sendSimpleEmail(otpRequest.getEmail(), "Renitialisation de mot de passe", "Bonjour " + utilisateurs.getNomcomplet() + " Le code ?? quatre chiffre ci-dessous est le code de renitialisation de votre mot de passe " + randomCode);

      return ResponseEntity.ok(otpResponse);

    }else {
      OtpResponse otpResponse = new OtpResponse("Adresse email introuvable",  0);

      return ResponseEntity.ok(otpResponse);
    }
  }

  @GetMapping("/recuperertousutilisateur")
  public ResponseEntity<?> recuperertousUtilisateur(){
    return ResponseEntity.ok(utilisateursRepository.findByEtat(true));
  }
}
