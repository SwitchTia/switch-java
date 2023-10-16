package train.trainproject2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import train.trainproject2.UTILITIES.Exceptions.DataNotCorrectException;
import train.trainproject2.UTILITIES.Exceptions.IncorrectProductQuantityException;
import train.trainproject2.UTILITIES.Exceptions.ProductDoesNotExistException;
import train.trainproject2.UTILITIES.Exceptions.ThisShoppingCartIsEmptyException;
import train.trainproject2.UTILITIES.Exceptions.UserAlreadyExistsException;
import train.trainproject2.UTILITIES.Exceptions.UserDoesNotExistException;
import train.trainproject2.UTILITIES.configurations.AuthenticationRequest;
import train.trainproject2.UTILITIES.configurations.AuthenticationResponse;
import train.trainproject2.UTILITIES.configurations.RegisterRequest;
import train.trainproject2.entities.Users;
import train.trainproject2.entities.Product;
import train.trainproject2.entities.ProductInCart;
import train.trainproject2.entities.Role;
import train.trainproject2.repositories.UserRepository;
import train.trainproject2.repositories.ProductInCartRepository;
import train.trainproject2.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository ur;

    @Autowired
    ProductRepository pr;

    @Autowired
    ProductInCartRepository pcr;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public boolean isValidEmail (String userEmail) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        return userEmail.matches(regex);
    }

    public AuthenticationResponse registerAdmin (RegisterRequest regRequest){
        Users u = new Users();
        u.setFirstname(regRequest.getFirstname().toUpperCase());
        u.setLastname (regRequest.getLastname().toUpperCase());
        u.setEmail(regRequest.getEmail().toLowerCase());
        u.setPassword (passwordEncoder.encode(regRequest.getPassword()));
        u.setRole(Role.ADMIN);
        if (ur.existsByEmail(u.getEmail())){
            throw new UserAlreadyExistsException();
        }
        else {
            ur.save(u);
            var jwtToken = jwtService.generateToken(u);
            return new AuthenticationResponse(jwtToken);
        } 
    }

    public AuthenticationResponse registerUser (RegisterRequest regRequest){
        Users u = new Users();
        u.setFirstname(regRequest.getFirstname().toUpperCase());
        u.setLastname (regRequest.getLastname().toUpperCase());
        u.setEmail(regRequest.getEmail().toLowerCase());
        u.setPassword (passwordEncoder.encode(regRequest.getPassword()));
        u.setRole(Role.USER);
        if (ur.existsByEmail(u.getEmail())){
            throw new UserAlreadyExistsException();
        }
        else {
            ur.save(u);
            var jwtToken = jwtService.generateToken(u);
            return new AuthenticationResponse(jwtToken);
        } 
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) throws RuntimeException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            authRequest.getEmail().toLowerCase(), 
            authRequest.getPassword()));
        Users u = ur.findByEmail(authRequest.getEmail().toLowerCase());
        var jwtToken = jwtService.generateToken(u);

        return new AuthenticationResponse (jwtToken);
    }

    public String deleteUser (String email) throws RuntimeException{
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        Users u = ur.findByEmail(email);
        ur.delete(u);
        String del = "The user has been deleted";
        return del;
    }

    public Users getUser (String email) throws RuntimeException {
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        Users u = ur.findByEmail(email);
        return (u);
    }

    public Users modifyUser (String email, String firstname, String lastname) throws RuntimeException {
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        Users u = ur.findByEmail(email);
        if (u == null){
            throw new UserDoesNotExistException ();
        }
        u.setFirstname (firstname);
        u.setLastname (lastname);
        u.setEmail(email);
        ur.save (u);
        return u;
    }

    public Users modifyUserEmail (String email, String newEmail){
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        Users u = ur.findByEmail(email);
        if (u == null){
            throw new UserDoesNotExistException();
        }
        u.setEmail(newEmail);
        ur.save (u);
        return u;
    }

    public Users modifyUserPassword (String email, String password, String newPassword){
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        Users u = ur.findByEmail(email);
        if (u == null){
            throw new UserDoesNotExistException();
        }
        u.setPassword(newPassword);
        ur.save (u);
        return u;
    }

    public Users addProdInCart (String email, Integer productCode, int inCartQnt) throws RuntimeException {
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        Users u = ur.findByEmail(email);
        Product p = pr.findByProductCode(productCode);
        ProductInCart pInCart = pcr.findByUAndP (u, p);
        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (p == null){
            throw new ProductDoesNotExistException();
        }
        if (pInCart != null) {
            if (p.getProductAvQnt() <= 0){
                    throw new IncorrectProductQuantityException ();
            }
            if (p.getProductAvQnt() > 0) {
                if (u.getProductList().contains (pInCart)) {
                    pInCart.setInCartQnt (pInCart.getInCartQnt() + inCartQnt);
                    
                    if (p.getProductAvQnt() < pInCart.getInCartQnt()){
                        throw new IncorrectProductQuantityException ();
                    }
                    pInCart = pcr.save (pInCart);
                }    
            }
        }
        else {
            pInCart = new ProductInCart(null, inCartQnt, p, u);
            pInCart = pcr.save (pInCart);
            u.getProductList().add(pInCart);
        }
        return u = ur.save(u);
    }

    public Users decreaseQntProdInCart (String email, Integer productCode, int inCartQnt) throws RuntimeException {
        Users u = ur.findByEmail(email);
        Product p = pr.findByProductCode(productCode);
        ProductInCart pInCart = pcr.findByUAndP (u, p);

        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (p == null){
            throw new ProductDoesNotExistException();
        }
        if (pInCart != null) {
            if (p.getProductAvQnt() > 0) {
                if (u.getProductList().contains (pInCart)) {
                    pInCart.setInCartQnt (pInCart.getInCartQnt() - inCartQnt);
                    pInCart = pcr.save (pInCart);
                    ur.save(u);

                    if (pInCart.getInCartQnt() == 0){
                        u.getProductList().remove(pInCart);
                        ur.save(u);
                    }
                    if (pInCart.getInCartQnt() < 0){
                        throw new IncorrectProductQuantityException ();
                    }
                }    
            }
        }
        return u = ur.save(u);
    }

    @Transactional
    public Users purchasedProd (String email, Integer productCode, int purchasedQnt) throws RuntimeException{
        Users u = ur.findByEmail(email);
        Product p = pr.findByProductCode(productCode);
        u.setPurchased(true);
        u = ur.save(u);
        
        p = pr.findByProductCode(productCode);
        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (p == null){
            throw new ProductDoesNotExistException();
        }
        if (p.getProductAvQnt() <= 0){
                    throw new IncorrectProductQuantityException ();
            }
        if (p.getProductAvQnt() == purchasedQnt) {
            p.setProductAvQnt(0);
            p = pr.save (p);
            u.getPurchasedList().add(p);
        }
        if (p.getProductAvQnt() > purchasedQnt) {
            if (u.getPurchasedList().contains (p)) {
                p.setProductAvQnt(p.getProductAvQnt() - purchasedQnt);
                if (p.getProductAvQnt() < purchasedQnt){
                        throw new IncorrectProductQuantityException ();
                }
                p = pr.save (p);
                u.getPurchasedList().add(p);
            }    
            else {
                p.setProductAvQnt(p.getProductAvQnt() - purchasedQnt);
                if (p.getProductAvQnt() < purchasedQnt){
                    throw new IncorrectProductQuantityException ();
                }
                p = pr.save(p);
                u.getPurchasedList().add(p);
            }
        }
        return u = ur.save(u);
    }

    public Users deleteProdInCart (String email, Integer productCode) throws RuntimeException {
        Users u = ur.findByEmail(email);
        Product p = pr.findByProductCode(productCode);
        ProductInCart pInCart = pcr.findByUAndP (u, p);

        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (p == null){
            throw new ProductDoesNotExistException();
        }
        if (pInCart == null){
            throw new ProductDoesNotExistException ();
        }
        if (pInCart != null) {
            pcr.delete(pInCart);
        }
        return ur.save (u);
    }


    public Users emptyTheCart (String email) throws RuntimeException {
        Users u = ur.findByEmail(email);

        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (u.getProductList().isEmpty()) {
            throw new ThisShoppingCartIsEmptyException ();
        }
        else {
            u.getProductList().clear();
        }
        return ur.save (u);
    } 
}
