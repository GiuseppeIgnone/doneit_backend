package com.sini.doneit.controller;

import com.sini.doneit.jwt.JwtTokenUtil;
import com.sini.doneit.model.*;
import com.sini.doneit.repository.FollowingJpaRepository;
import com.sini.doneit.repository.PersonalCardJpaRepository;
import com.sini.doneit.repository.UserJpaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.transaction.Transactional;

@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PersonalCardJpaRepository personalCardJpaRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private FollowingJpaRepository followingJpaRepository;

    @GetMapping("/my-personal-card")
    public PersonalCard getPersonalCard(@RequestHeader HttpHeaders httpHeaders) {
        Long id = userJpaRepository.getIdByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        return personalCardJpaRepository.findByUserId(id);
    }

    @GetMapping("/user-personal-card/{username}")

    public PersonalCard getUserPersonalCard(@PathVariable String username) {
        Long id = userJpaRepository.getIdByUsername(username);
        return personalCardJpaRepository.findByUserId(id);

    }

    @PostMapping("/user/follow-user")
    public ResponseEntity<ResponseMessage> followUser(@RequestHeader HttpHeaders httpHeaders, @RequestBody String username) {
        User userFrom = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        User userTo = userJpaRepository.findByUsername(username);

        if (username.equals(userFrom.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Errore nel seguire l'utente",
                    MessageCode.FOLLOWING_REQUEST_FAILED), HttpStatus.BAD_REQUEST);
        }
        Followers followers = new Followers(userFrom, userTo);


        followingJpaRepository.save(followers);
        return new ResponseEntity<>(new ResponseMessage("Hai iniziato a seguire l'utente con successo",
                MessageCode.SUCCESSFUL_REQUEST), HttpStatus.OK);
    }

    @GetMapping("/user/{username}/get-followers")
    public List<User> getUserFollower(@PathVariable String username) {

        User user = userJpaRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return followingJpaRepository.getUserFollowers(user.getId());
    }

    @GetMapping("/user/{username}/get-following")
    public List<User> getUserFollowing(@PathVariable String username) {

        User user = userJpaRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        List<User> userList = followingJpaRepository.getUserFollowing(user.getId());
        return userList;
    }

    @GetMapping("/user/{username}/get-image-profile")
    public String getUserImageProfile(@PathVariable String username) {
        User user = userJpaRepository.findByUsername(username);
        return personalCardJpaRepository.findByUserId(user.getId()).getBase64StringImage();
    }


    @GetMapping("/user/get-user-started-with/{string}")
    public List<String> getUserStartedWithString(@PathVariable String string) {
        if (string.length() < 2) {
            return null;
        }
        List<String> users = userJpaRepository.getUserStartedWithString(string);
        return users;
    }

    //ritorna tutti gli utenti e le rispettive personalCards
    @GetMapping("/user/get-all-users")
    public List<PersonalCard> getAllUsers() {
        return personalCardJpaRepository.findAll();
    }

    @PostMapping("/user/get-users-images")
    public List<String> getUsersImages(@RequestBody List<Long> users) {
        System.out.println(users);
        return personalCardJpaRepository.getUsersImage(users);
    }


}
