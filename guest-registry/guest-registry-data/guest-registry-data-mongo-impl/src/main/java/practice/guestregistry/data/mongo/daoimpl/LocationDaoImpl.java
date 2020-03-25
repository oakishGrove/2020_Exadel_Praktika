package practice.guestregistry.data.mongo.daoimpl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import practice.guestregistry.data.api.dao.LocationDao;
import practice.guestregistry.data.mongo.entities.LocationEntity;
import practice.guestregistry.data.mongo.mappers.LocationMapper;
import practice.guestregistry.domain.Location;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LocationDaoImpl implements LocationDao {

    private final MongoTemplate mongoTemplate;
    private final LocationMapper mapper;

    @Autowired
    public LocationDaoImpl(MongoTemplate mongoTemplate, LocationMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public Location findById (String id) {//TODO throw null exception
        LocationEntity locationEntity = mongoTemplate.findById(new ObjectId(id), LocationEntity.class);
        return mapper.map(locationEntity);
    }

    @Override
    public List<Location> findAll () {
        List<LocationEntity> list = mongoTemplate.findAll(LocationEntity.class);
        System.out.println(list);
        return mongoTemplate.findAll(LocationEntity.class)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void add (Location location) {
        LocationEntity locationEntity = mapper.map(location);
        locationEntity.setId(ObjectId.get());
        mongoTemplate.save(locationEntity);
        location.setId(locationEntity.getId().toString());
    }

    @Override
    public void update (Location location) {
        LocationEntity locationEntity = mapper.map(location);
        mongoTemplate.save(locationEntity);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        mongoTemplate.remove(query, LocationEntity.class);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("id").exists(true)), LocationEntity.class);
    }

    @Override
    public boolean existById(String id) {
        return mongoTemplate.exists(Query.query(Criteria.where("id").is(new ObjectId(id))), LocationEntity.class);
    }

    @Override
    public boolean exist(Location location) {
        return mongoTemplate.exists(Query.query(Criteria.byExample(mapper.map(location))), LocationEntity.class);
    }
}
