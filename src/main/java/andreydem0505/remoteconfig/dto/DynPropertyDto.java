package andreydem0505.remoteconfig.dto;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import lombok.Value;

@Value
public class DynPropertyDto {
    String name;
    PropertyType type;
    Object data;
}
