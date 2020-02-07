package com.sini.doneit.controller;

import com.sini.doneit.jwt.JwtTokenUtil;
import com.sini.doneit.model.PersonalCard;
import com.sini.doneit.model.ResponseMessage;
import com.sini.doneit.model.User;
import com.sini.doneit.model.Wallet;
import com.sini.doneit.repository.PersonalCardJpaRepository;
import com.sini.doneit.repository.UserJpaRepository;
import com.sini.doneit.services.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.sini.doneit.model.MessageCode.*;

@RestController
@CrossOrigin("*")
public class RegisterController {
    public static final String defaultImageBase64 = "data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAA8AAD/4QUgaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjAtYzA2MCA2MS4xMzQ3NzcsIDIwMTAvMDIvMTItMTc6MzI6MDAgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczpkYz0iaHR0cDovL3B1cmwub3JnL2RjL2VsZW1lbnRzLzEuMS8iIHhtcFJpZ2h0czpXZWJTdGF0ZW1lbnQ9Imh0dHA6Ly93d3cucHVibGljZG9tYWlucGljdHVyZXMubmV0IiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InV1aWQ6OUUzRTVDOUE4QzgxREIxMTg3MzREQjU4RkRERTRCQTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MzA3QjQ0MDc4OTdCMTFFMjg2REJENENGNEQwNTUzMjAiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MzA3QjQ0MDY4OTdCMTFFMjg2REJENENGNEQwNTUzMjAiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgSWxsdXN0cmF0b3IgQ1M0Ij4gPHhtcFJpZ2h0czpVc2FnZVRlcm1zPiA8cmRm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xV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxV2KuxVnn5E2f1v84fKUVK8dRimp/wAYay/8aYq/RjFXYq7FXYq7FXYq7FXYq7FXYqx38xdYOi+QfMWqhuL2WnXUsZ/y1hbh/wANTFX5m4q7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq7FXYq9W/5xdtPrP53+Xq9IfrUx/wBjaS0/E4q+/sVdirsVdirsVdirsVdirsVdirzH/nJe/wDqX5J+ZXBo00cFuP8Antcxof8AhScVfnzirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdir2v/AJxCt/V/OS2en9xY3cn3qE/43xV904q7FXYq7FXYq7FXYq7FXYq7FXi3/OXlwYvyauY/+Wi+tI/ucyf8aYq+FMVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVe9/8AOGMHP81r2TtFpFwf+CngX+OKvtnFXYq7FXYq7FXYq7FXYq7FXYq8H/5zMl4/lNap/vzVrcfdDOf4Yq+I8VdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVdirsVTXy95p8x+W7uS80DUrjS7uWMwyT2sjRO0ZIYqSpG1VBxVkH/K6vzc/6m/Vv+kuX+uKu/5XV+bn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";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PersonalCardJpaRepository personalCardJpaRepository;

    @PostMapping(path = "/register-user")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody User user) {
        ResponseMessage responseMessage = null;
        if (registerValidator.validateRegister(user)) {
            if (userAlreadyExists(user)) {
                responseMessage = new ResponseMessage("Username e/o mail già esistenti!", USER_ALREADY_CREATED);
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            encryptPassword(user);
            user.setPersonalCard(new PersonalCard().setUser(user).setBase64StringImage(defaultImageBase64).setWallet(
                    new Wallet()
            ));
            userJpaRepository.save(user);
            responseMessage = new ResponseMessage("Utente creato con successo", SUCCESSFUL_REGISTER);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
        System.out.println("credenziali non valide");
        responseMessage = new ResponseMessage("Errore nella creazione dell'utente", INVALID_DATA);
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    private void encryptPassword(User user) {
        String password = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(password));
    }


    @PostMapping("/complete-register")
    public ResponseEntity<ResponseMessage> completeRegister(@RequestHeader HttpHeaders httpHeaders,
                                                            @RequestBody PersonalCard personalCard) {
        User user = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        ResponseMessage responseMessage;
        if (user != null) {

            PersonalCard p = personalCardJpaRepository.findByUserId(user.getId());
            p.setUniversity(personalCard.getUniversity())
                    .setFaculty(personalCard.getFaculty())
                    .setBase64StringImage(personalCard.getBase64StringImage())
                    .setStatusDescription(personalCard.getStatusDescription())
                    .setTelephone(personalCard.getTelephone());
            if (checkIfComplete(personalCard)) {
                p.setDone(true);
                responseMessage = new ResponseMessage("Registrazione completata",
                        SUCCESSFUL_REGISTER);
            }
            else{
                responseMessage = new ResponseMessage("Registrazione incompelta",
                        INCOMPLETE_REGISTER);
            }
            personalCardJpaRepository.save(p);

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Errore nella registrazione",
                INVALID_DATA), HttpStatus.BAD_REQUEST);

    }

    private boolean userAlreadyExists(User user) {
        return userJpaRepository.findByUsername(user.getUsername()) != null ||
                userJpaRepository.findByEmail(user.getEmail()) != null;
    }

    private boolean checkIfComplete(PersonalCard personalCard) {
        return personalCard.getUniversity() != null && personalCard.getFaculty() != null &&
                !personalCard.getBase64StringImage().equals(defaultImageBase64);
    }


}