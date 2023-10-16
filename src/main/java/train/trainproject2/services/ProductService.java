package train.trainproject2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import train.trainproject2.UTILITIES.Exceptions.DataNotCorrectException;
import train.trainproject2.UTILITIES.Exceptions.ProductAlreadyExistsException;
import train.trainproject2.UTILITIES.Exceptions.ProductDoesNotExistException;
import train.trainproject2.entities.Product;
import train.trainproject2.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository pr;


    public Product saveProduct (Product p) throws RuntimeException {
        if (pr.existsByProductCode(p.getProductCode()))  {
            throw new ProductAlreadyExistsException ();
        }
        return pr.save (p);
    }

    public String deleteProduct (int productCode) throws RuntimeException {
        Product p = pr.findByProductCode (productCode);
        if (p == null) {
            throw new DataNotCorrectException();
        }
        pr.delete (p);
        String del = "The product has been deleted";
        return del;
    }

    public Product getProduct (int productCode) {
        //if (!isValidProductCode) controlli con regex
        return pr.findByProductCode(productCode);
    }

    public Product modifyProduct (String productName, int productCode, int productQnt, double productPrice, String productType) throws RuntimeException {
        Product p = pr.findByProductCode(productCode);
        if (p == null){
            throw new ProductDoesNotExistException();
        }
        p.setProductName (productName);
        p.setProductAvQnt (productQnt);
        p.setProductPrice (productPrice);
        p.setProductType (productType);
        pr.save (p);
        return p;
    }

    public Product modifyProductCode (int productCode, int newProductCode) {
        Product p = pr.findByProductCode(productCode);
        if (p == null){
            throw new ProductDoesNotExistException();
        }
        p.setProductCode(newProductCode);
        pr.save (p);
        return p;
    }

    public Page<Product> findByType (String productType, int pageNr, int pageSize, String sortDirection) {
        Sort sort;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Order.desc("productPrice"));
        } else {
            sort = Sort.by(Sort.Order.asc("productPrice"));
        }
        PageRequest pageReq = PageRequest.of(pageNr, pageSize, sort);
        return pr.findByProductType (productType, pageReq);
    }
    
} 