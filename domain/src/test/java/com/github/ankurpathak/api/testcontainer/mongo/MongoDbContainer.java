package com.github.ankurpathak.api.testcontainer.mongo;

import com.github.ankurpathak.api.util.LogUtil;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> implements BeforeAllCallback, AfterAllCallback, TestRule {

    private static final Logger log = LoggerFactory.getLogger(MongoDbContainer.class);

    /**
     * This is the internal port on which MongoDB is running inside the container.
     * <p>
     * You can use this constant in case you want to map an explicit public port to it
     * instead of the default random port. This can be done using methods like
     * {@link #setPortBindings(java.util.List)}.
     */
    public static final int MONGODB_PORT = 27017;
    public static final String DEFAULT_IMAGE_AND_TAG = "mongo:4.1.13-bionic";

    /**
     * Creates a new {@link MongoDbContainer} with the {@value DEFAULT_IMAGE_AND_TAG} image.
     */
    public MongoDbContainer() {
        this(DEFAULT_IMAGE_AND_TAG);
    }


    /**
     * Creates a new {@link MongoDbContainer} with the given {@code 'image'}.
     *
     * @param image the image (e.g. {@value DEFAULT_IMAGE_AND_TAG}) to use
     */
    public MongoDbContainer(@NotNull String image) {
        super(image);
        addExposedPort(MONGODB_PORT);
    }

    /**
     * Returns the actual public port of the internal MongoDB port ({@value MONGODB_PORT}).
     *
     * @return the public port of this container
     * @see #getMappedPort(int)
     */
    @NotNull
    public Integer getPort() {
        return getMappedPort(MONGODB_PORT);
    }


    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
       // close();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        start();
    }


    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        super.containerIsStarted(containerInfo);
        try{
            execInContainer("/bin/bash", "-c", "mongo --eval 'printjson(rs.initiate())' --quiet");
            execInContainer("/bin/bash", "-c",
                    "until mongo --eval \"printjson(rs.isMaster())\" | grep ismaster | grep true > /dev/null 2>&1;do sleep 1;done");
        }catch (Exception ex){
            LogUtil.logStackTrace(log, ex);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                start();
                try {
                    statement.evaluate();
                }finally {
                  close();
                }
            }
        };
    }


}
