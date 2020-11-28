package com.johngroup.aws;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.johngroup.aws.entity.Person;
import com.johngroup.aws.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntregationTest extends AbstractIntegrationTest {
    private static final String TABLE = "person";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AmazonDynamoDB dynamoDB;

    @Autowired
    PersonRepository personRepository;

    @Before
    public void clear() {
        personRepository.findAll().forEach(person -> personRepository.delete(person));
    }

    @Test
    public void personTableExists() {
        assertThat(dynamoDB.listTables().getTableNames()).contains("person");
    }

    @Test
    public void findAll() {
        putPerson();

        ResponseEntity<List<Person>> response = restTemplate.exchange(
                "/getAll",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {});

        List<Person> personList = response.getBody();

        assertThat(personList).hasSizeGreaterThanOrEqualTo(0);
    }

    @Test
    public void delete() {
        putPerson();

        restTemplate.delete("/deletePerson/random-person-id", Person.class);

        assertThat(personRepository.findAll()).isEmpty();
    }

    private void putPerson() {
        HashMap<String, AttributeValue> personMap = new HashMap<>();
        HashMap<String, AttributeValue> addressMap = new HashMap<>();

        addressMap.put("city", new AttributeValue().withS("Soledad"));
        addressMap.put("state", new AttributeValue().withS("Atlántico"));
        addressMap.put("pinCode", new AttributeValue().withS("80080"));

        personMap.put("personId", new AttributeValue().withS("random-person-id"));
        personMap.put("name", new AttributeValue().withS("John"));
        personMap.put("age", new AttributeValue().withN("22"));
        personMap.put("email", new AttributeValue().withS("john@gmail.com"));
        personMap.put("address", new AttributeValue().withM(addressMap));

        dynamoDB.putItem(TABLE, personMap);
    }

}
