package com.example.MyProject.rest.controller;


import com.example.MyProject.entity.Token;
import com.example.MyProject.exceptions.InvalidLoginException;
import com.example.MyProject.exceptions.InvalidSessionTException;
import com.example.MyProject.repository.UserRepository;
import com.example.MyProject.rest.ClientSession;
import com.example.MyProject.rest.UserSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LoginController {

    private static final int LENGTH_TOKEN = 15;
    private Map<String, ClientSession> tokensMap;
    private UserSystem userSystem;

    @Autowired
    public LoginController(@Qualifier("tokens") Map<String, ClientSession> tokensMap, UserSystem userSystem,
                           UserRepository userRepository, ApplicationContext applicationContext) {
        this.tokensMap = tokensMap;
        this.userSystem = userSystem;
    }
    @PostMapping("/login/{email}/{password}")
    public ResponseEntity<Token> login(@PathVariable String email, @PathVariable String password)
            throws InvalidLoginException, InvalidSessionTException {

            ClientSession clientSession = userSystem.createClientSession(email, password);
            String token = generateToken();
            tokensMap.put(token, clientSession);
            Token myToken = new Token();
            myToken.setToken(token);
            return ResponseEntity.ok(myToken);
    }
    @GetMapping("/getAccount/{token}")
    public ResponseEntity<Integer> getAccount(@PathVariable String token){
        ClientSession clientSession = tokensMap.get(token);
        return  ResponseEntity.ok(clientSession.getRole());
    }

    private static String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-","")
                .substring(0, LENGTH_TOKEN);
    }
}
