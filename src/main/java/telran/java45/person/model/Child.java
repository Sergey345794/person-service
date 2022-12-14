package telran.java45.person.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Child extends Person {
	private static final long serialVersionUID = 202212122104L;
	String kindergarten;
	
	public Child(Integer id, String name, LocalDate birthDate, Address address,String kindergarten) {
		super(id, name, birthDate, address);
		this.kindergarten = kindergarten;
	}
}
