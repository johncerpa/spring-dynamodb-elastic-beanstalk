package com.johngroup.aws.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.johngroup.aws.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PersonRepository {

    @Autowired
    private DynamoDBMapper mapper;

    public Person add(Person person) {
        mapper.save(person);
        return person;
    }

    public Person findById(String personId) {
        return mapper.load(Person.class, personId);
    }

    public String delete(Person person) {
        mapper.delete(person);
        return "Person was deleted";
    }

    public String update(Person person) {
        mapper.save(person, buildExpression(person));
        return "Person updated";
    }

    // Checking if the entity exists in database
    private DynamoDBSaveExpression buildExpression(Person person) {
        DynamoDBSaveExpression expression = new DynamoDBSaveExpression();

        Map<String, ExpectedAttributeValue> expected = new HashMap<>();

        expected.put(
                "personId",
                new ExpectedAttributeValue(new AttributeValue().withS(person.getPersonId()))
        );

        expression.setExpected(expected);

        return expression;
    }

}
