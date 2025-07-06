package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountService accountService;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            return null;
        }
        if(message.getMessageText().length() > 255) {
            return null;
        }          
        if (message.getPostedBy() == null || !accountService.accountExistsById(message.getPostedBy())) {
            return null;
        }        
        return messageRepository.save(message);
    }    
    
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }
    
    public int deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1; //one record deleted
        }
        return 0;//record not found
    } 
    
    public int updateMessage(Integer messageId, String newMessageText) {
        // check valid new text
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return 0;
        }
        if (newMessageText.length() > 255) {
            return 0;
        }        
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return 1; //one record updated            
        }
        return 0; //record not found
    }
    
    public List<Message> getMessagesByUserId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
