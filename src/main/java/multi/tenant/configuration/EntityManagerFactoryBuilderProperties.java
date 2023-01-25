package multi.tenant.configuration;

import lombok.Data;

@Data
public class EntityManagerFactoryBuilderProperties {

	private String dialect;

	private String ddl_auto;

}
