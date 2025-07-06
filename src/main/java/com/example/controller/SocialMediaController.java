package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        Account savedAccount = accountService.registerAccount(account);
        
        if (savedAccount == null) {            
            if (account.getUsername() != null && accountService.usernameExists(account.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
            } else {                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
            }
        }
        
        return ResponseEntity.ok(savedAccount); // 200
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        Account loggedInAccount = accountService.loginAccount(account);  

        if (loggedInAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        return ResponseEntity.ok(loggedInAccount); // 200
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message savedMessage = messageService.createMessage(message);
        
        if (savedMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        
        return ResponseEntity.ok(savedMessage); // 200
    }
    
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages); // 200
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        
        if (message == null) {
            return ResponseEntity.ok().build(); // 200 with empty body
        }
        
        return ResponseEntity.ok(message); // 200
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int deletedRows = messageService.deleteMessageById(messageId);

        if (deletedRows == 0) {
            return ResponseEntity.ok().build(); // 200 with empty body
        }
        return ResponseEntity.ok(deletedRows); // 200
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        int updatedRows = messageService.updateMessage(messageId, message.getMessageText());
        
        if (updatedRows == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        
        return ResponseEntity.ok(updatedRows); // 200
    }
    
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUserId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        return ResponseEntity.ok(messages); // 200
    }
}





