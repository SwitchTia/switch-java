package train.trainproject2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import train.trainproject2.entities.Product;
import train.trainproject2.repositories.ProductRepository;
import train.trainproject2.services.ProductService;

@RestController
@RequestMapping ("/product")
public class ProductController {
    @Autowired
    ProductRepository pr;

    @Autowired
    ProductService ps;
    

    @PostMapping ("/saveProduct")
    @PreAuthorize ("hasAuthority ('ADMIN')")
    public ResponseEntity saveProduct (@RequestBody Product p){
        try {
            return new ResponseEntity (ps.saveProduct(p), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping ("/deleteProduct")
    @PreAuthorize ("hasAuthority ('ADMIN')")
    public ResponseEntity deleteProduct (@RequestParam ("productCode") int productCode ) {
        try {
            return new ResponseEntity (ps.deleteProduct(productCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getAllProducts")
    @PreAuthorize ("hasAuthority ('ADMIN')")
    public ResponseEntity getAllProducts () {
        return new ResponseEntity (pr.findAll(), HttpStatus.OK);
    }

    @GetMapping ("/getProduct")
    @PreAuthorize ("hasAnyAuthority ('USER','ADMIN')")
    public ResponseEntity getProduct (@RequestParam ("productCode") int productCode) {
        try {
            return new ResponseEntity (ps.getProduct(productCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/modifyProduct")
    @PreAuthorize ("hasAuthority ('ADMIN')")
    public ResponseEntity modifyProduct (@RequestParam ("productName") String productName, @RequestParam ("productCode") int productCode, @RequestParam ("productAvQnt") int productAvQnt, @RequestParam ("productPrice") double productPrice) {
        try {
            return new ResponseEntity (ps.modifyProduct (productName, productCode, productAvQnt, productPrice), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/modifyProductCode")
    @PreAuthorize ("hasAuthority ('ADMIN')")
    public ResponseEntity modifyProductCode (@RequestParam ("productCode") int productCode, @RequestParam ("newProductCode") int newProductCode){
        try {
            return new ResponseEntity (ps.modifyProductCode (productCode, newProductCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/findByType")
    @PreAuthorize ("hasAnyAuthority ('USER','ADMIN')")
    public Page <Product> findByType (@RequestParam ("type") String type, @RequestParam ("pageNr") int pageNr, @RequestParam ("pageSize") int pageSize, @RequestParam ("sortDirection") String sortDirection){
        return ps.findByType (type, pageNr, pageSize, sortDirection);
    }
    
}
 