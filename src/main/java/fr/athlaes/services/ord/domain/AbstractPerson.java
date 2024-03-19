package fr.athlaes.services.ord.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractPerson {
    protected String name;
    protected String firstname;
    protected String address;
    protected String city;
    protected int postalCode;
    protected LocalDate birthDate;
    protected String email;
    protected String phone;
}
