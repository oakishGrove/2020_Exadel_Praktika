package practice.guestregistry.data.mongo.daoimpl;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import practice.guestregistry.data.api.dao.EventDao;
import practice.guestregistry.data.mongo.entities.CardEntity;
import practice.guestregistry.data.mongo.entities.EventEntity;
import practice.guestregistry.data.mongo.mappers.EventMapper;
import practice.guestregistry.domain.Event;
import practice.guestregistry.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventDaoImpl implements EventDao {

    private final MongoTemplate mongoTemplate;
    private final EventMapper eventMapper;
    private static final Logger log = LoggerFactory.getLogger(LocationDaoImpl.class);

    @Autowired
    public EventDaoImpl(MongoTemplate mongoTemplate, EventMapper eventMapper) {
        this.mongoTemplate = mongoTemplate;
        this.eventMapper = eventMapper;
    }

    @Override
    public Event findById (String id) {
        EventEntity eventEntity = mongoTemplate.findById(id, EventEntity.class);
        return eventMapper.entityToDomain(eventEntity);
    }

    @Override
    public List<Event> findAll () {
        return mongoTemplate.findAll(EventEntity.class).stream().map(eventMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void add (Event event) {
        EventEntity eventEntity = eventMapper.domainToEntity(event);
        eventEntity.setId(ObjectId.get());
        mongoTemplate.save(eventEntity);
        event.setId(eventEntity.getId().toString());
    }

    @Override
    public void update (Event event) {
        EventEntity eventEntity = eventMapper.domainToEntity(event);
        if (mongoTemplate.exists(Query.query(Criteria.where("id").exists(true)), EventEntity.class)) {
            mongoTemplate.save(eventEntity);
        }
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, EventEntity.class);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("id").exists(true)), EventEntity.class);
    }

    @Override
    public boolean existById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, EventEntity.class);
    }

    @Override
    public boolean exist(Event event) {
        EventEntity eventEntity = eventMapper.domainToEntity(event);
        return mongoTemplate.exists(Query.query(Criteria.byExample(eventEntity)), EventEntity.class);
    }
}
