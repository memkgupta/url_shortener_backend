package com.url_shortner.shortner.controllers;

import com.url_shortner.shortner.services.URLService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authorisation")
public class AuthorisationController {
    private final URLService urlService;

    @GetMapping("/authorise")
    public ResponseEntity<?> authoriseURL(@RequestParam(name = "userId") String userId, @RequestParam(name = "urlId") String urlId, @RequestHeader("Authorization") String auth) {
        if (!auth.equals("Bearer SERVICE_AUTH")) {
            // we can do more efficient authentication but i just want to complete my project
            System.out.println("Maa ki chuuu");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        System.out.println(userId + " " + urlId + " " + auth);
        try {
            if (urlService.authoriseURLOwner(urlId, userId)) {
                return ResponseEntity.ok().build();
            }
            System.out.println("Maa ki chuuu");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
