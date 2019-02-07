package com.github.ankurpathak.app;

import com.mongodb.client.model.IndexOptions;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

public class MongoCleanUpExtension implements AfterEachCallback, BeforeEachCallback {

    private final Object testClassInstance;
    private final Class<?>[] collectionClasses;
    private final String fieldName;
    private final String getterName;
    private MongoTemplate mongoTemplate;

    public MongoCleanUpExtension(final Object testClassInstance, final Class<?>... collectionClasses) {
        this(testClassInstance, "mongoTemplate", "getMongoTemplate", collectionClasses);
    }
    public MongoCleanUpExtension(final Object testClassInstance, final String fieldOrGetterName,
                            final Class<?>... collectionClasses) {
        this(testClassInstance, fieldOrGetterName, fieldOrGetterName, collectionClasses);
    }
    public MongoCleanUpExtension(final Object testClassInstance, final String fieldName,
                               final String getterName, final Class<?>... collectionClasses) {
        Assert.notNull(testClassInstance, "parameter 'testClassInstance' must not be null");
        Assert.notNull(fieldName, "parameter 'fieldName' must not be null");
        Assert.notNull(getterName, "parameter 'getterName' must not be null");
        Assert.notNull(collectionClasses, "parameter 'collectionClasses' must not be null");
        Assert.noNullElements(collectionClasses,
                "array 'collectionClasses' must not contain null elements");
        this.fieldName = fieldName;
        this.getterName = getterName;
        this.testClassInstance = testClassInstance;
        this.collectionClasses = collectionClasses;
    }


    private MongoTemplate getMongoTemplate() {
        if(this.mongoTemplate == null){
            try {
                Object value = ReflectionTestUtils.getField(testClassInstance, fieldName);
                if (value instanceof MongoTemplate) {
                    this.mongoTemplate = (MongoTemplate) value;
                }
                value = ReflectionTestUtils.invokeGetterMethod(testClassInstance, getterName);
                if (value instanceof MongoTemplate) {
                    this.mongoTemplate = (MongoTemplate) value;
                }
            } catch (final IllegalArgumentException e) {
                // throw exception with dedicated message at the end
            }
            if(this.mongoTemplate == null){
                throw new IllegalArgumentException(
                        String.format("%s expects either field '%s' or method '%s' in order to access the required MongoTemmplate",
                                this.getClass().getSimpleName(), fieldName, getterName));
            }
        }

        return mongoTemplate;
    }

    private void dropCollections() {
        for (final Class<?> type : this.collectionClasses) {
            this.getMongoTemplate().dropCollection(type);
        }
    }
    private void createIndices() {
        for (final Class<?> type : this.collectionClasses) {
            createIndicesFor(type);
        }
    }

    private void createIndicesFor(final Class<?> type) {
        final MongoMappingContext mappingContext =
                (MongoMappingContext) getMongoTemplate().getConverter().getMappingContext();
        final MongoPersistentEntityIndexResolver indexResolver =
                new MongoPersistentEntityIndexResolver(mappingContext);
        for (final MongoPersistentEntityIndexResolver.IndexDefinitionHolder indexToCreate : indexResolver.resolveIndexFor(ClassTypeInformation.from(type))) {
            createIndex(indexToCreate);
        }
    }
    private void createIndex(final MongoPersistentEntityIndexResolver.IndexDefinitionHolder indexDefinition) {
        this.getMongoTemplate().getDb().getCollection(indexDefinition.getCollection())
                .createIndex(indexDefinition.getIndexKeys(), new IndexOptions().partialFilterExpression(indexDefinition.getIndexOptions()));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        dropCollections();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        dropCollections();
        createIndeces();
    }
}
