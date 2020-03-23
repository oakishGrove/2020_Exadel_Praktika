package practice.guestregistry.controllers.api;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.guestregistry.data.api.domain.Location;
import practice.guestregistry.services.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "http://localhost:4200")
@Api
public class LocationController {

    LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(path="{id}")
    public Location getLocation(@PathVariable String id) {
        return locationService.getLocationById(id);
    }

    @GetMapping
    public List<Location> getLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location savedLocation = locationService.saveLocation(location);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(savedLocation.getId().toString())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(path="{id}")
//    public ResponseEntity<Location> updateLocation(@PathVariable("id") ObjectId id, @Valid @RequestBody Location newLocation) {
    public ResponseEntity<Location> updateLocation(@RequestBody Location newLocation) {
        locationService.updateLocation(newLocation);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path="{id}")
    public void deleteLocation(@PathVariable String id) {
        locationService.deleteLocationById(id);
//        return new ResponseEntity<>(HttpStatus.OK);
    }

}
