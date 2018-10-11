package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class CustomizedSequenceRepositoryImpl extends AbstractCustomizedDomainRepository<Sequence, String> implements CustomizedSequenceRepository {
    public static final String MESSAGE_SEQUENCE_PROBLEM = "Unable to get next for sequence %s";

    private final MongoTemplate template;

    public CustomizedSequenceRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }


    @Override
    public BigInteger next(String id)  {

        //get sequence id
        Query query = new Query(Criteria.where(DocumentCollections.Field.FIELD_ID).is(id));

        Sequence sequence = template.findOne(query, Sequence.class);
        BigInteger nextValue = sequence.getCurr();
        nextValue = nextValue.add(BigInteger.ONE);

        //increase sequence id by 1
        Update update = new Update();
        update.set(DocumentCollections.Field.FIELD_CURRENT, nextValue);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        //this is the magic happened.
        Sequence seq =
                template.findAndModify(query, update, options, Sequence.class);

        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (seq == null) {
            throw new SequenceException(String.format(MESSAGE_SEQUENCE_PROBLEM, id));
        }

        return seq.getCurr();

    }

    @Override
    public MongoTemplate getTemplate() {
        return template;
    }
}
