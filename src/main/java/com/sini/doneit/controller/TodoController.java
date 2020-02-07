package com.sini.doneit.controller;


import com.sini.doneit.jwt.JwtTokenUtil;
import com.sini.doneit.model.ResponseMessage;
import com.sini.doneit.model.Todo;
import com.sini.doneit.model.User;
import com.sini.doneit.model.Wallet;
import com.sini.doneit.repository.ProposalJpaRepository;
import com.sini.doneit.repository.TodoJpaRepository;
import com.sini.doneit.repository.UserJpaRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.sini.doneit.repository.WalletJpaRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import static com.sini.doneit.model.MessageCode.*;

@RestController
@CrossOrigin("*")
public class TodoController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private WalletJpaRepository walletJpaRepository;

    @Autowired
    private ProposalJpaRepository proposalJpaRepository;

    @GetMapping("/my-todo-list")
    public List<Todo> getUserTodoList(@RequestHeader HttpHeaders headers) {
        String username = jwtTokenUtil.getUsernameFromToken((jwtTokenUtil.getTokenFromHeader(headers)));
        List<Todo> todoList = userJpaRepository.findByUsername(username).getTodoList();
        return todoList;
    }

    @GetMapping("my-todo-list/{state}")
    public List<Todo> getUserTodoListByState(@RequestHeader HttpHeaders headers, @PathVariable String state) {
        String username = jwtTokenUtil.getUsernameFromToken((jwtTokenUtil.getTokenFromHeader(headers)));
        User user = userJpaRepository.findByUsername(username);
        return this.todoJpaRepository.findByUserAndState(user, state);
    }

    @GetMapping("todo-list/users/{username}/state/{state}")
    public List<Todo> getUserTodoListByState(@PathVariable String username, @PathVariable String state) {
        User user = userJpaRepository.findByUsername(username);
        return this.todoJpaRepository.findByUserAndState(user, state);
    }

    @GetMapping("/get-todo/{todoId}")
    public Todo getTodoById(@RequestHeader HttpHeaders headers, @PathVariable Long todoId) {
        User user = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(headers));
        Optional<Todo> todo = todoJpaRepository.findById(todoId);
        if (todo.isPresent()) {
            if (user.isOwnerOfTodo(todo.get())) {
                System.out.println(todo.get());
                return todo.get();
            }
        }
        return null;
    }

    @GetMapping("profile-todo-list/users/{username}")
    public List<Todo> getProfileTodoList(@PathVariable String username){
        return this.todoJpaRepository.findProfileTodoList(username);
    }

    @GetMapping("/get-generic-todo/{todoId}")
    public Todo getTodoById(@PathVariable Long todoId) {
        return this.todoJpaRepository.findById(todoId).get();
    }

    @GetMapping(path = "/all-todo-list")
    public List<Todo> getAllTodo() {
        return todoJpaRepository.findAll();
    }

    @GetMapping(path = "/active-todo-list")
    public List<Todo> getActiveTodo(@RequestHeader HttpHeaders headers) {
        List<Todo> todoList = todoJpaRepository.findAllActiveTodo(new Date());
        return todoList;
    }

    @PostMapping("/create-todo")
    @Transactional
    public ResponseEntity<ResponseMessage> createTodo(@RequestBody Todo todo, @RequestHeader HttpHeaders headers) {
        String username = jwtTokenUtil.getUsernameFromHeader(headers);
        User user = userJpaRepository.findByUsername(username);
        Wallet userWallet = user.getPersonalCard().getWallet();
        if (userWallet.removeCfu(todo.getCategory().getCfuPrice())) {
            walletJpaRepository.save(userWallet);
            todo.setUser(user);
            todoJpaRepository.save(todo);

            return new ResponseEntity<>(new ResponseMessage("Todo creato correttamente", TODO_CREATED),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponseMessage("Cfu insufficienti", CFU_INSUFFICIENT),
                HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/delete-todo/{todoId}")
    public ResponseEntity<ResponseMessage> deleteTodo(@RequestHeader HttpHeaders headers, @PathVariable Long todoId) {
        User user = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(headers));
        Optional<Todo> todo = todoJpaRepository.findById(todoId);
        if (todo.isPresent()) {
            if (user.isOwnerOfTodo(todo.get())) {
                user.removeTodo(todo.get());
                todoJpaRepository.deleteById(todoId);
                return new ResponseEntity<>(new ResponseMessage("Todo eliminato con successo", TODO_DELETED),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("Errore nell'eliminazione del todo", ERROR_TODO_REMOVAL),
                HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update-todo")
    public ResponseEntity<ResponseMessage> updateTodo(@RequestBody Todo todo, @RequestHeader HttpHeaders headers) {
        User user = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(headers));
        Optional<Todo> todoDb = todoJpaRepository.findById(todo.getId());
        if (todoDb.isPresent()) {
            if (user.isOwnerOfTodo(todoDb.get())) {
                user.removeTodo(todo);
                user.addTodo(todo);
                todoJpaRepository.save(todo);

                return new ResponseEntity<>(new ResponseMessage("Todo modificato con successo", SUCCESS_TODO_MODIFIED),
                        HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new ResponseMessage("Errore nella modifica del todo", FAILED_TODO_MODIFY),
                HttpStatus.OK);
    }

    @GetMapping("todo/get-joined-todo")
    public List<Todo> getJoinedTodo(@RequestHeader HttpHeaders httpHeaders) {
        User user = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        List<Todo> todoList = proposalJpaRepository.getJoinedTodo(user);
        return todoList;
    }

}
