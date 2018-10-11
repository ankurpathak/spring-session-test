package com.ankurpathak.springsessiontest;

public interface ISequenceRepository extends ExtendedMongoRepository<Sequence, String>, CustomizedSequenceRepository {
}
