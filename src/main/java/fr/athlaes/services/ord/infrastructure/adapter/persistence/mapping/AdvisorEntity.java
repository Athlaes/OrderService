package fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping;

import fr.athlaes.services.ord.domain.Advisor;
import fr.athlaes.services.ord.domain.Client;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.mapper.CreditMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Advisor")
public class AdvisorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID matricule;
    @NotNull
    private String name;
    @NotNull
    private String firstname;
    @NotNull
    private String address;
    @NotNull
    private LocalDate birthDate;

    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    @Column(unique = true)
    private String phone;

    @NotNull
    private int postalCode;

    @NotNull
    private String city;

    @NotNull
    private boolean isManager;

    public AdvisorEntity(Advisor advisor) {
        this.matricule = advisor.getMatricule();
        this.name = advisor.getName();
        this.firstname = advisor.getFirstname();
        this.address = advisor.getAddress();
        this.birthDate = advisor.getBirthDate();
        this.email = advisor.getEmail();
        this.phone = advisor.getPhone();
        this.postalCode = advisor.getPostalCode();
        this.city = advisor.getCity();
        this.isManager = advisor.isManager();
    }

    public Advisor toDomain() {
        return new Advisor(name, firstname, address, city, postalCode, birthDate, email, phone, matricule, isManager);
    }
}
