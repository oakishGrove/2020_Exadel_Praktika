package practice.guestregistry.services_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import practice.guestregistry.models.Location;
import practice.guestregistry.services.LocationService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static practice.guestregistry.models.LocationType.OFFICE;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureDataMongo
@ActiveProfiles("test")
public class LocationServiceTests {
    @Autowired
    private LocationService locationService;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }

    @Before
    public void cleanUp() {
        System.out.println("BEFORE WAS SUMMONED");
        for (String name : mongoTemplate.getCollectionNames()) {
            System.out.println("DELETING POOR THING" + name);
            mongoTemplate.dropCollection(name);
        }
    }

    @Test
    public void testGetAllLocations() {
        for (Location name : mongoTemplate.findAll(Location.class)) {
            System.out.println(name.toString());
        }
        Location location1 = new Location();
        location1.setName("A");
        location1.setCountry("Lietuva");
        location1.setCity("Vilnius");
        location1.setAddress("Zalgirio 90");
        location1.setLocationType(OFFICE);
        location1.setPhoneNumber("851212345");
        Location location2 = new Location();
        location2.setName("B");
        location2.setCountry("Lietuva");
        location2.setCity("Kaunas");
        location2.setAddress("Geliu 5");
        location2.setLocationType(OFFICE);
        location2.setPhoneNumber("851122222");

        locationService.addLocation(location1);
        locationService.addLocation(location2);

        List<Location> locList = locationService.getAllLocations();

        for (Location name : locList) {
            System.out.println(name.toString());
        }
//        System.out.println(locList);

        assertEquals(2, locList.size());
    }
}