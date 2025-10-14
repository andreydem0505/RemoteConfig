package andreydem0505.remoteconfig.controllers;

import andreydem0505.remoteconfig.dto.DynPropertyDto;
import andreydem0505.remoteconfig.services.DynPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Urls.PROPERTIES_URL)
@RequiredArgsConstructor
public class DynPropertiesController {
    private final DynPropertiesService dynPropertiesService;

    @PostMapping
    public ResponseEntity<String> createDynProperty(@RequestBody DynPropertyDto data, Authentication authentication) {
        dynPropertiesService.createDynProperty(authentication.getName(), data.getName(), data.getData());
        return new ResponseEntity<>("Dyn property created", HttpStatus.CREATED);
    }
}
