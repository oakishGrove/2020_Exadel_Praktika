package practice.guestregistry.data.mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

//TODO:it should use core application-test.properties instead
//@ComponentScan(basePackages = "practice.guestregistry")
//@PropertySource("file:${project.basedir}/guest-registry-core/src/main/resources/core:application-test.properties")
//@PropertySource(value = "application-test.properties")
//@PropertySource("file:guest-registry-core/src/main/resources/application-test.properties")
//@PropertySource({"file:guest-registry-core/src/main/resources/application-${envTarget:dev}.properties"})
@PropertySource({"classpath:mongoEmbedded.properties"})
public class EmbeddedMongoConfig {

    private static final Logger log = LoggerFactory.getLogger(EmbeddedMongoConfig.class);
    private static MongodProcess mongoProcess;
    @Value("${spring.data.mongodb.host}")
    private String MONGO_HOST;
    @Value("${spring.data.mongodb.database}")
    private String MONGO_DATABASE;
    @Value("${spring.data.mongodb.port}")
    private String MONGO_PORT;

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(new SimpleMongoClientDbFactory(
                "mongodb://"
                        + MONGO_HOST + ":"
                        + MONGO_PORT + "/"
                        + MONGO_DATABASE));
    }

    @PostConstruct
    public void start () throws IOException {

        log.debug("using: " + MONGO_DATABASE);
        log.debug("using: " + MONGO_HOST);
        log.debug("using: " + MONGO_PORT);

        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable mongoExecutable = starter.prepare(
        new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(MONGO_HOST, Integer.decode(MONGO_PORT), false))
                .build());
        try {
            mongoProcess = mongoExecutable.start();
        } catch (Exception ex) {
            if (mongoProcess != null) {
                mongoProcess.stop();
            }
            mongoProcess = mongoExecutable.start();
        }
    }

    @PreDestroy
    public static void stop() {
        if (mongoProcess != null) {
            mongoProcess.stop();
        }
    }

}
