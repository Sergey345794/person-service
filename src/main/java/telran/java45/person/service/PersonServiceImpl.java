package telran.java45.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
import telran.java45.person.dao.PersonRepository;
import telran.java45.person.dto.AddressDto;
import telran.java45.person.dto.ChildDto;
import telran.java45.person.dto.CityPopulationDto;
import telran.java45.person.dto.EmployeeDto;
import telran.java45.person.dto.PersonDto;
import telran.java45.person.dto.exceptions.PersonNotFoundException;
import telran.java45.person.model.Address;
import telran.java45.person.model.Child;
import telran.java45.person.model.Employee;
import telran.java45.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
	
	final PersonRepository personRepository;
	final ModelMapper modelMapper;
	
	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if(personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		TypeMap<Person,  PersonDto> mapperMap = modelMapper.createTypeMap(Person.class, PersonDto.class);
		mapperMap.include(Child.class, ChildDto.class)
				.include(Employee.class, EmployeeDto.class);
				//TODO
//		return modelMapper.map(person, mapperMap);	
		return null;
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
		personRepository.delete(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
		person.setName(name);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
		person.setAddress(modelMapper.map(addressDto, Address.class));
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findByAddressCity(city)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findByName(name)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAges(Integer minAge, Integer maxAge) {
		LocalDate from = LocalDate.now().minusYears(minAge);
		LocalDate to = LocalDate.now().minusYears(maxAge);
		return personRepository.findByBirthDateBetween(from, to)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		return personRepository.getCitiesPopulation();
	}
	
	@Override
	public void run(String... args) {
		if(personRepository.count() == 0) {
			Person person = new Person(1000, "John", LocalDate.of(1995,  4, 11),
					new Address("Tel Aviv", "Ben Gvirol", 87));
			Child child = new Child(2000, "Mosche", LocalDate.of(2018,  7, 5),
					new Address("Ashkelon", "Bar Kihva", 21), "Shalom");
			Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995,  11, 23),
					new Address("Rehovot", "Herzl", 7), "Motorola", 20000);
			personRepository.save(person);
			personRepository.save(child);
			personRepository.save(employee);
		}
	}

}
