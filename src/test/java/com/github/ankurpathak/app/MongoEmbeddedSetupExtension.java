package com.github.ankurpathak.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.util.*;

public class MongoEmbeddedSetupExtension implements AfterEachCallback, BeforeEachCallback {

    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;
    private ObjectMapper objectMapper;




    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        mongoTemplate.getDb().drop();
        mongodExecutable.stop();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        String ip = "localhost";
        int randomPort = SocketUtils.findAvailableTcpPort();

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        this.mongodExecutable = starter.prepare(mongodConfig);
        this.mongodExecutable.start();
        this.mongoTemplate = new MongoTemplate(new MongoClient(ip, randomPort), "test");
        this.objectMapper = new ObjectMapper();

        Collection<Class<?>> mongoCollections = setUpCollections();
        Map<String, Resource> jsons = setUpJsons(mongoCollections);
        createDocuments(jsons);
    }


    private Map<String, Resource> setUpJsons(Collection<Class<?>> mongoCollections) {
        Map<String, Resource> jsons = new HashMap<>();
        for(Class<?> mongoCollection : mongoCollections){
            Document document = AnnotationUtils.findAnnotation(mongoCollection, Document.class);
            if(document != null){
                Resource file = new ClassPathResource(String.format("%s.json", document.collection()), mongoCollection);
                if(file.exists()){
                    jsons.put(document.collection(), file);
                }
            }
        }
        return jsons;
    }


    private Set<Class<?>> setUpCollections(){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.github.ankurpathak.app.domain.model")).setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));
        Set<Class<?>> collections = reflections.getTypesAnnotatedWith(Document.class);
        return CollectionUtils.isEmpty(collections) ? Collections.emptySet() : collections;
    }

    private void createDocuments(Map<String, Resource> jsons) throws IOException {
        for(Map.Entry<String, Resource> json: jsons.entrySet()){
            List<?> list = objectMapper.readValue(json.getValue().getFile(), List.class);
            mongoTemplate.insert(list, json.getKey());
        }
    }

}
