package train.trainproject2.controllers;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import train.trainproject2.UTILITIES.configurations.AuthenticationRequest;
import train.trainproject2.UTILITIES.configurations.RegisterRequest;
import train.trainproject2.entities.Users;
import train.trainproject2.repositories.UserRepository;
import train.trainproject2.services.JwtService;
import train.trainproject2.services.UserService;

@RestController
@RequestMapping ("/users")
public class UserController {

    @Autowired
    UserService us;

    @Autowired
    UserRepository ur;

    @Autowired
    JwtService jwtService;

    @PostMapping ("/registerAdmin")
    public ResponseEntity registerAdmin (@RequestBody RegisterRequest regRequest){
        try {
            return new ResponseEntity (us.registerAdmin(regRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/registerUser")
    public ResponseEntity registerUser (@RequestBody RegisterRequest regRequest){
        try {
            return new ResponseEntity (us.registerUser(regRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/authenticate")
    public ResponseEntity authenticateUser (@RequestBody AuthenticationRequest authRequest){
        try {
            return new ResponseEntity (us.authenticate(authRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/deleteUser")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser (@RequestParam ("email") String email) {
        try {
            return new ResponseEntity (us.deleteUser(email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping ("/getUser")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity getUser (@RequestParam ("email") String email) {
        try {
            return new ResponseEntity (us.getUser (email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getAllUsers")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity getAllUsers () {
        return new ResponseEntity (ur.findAll(), HttpStatus.OK);
    }

    @PutMapping ("/modifyUser")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity modifyUser (@RequestParam ("email") String email, @RequestParam ("firstname") String firstname, @RequestParam ("lastname") String lastname){
        try {
            return new ResponseEntity (us.modifyUser (email, firstname, lastname), HttpStatus.OK);
        } catch (RuntimeException e) {
             String ex = e.getClass().getSimpleName();
             return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/modifyUserEmail")
    @PreAuthorize ("hasAuthority('ADMIN')")
    public ResponseEntity modifyUserEmail (@RequestParam ("email") String email, @RequestParam ("newEmail") String newEmail){
        try {
            return new ResponseEntity (us.modifyUserEmail (email, newEmail), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
             return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/modifyUserPassword")
    @PreAuthorize ("hasAuthority('USER')")
    public ResponseEntity modifyUserPassword (HttpServletRequest servletRequest, @RequestParam ("password") String password, @RequestParam ("newPassword") String newPassword){
        try {
            String email =jwtService.extractEmailFromRequest(servletRequest);
            return new ResponseEntity (us.modifyUserPassword (email, password, newPassword), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
             return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/addProdInCart")
    @PreAuthorize ("hasAuthority('USER')")
    public ResponseEntity addProdInCart (HttpServletRequest servletRequest, @RequestParam ("productCode")int productCode, @RequestParam ("inCartQnt")int inCartQnt){
        try {
            String email =jwtService.extractEmailFromRequest(servletRequest);
            return new ResponseEntity (us.addProdInCart(email, productCode, inCartQnt), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/decreaseQntProdInCart")
    @PreAuthorize ("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity decreaseQntProdInCart (@RequestParam ("email") String email, @RequestParam ("productCode")int productCode, @RequestParam ("inCartQnt")int inCartQnt){
            try {
            return new ResponseEntity (us.decreaseQntProdInCart(email, productCode, inCartQnt), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/deleteProdInCart")
    @PreAuthorize ("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity deleteProdInCart (@RequestParam ("email") String email, @RequestParam ("productCode")int productCode){
        try {
            return new ResponseEntity (us.deleteProdInCart(email, productCode ), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/purchasedProd")
    @PreAuthorize ("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity purchasedProd(@RequestParam ("email") String email, @RequestParam ("productCode") Integer productCode, @RequestParam ("purchasedQnt")int purchasedQnt) {
        try {
            return new ResponseEntity (us.purchasedProd (email, productCode, purchasedQnt), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/emptyTheCart")
    @PreAuthorize ("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity emptyTheCart (@RequestParam ("email") String email){
            try {
            return new ResponseEntity (us.emptyTheCart(email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    } 

}
