package train.trainproject2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "product")
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id", nullable = false)
    private int id;

    @Column (name = "product_name")
    private String productName;

    @Column (name = "product_code", nullable = false, unique = true)
    private int productCode;

    @Column (name = "product_avqnt")
    private int productAvQnt;

    @Column (name = "price", nullable = false)
    private double productPrice;

    @Column (name = "type")
    private String type;

    @Version
    @Column (name = "version")
    private Long version;

    /*Relazione inversa Product e ProductInCart:
    @OneToMany
    @JoinColumn (name = "product_id")
    private List <ProductInCart> productList = new ArrayList <> ();
    */
    

    
}