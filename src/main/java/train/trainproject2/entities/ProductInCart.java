package train.trainproject2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "productInCart")
public class ProductInCart {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id")
    private Integer id;

    @Column (name = "prod_in_cart_qnt")
    private int inCartQnt;

    //Relazione ProductInCart e Product:
    @ManyToOne
    @JoinColumn (name = "product_id")
    private Product p;

    //Relazione inversa ProductInCart e Utente:
    @ManyToOne
    @JsonIgnore
    //@ToString.Exclude
    @JoinColumn (name = "user_id")
    private Users u;
}
