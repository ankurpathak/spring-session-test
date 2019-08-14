package com.github.ankurpathak.api.domain.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.service.ISchemaService;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.mongodb.client.model.IndexOptions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.valid4j.Assertive.require;

public class MongoDataRule<SELF extends AbstractRestIntegrationTest<SELF>> implements AfterEachCallback, BeforeEachCallback, TestRule {
    private AbstractRestIntegrationTest<SELF> test;
    private MongoTemplate template;
    private ObjectMapper objectMapper;
    private Collection<Class<?>> collections;
    private Map<String, Resource> jsons;
    private ISchemaService schemaService;




    public MongoTemplate getTemplate() {
        if(this.template == null){
            this.template = setUpTemplate();
            ensure(template, notNullValue());
        }
        return this.template;
    }

    public ObjectMapper getObjectMapper() {
        if(this.objectMapper == null){
            this.objectMapper = setUpObjectMapper();
            ensure(this.objectMapper, notNullValue());
        }
        return this.objectMapper;
    }

    public ISchemaService getSchemaService() {
        if(this.schemaService == null){
            this.schemaService = setUpSchemaService();
            ensure(this.schemaService, notNullValue());
        }
        return this.schemaService;
    }

    public Collection<Class<?>> getCollections() {
        if(this.collections == null){
            this.collections = setUpCollections();
            ensure(this.collections, MatcherUtil.notCollectionEmpty());
        }
        return this.collections;
    }

    public Map<String, Resource> getJsons() {
        this.jsons = setUpJsons();
        ensure(this.jsons, MatcherUtil.notMapEmpty());
        return this.jsons;
    }

    public MongoDataRule(AbstractRestIntegrationTest<SELF> test) {
        require(test, notNullValue());
        this.test = test;
    }



    private MongoTemplate setUpTemplate(){
        Field field = ReflectionUtils.findField(test.getClass(), "mongoTemplate");
        if(field != null) {
            field.setAccessible(true);
            Object fileldValue = ReflectionUtils.getField(field, test);
            if(fileldValue instanceof MongoTemplate)
                return (MongoTemplate) fileldValue;
        }
        return null;
    }

    private ISchemaService setUpSchemaService(){
        Field field = ReflectionUtils.findField(test.getClass(), "schemaService");
        if(field != null) {
            field.setAccessible(true);
            Object fileldValue = ReflectionUtils.getField(field, test);
            if(ISchemaService.class.isAssignableFrom(fileldValue.getClass()))
                return ISchemaService.class.cast(fileldValue);
        }
        return null;
    }


    private ObjectMapper setUpObjectMapper(){
        Field field = ReflectionUtils.findField(test.getClass(), "objectMapper");
        if(field != null) {
            field.setAccessible(true);
            Object fileldValue = ReflectionUtils.getField(field, test);
            if(fileldValue instanceof ObjectMapper)
                return (ObjectMapper) fileldValue;
        }
        return null;
    }


    private Map<String, Resource> setUpJsons() {
        Map<String, Resource> jsons = new HashMap<>();
        for(Class<?> mongoCollection : getCollections()){
            Document document = AnnotationUtils.findAnnotation(mongoCollection, Document.class);
            if(document != null){
                Resource file = new ClassPathResource(String.format("%s.json", document.collection()), test.getClass());
                if(file.exists()){
                    jsons.put(document.collection(), file);
                }
            }
        }
        return jsons;
    }


    private Set<Class<?>> setUpCollections(){
      //  Reflections reflections = new Reflections(new ConfigurationBuilder()
      //          .setUrls(ClasspathHelper.forPackage("com.github.ankurpathak.api.domain.model")).setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));
       // Set<Class<?>> collections = reflections.getTypesAnnotatedWith(Document.class);
        AnnotatedTypeScanner scanner = new AnnotatedTypeScanner(Document.class);
        Set<Class<?>> collections = scanner.findTypes("com.github.ankurpathak.api.domain.model");
        return CollectionUtils.isEmpty(collections) ? Collections.emptySet() : collections;
    }


    private void createDocuments() throws IOException {
        for(Map.Entry<String, Resource> json: this.getJsons().entrySet()){
            List<Map<String, Object>> list = getObjectMapper().readValue(json.getValue().getFile(), List.class);
            processList(list);
            getTemplate().insert(list, json.getKey());
        }
    }

    private void processList(List<Map<String, Object>> list) {
        list.stream().forEach(doc -> {
            if(doc.containsKey(Model.Domain.Field.CREATED)){
                doc.put(Model.Domain.Field.CREATED, Instant.parse(MapUtils.getString(doc, "created")));
            }
            if(doc.containsKey(Model.Domain.Field.UPDATED)){
                doc.put(Model.Domain.Field.UPDATED, Instant.parse(MapUtils.getString(doc, "updated")));
            }
        });
    }


    private void dropCollections() {
        for (final Class<?> type : this.getCollections()) {
            this.getTemplate().dropCollection(type);
        }
    }

    private void dropDatabase() {
        this.getTemplate().getDb().drop();
    }
    private void createIndices() {
        for (final Class<?> type : this.getCollections()) {
            createIndicesFor(type);
        }
    }

    private void createIndicesFor(final Class<?> type) {
        final MongoMappingContext mappingContext = (MongoMappingContext) getTemplate().getConverter().getMappingContext();
        final MongoPersistentEntityIndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mappingContext);
        Iterable<? extends IndexDefinition> definitions = indexResolver.resolveIndexFor(type);
        IndexOperations indexOperations = template.indexOps(type);
        definitions.forEach(indexOperations::ensureIndex);
    }


    private void createIndex(final MongoPersistentEntityIndexResolver.IndexDefinitionHolder indexDefinition) {
        this.getTemplate().getDb().getCollection(indexDefinition.getCollection())
                .createIndex(indexDefinition.getIndexKeys(), new IndexOptions().partialFilterExpression(indexDefinition.getIndexOptions()));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        after();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        before();
    }

    public void before()throws Exception{
        // dropCollections();
        dropDatabase();
        createIndices();
        createViews();
        createDocuments();
    }

    private void createViews() throws Exception {
        getSchemaService().createViews();
    }

    public void after() throws Exception{
        dropDatabase();
                //dropCollections();
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    statement.evaluate();
                }finally {
                    after();
                }
            }
        };
    }
}
