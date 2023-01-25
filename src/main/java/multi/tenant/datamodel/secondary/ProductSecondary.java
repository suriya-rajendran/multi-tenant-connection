package multi.tenant.datamodel.secondary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="product_secondary")
@Entity
public class ProductSecondary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4110293803187542104L;

	@Id
	private Long Id;
	
	private String code;
	
	private String name;

}
