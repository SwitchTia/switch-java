package train.trainproject2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import train.trainproject2.entities.Users;
import train.trainproject2.entities.Product;
import train.trainproject2.entities.ProductInCart;

public interface ProductInCartRepository extends JpaRepository <ProductInCart, Integer>{
    ProductInCart findByUAndP (Users u, Product p);
    
}
