package andreydem0505.remoteconfig.controllers;

import andreydem0505.remoteconfig.dto.DynPropertyDto;
import andreydem0505.remoteconfig.dto.FeatureFlagHitRequestDto;
import andreydem0505.remoteconfig.services.DynPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Urls.PROPERTIES_URL)
@RequiredArgsConstructor
public class DynPropertiesController {
    private final DynPropertyService dynPropertyService;

    @PostMapping
    public ResponseEntity<String> createDynProperty(@RequestBody DynPropertyDto data, Authentication authentication) {
        dynPropertyService.createDynProperty(
                authentication.getName(),
                data.getName(),
                data.getType(),
                data.getData()
        );
        return new ResponseEntity<>("Dynamic property created", HttpStatus.CREATED);
    }

    @GetMapping("/{propertyName}")
    public ResponseEntity<?> getDynPropertyData(@PathVariable String propertyName, Authentication authentication) {
        Object result = dynPropertyService.getDynPropertyData(authentication.getName(), propertyName);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/check/{featureFlag}")
    public boolean checkFeatureFlagHit(
            @PathVariable String featureFlag,
            @RequestBody(required = false) FeatureFlagHitRequestDto dto,
            Authentication authentication
    ) {
        return dynPropertyService.checkHit(
                authentication.getName(),
                featureFlag,
                dto == null ? null : dto.getContext()
        );
    }
}
