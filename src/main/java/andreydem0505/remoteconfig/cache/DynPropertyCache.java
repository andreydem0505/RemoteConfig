package andreydem0505.remoteconfig.cache;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DynPropertyCache {
    private PropertyType type;
    private Object data;
}
