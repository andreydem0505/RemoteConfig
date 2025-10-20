package andreydem0505.remoteconfig.data.documents;

import lombok.Value;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "properties")
@CompoundIndex(name = "username_propertyName_index", def = "{'username' : 1, 'propertyName' : 1}", unique = true)
@Value
public class DynProperty {
    String username;
    String propertyName;
    PropertyType type;
    Object data;
}
