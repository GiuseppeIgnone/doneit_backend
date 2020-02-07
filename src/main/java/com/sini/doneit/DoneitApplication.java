package com.sini.doneit;

import com.sini.doneit.controller.RegisterController;
import com.sini.doneit.model.Category;
import com.sini.doneit.model.PersonalCard;
import com.sini.doneit.model.User;
import com.sini.doneit.model.Wallet;
import com.sini.doneit.repository.CategoryJpaRepository;
import com.sini.doneit.repository.UserJpaRepository;
import com.sini.doneit.services.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@SpringBootApplication
public class DoneitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoneitApplication.class, args);
    }

    @Component
    class DemoCommandLineRunner implements CommandLineRunner {

        @Autowired
        private CategoryJpaRepository categoryJpaRepository;

        @Autowired
        private UserJpaRepository userJpaRepository;

        @Override
        public void run(String... args) throws Exception {
            if (categoryJpaRepository.findAll().size() == 0) {
                categoryJpaRepository.save(new Category("Ripetizioni", 9));
                categoryJpaRepository.save(new Category("Riparazione PC", 3));
                categoryJpaRepository.save(new Category("Faccende domestiche", 3));
            }
            if (userJpaRepository.findAll().size() == 0) {
                insertUser("Francesco", "Di Sario", "fra_de_saa", "Password1234!", "fra@gmail.com");
                insertUser("Giuseppe", "Ignone", "seppu_97", "Password1234!", "giuse@gmail.com");
                insertUser("Daniele", "Camilleri", "black_mamba97", "Password1234!", "@dani@gmail.com");
                insertUser("Giovanna", "Petrone", "gio_spike", "Password1234!", "spike@gmail.com");
                insertUser("Matteo", "Baldoni", "matteo_baldoni", "Password1234!", "spike1@gmail.com");
                insertUser("Giancarlo", "Ruffo", "gruffo_99", "Password1234!", "spike2@gmail.com");
                insertUser("Francesco", "Bergadando", "fra_berga", "Password1234!", "spike3@gmail.com");
            }
        }


        private void insertUser(String name, String surname, String username, String password, String email) {
            User u1 = new User();
            u1.setUsername(username);
            u1.setName(name);
            u1.setSurname(surname);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            u1.setPassword(bCryptPasswordEncoder.encode(password));
            u1.setEmail(email);
            PersonalCard personalCard = new PersonalCard();
            personalCard.setBase64StringImage(RegisterController.defaultImageBase64);
            personalCard.setUser(u1);
            personalCard.setWallet(new Wallet());
            u1.setPersonalCard(personalCard);
            userJpaRepository.save(u1);
        }
    }
}


