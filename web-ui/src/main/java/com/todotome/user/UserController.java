package com.todotome.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

@RestController
public class UserController {

    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
    })
    @PreAuthorize("hasRole('ROLE_TRUSTED_CLIENT')")
    @RequestMapping(method = RequestMethod.GET, value = "/user", produces = APPLICATION_JSON_VALUE)
    public User getUser() {
        System.out.println("INSIDE /user");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(null == auth) {
            System.out.println("AUTH is NULL");
        } else {
            System.out.println("NAME: " + auth.getName());
        }
        User aUser = UserRepository.fakeUserRepository.get(auth.getName());

        if(auth != null && aUser != null) {
            return aUser;
        } else {

            throw new IllegalArgumentException("error.username");
        }
    }*/

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
/*
    @RequestMapping(value="principal", method=RequestMethod.GET)
    public Object getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal;
    }

    @RequestMapping(value="roles", method=RequestMethod.GET)
    public Object getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }*/
}