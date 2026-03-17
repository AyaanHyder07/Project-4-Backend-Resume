// package com.resume.dashboard.config;

// import com.resume.dashboard.entity.User;
// import com.resume.dashboard.repository.UserRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.time.Instant;
// import java.util.UUID;

// @Configuration
// public class AdminUserSeeder {

//     @Bean
//     CommandLineRunner seedAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//         return args -> {
//             if (userRepository.findByUsername("admin1").isPresent()) {
//                 return;
//             }

//             User user = new User();
//             user.setId(UUID.randomUUID().toString());
//             user.setUsername("admin1");
//             user.setPhoneNumber("9999999999");
//             user.setPassword(passwordEncoder.encode("admin1"));
//             user.setRole("ROLE_ADMIN");
//             user.setStatus(User.UserStatus.ACTIVE);
//             user.setCreatedAt(Instant.now());
//             user.setLastLogin(Instant.now());
//             userRepository.save(user);
//         };
//     }
// }
