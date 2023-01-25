package multi.tenant.datamodel.primary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="product_primary")
@Entity
public class ProductPrimary implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2799071896482398325L;
	
	@Id
	private Long Id;
	
	private String code;
	
	private String name;

}
