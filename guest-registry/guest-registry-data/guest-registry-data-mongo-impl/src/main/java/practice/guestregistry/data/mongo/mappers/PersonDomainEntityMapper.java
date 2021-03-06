package practice.guestregistry.data.mongo.mappers;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.guestregistry.data.mongo.entities.PersonEntity;
import practice.guestregistry.domain.Person;

@Component
public class PersonDomainEntityMapper {
    private static MapperFactory mapperFactory;

    @Autowired
    public PersonDomainEntityMapper() {

        this.mapperFactory = new DefaultMapperFactory.Builder().useAutoMapping(false).build();
        this.mapperFactory.classMap(PersonEntity.class, Person.class)
                .exclude("id")
                .byDefault()
                .customize(new CustomMapper<PersonEntity, Person>() {
                    @Override
                    public void mapBtoA(Person personDomain, PersonEntity personEntity, MappingContext context) {
                        if (personDomain.getId() != null && ObjectId.isValid(personDomain.getId())) {
                            personEntity.setId(new ObjectId(personDomain.getId()));
                        } else {
                            personEntity.setId(null);
                        }
                    }
                    @Override
                    public void mapAtoB(PersonEntity personEntity, Person person, MappingContext context) {
                        person.setId(personEntity.getId().toHexString());
                    }
                })
                .register();
    }

    public Person map (PersonEntity personEntity) {
        return this.mapperFactory.getMapperFacade(PersonEntity.class, Person.class).map(personEntity);
    }

    public PersonEntity map (Person personDomain) {
        return this.mapperFactory.getMapperFacade(PersonEntity.class, Person.class).mapReverse(personDomain);
    }
//    public void map (PersonDomain workerDTO, Person person) {
//        this.mapperFactory.getMapperFacade(PersonDomain.class, Person.class).map(workerDTO, person);
//    }
//    public void map (PersonDomain workerDTO, Card card) {
//        this.mapperFactory.getMapperFacade(PersonDomain.class, Card.class).map(workerDTO, card);
//    }

//    public <S, D> D map(S s, Class<D> type) {
//        return this.mapperFactory.getMapperFacade().map(s, type);
//    }
//public <Target> void map (PersonDomain workerDTO, Target mapTo) {
//    this.mapperFactory.getMapperFacade(PersonDomain.class, Target.class).map(workerDTO, mapTo);
//}


//    public <S, D> D map (S s, Class<D> target) {
//        return this.mapperFactory.getMapperFacade().map(s, target);
//    }
}
