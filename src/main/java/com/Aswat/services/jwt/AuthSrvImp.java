package com.Aswat.services.jwt;

import com.Aswat.Dtos.SignupRequest;
import com.Aswat.Dtos.UserDto;
import com.Aswat.entity.User;
import com.Aswat.enums.UserRole;
import com.Aswat.reposistories.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthSrvImp implements AuthoSrv {
    private final UserRepo userRepo;

    public AuthSrvImp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostConstruct
    public void createAdminAccount() {
        // Vérifier s'il existe déjà un compte admin
        //Optional<User> existingAdminAccount = userRepo.findFirstByEmail("khaoula@gmail.com");
        User adminAccount = userRepo.findByUserRole(UserRole.ADMIN);
        if(adminAccount==null){            // Créer un nouvel utilisateur admin
            User user = new User();
            user.setName("admin");
            user.setEmail("admin@gmail.com");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setUserRole(UserRole.ADMIN);
            userRepo.save(user);
        }
    }

    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        // Vérifier s'il existe déjà un utilisateur avec cet email
        Optional<User> existingUser = userRepo.findFirstByEmail(SignupRequest.getEmail());

        if (existingUser.isPresent()) {
            // Gérez ici le cas où l'e-mail est déjà utilisé
            throw new IllegalArgumentException("Email already in use.");
        }

        // Créer un nouvel utilisateur
        User newUser = new User();
        newUser.setName(SignupRequest.getName());
        newUser.setEmail(SignupRequest.getEmail());
        newUser.setPassword(new BCryptPasswordEncoder().encode(SignupRequest.getPassword()));
        newUser.setUserRole(UserRole.CUSTOMER);

        // Enregistrer le nouvel utilisateur
        User createdUser = userRepo.save(newUser);

        // Créer un objet UserDto à partir de l'utilisateur créé
        UserDto createdUserDto = new UserDto();
        createdUserDto.setId(createdUser.getId());
        createdUserDto.setName(createdUser.getName());
        createdUserDto.setEmail(createdUser.getEmail());
        createdUserDto.setUserRole(createdUser.getUserRole());

        return createdUserDto;
    }
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public boolean deleteUser(Long id) {
        // Vérifier si l'utilisateur existe
        if (userRepo.existsById(id)) {
            // Supprimer l'utilisateur de la base de données
            userRepo.deleteById(id);
            return true; // Retourne true si la suppression réussit
        } else {
            return false; // Retourne false si l'utilisateur n'est pas trouvé
        }
    }
}

