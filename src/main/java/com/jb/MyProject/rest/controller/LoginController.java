package com.jb.MyProject.rest.controller;

import com.jb.MyProject.entity.ClientSession;
import com.jb.MyProject.entity.Token;
import com.jb.MyProject.exceptions.InvalidLoginException;
import com.jb.MyProject.exceptions.InvalidSessionTException;
import com.jb.MyProject.rest.UserSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/")
public class LoginController {
    public static final int LENGTH_TOKEN = 15;
    private Map<String, ClientSession> tokensMap;
    private UserSystem userSystem;

    @Autowired
    public LoginController(@Qualifier("tokens") Map<String, ClientSession> tokensMap, UserSystem userSystem) {
        this.tokensMap = tokensMap;
        this.userSystem = userSystem;
    }

    @PostMapping("login/{email}/{password}")
    public ResponseEntity<Token> login(@PathVariable String email, @PathVariable String password)
            throws InvalidLoginException, InvalidSessionTException {
        ClientSession clientSession = userSystem.createClientSession(email, password);
        String token = generateToken();
        tokensMap.put(token, clientSession);
        Token myToken = new Token();
        myToken.setToken(token);
        return ResponseEntity.ok(myToken);
    }

    @GetMapping("role/{email}/{password}")
    public ResponseEntity<String> role(@PathVariable String email, @PathVariable String password)
            throws InvalidLoginException, InvalidSessionTException {
        ClientSession clientSession = userSystem.createClientSession(email, password);
        return ResponseEntity.ok(String.valueOf(clientSession.getRole()));
    }

    private static String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, LENGTH_TOKEN);
    }
}
