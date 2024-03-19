package fr.athlaes.services.ord.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Advisor extends AbstractPerson {
    private UUID matricule;

    private boolean isManager;

    public Advisor(String name, String firstname, String address, String city, int postalCode, LocalDate birthDate, String email, String phone, UUID matricule, boolean isManager) {
        super(name, firstname, address, city, postalCode, birthDate, email, phone);
        this.matricule = matricule;
        this.isManager = isManager;
    }
}
