package practice.guestregistry.data.mongo.daoimpl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import practice.guestregistry.data.api.dao.PersonDao;
import practice.guestregistry.data.mongo.entities.PersonEntity;
import practice.guestregistry.data.mongo.mappers.PersonDomainEntityMapper;
import practice.guestregistry.domain.Person;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonDaoImpl implements PersonDao {
    private MongoTemplate mongoTemplate;
    private PersonDomainEntityMapper mapper;

    @Autowired
    public PersonDaoImpl(MongoTemplate mongoTemplate, PersonDomainEntityMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public void deleteAll() {
        mongoTemplate.findAllAndRemove(Query.query(new Criteria().where("id").exists(true)), PersonEntity.class);
    }

    @Override
    public Optional<Person> findById(String id) {
        PersonEntity personDB = mongoTemplate.findById(new ObjectId(id), PersonEntity.class);
        return Optional.of(mapper.map(personDB));
    }

    @Override
    public List<Person> findAll() {
        return mongoTemplate.findAll(PersonEntity.class).
                stream().
                map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public Person add(Person person) {
        PersonEntity mappedPersonEntity = mapper.map(person);
        mappedPersonEntity.setId(ObjectId.get());
        return mapper.map(mongoTemplate.save(mappedPersonEntity));
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(new ObjectId(id))), PersonEntity.class);
    }

    @Override
    public boolean existById(String id) {
        return  mongoTemplate.exists(Query.query(Criteria.where("id").is(new ObjectId(id))), PersonEntity.class);
    }


    @Override
    public Person update(Person personDomain) {
        PersonEntity mappedPersonEntity = mapper.map(personDomain);
        return mapper.map(mongoTemplate.save(mappedPersonEntity));
    }

    @Override
    public boolean exist(Person personDomain) {
        return mongoTemplate.exists(Query.query(Criteria.byExample(mapper.map(personDomain))), PersonEntity.class);
    }


//    @PostConstruct
//    private void after() {
//        if (!mongoTemplate.collectionExists(Card.class)   ) {
//            sequenceDao.initCollection(HOSTING_SEQ_KEY);
//        }
//    }
}
