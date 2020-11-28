package com.johngroup.aws;

import com.johngroup.aws.entity.Person;
import com.johngroup.aws.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class SpringbootDynamodbExampleApplication {

	@Autowired
	private PersonRepository personRepository;

	@GetMapping("/getAll")
	public List<Person> getAll() {
		return personRepository.findAll();
	}

	@PostMapping("/savePerson")
	public Person savePerson(@RequestBody Person person) {
		return personRepository.add(person);
	}

	@GetMapping("/getPerson/{personId}")
	public Person findPersonById(@PathVariable String personId) {
		return personRepository.findById(personId);
	}

	@DeleteMapping("/deletePerson")
	public String deletePerson(@RequestBody Person person) {
		return personRepository.delete(person);
	}

	@DeleteMapping("/deletePerson/{personId}")
	public String deletePersonById(@PathVariable String personId) { return personRepository.deleteById(personId); };

	@PutMapping("/updatePerson")
	public String updatePerson(@RequestBody Person person) {
		return personRepository.update(person);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDynamodbExampleApplication.class, args);
	}
}
