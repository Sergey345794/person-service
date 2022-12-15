package telran.java45.person.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	 @JsonSubTypes.Type(value = PersonDto.class, name = "person"),
    @JsonSubTypes.Type(value = ChildDto.class, name = "child"),
    @JsonSubTypes.Type(value = EmployeeDto.class, name = "employee")
})
public class PersonDto {

    Integer id;
    String name;
    LocalDate birthDate;
    AddressDto address; 
    
	public PersonDto( Integer id,  String name, LocalDate birthDate, AddressDto address) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.address = address;
	}
}
