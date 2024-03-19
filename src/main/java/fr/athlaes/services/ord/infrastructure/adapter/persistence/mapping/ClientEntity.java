package fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.athlaes.services.ord.domain.Client;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.mapper.CreditMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Client")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String firstname;

    @NotNull
    private String address;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private String currentJob;

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

    @Transient
    private List<CreditOrderEntity> creditOrder = new ArrayList<>();

    public ClientEntity(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.firstname = client.getFirstname();
        this.address = client.getAddress();
        this.birthDate = client.getBirthDate();
        this.currentJob = client.getCurrentJob();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.creditOrder = client.getCreditOrder().stream().map(CreditOrderEntity::new).toList();
        this.postalCode = client.getPostalCode();
        this.city = client.getCity();
    }

    public Client toDomain() {
        return new Client(name, firstname, address, city, postalCode, birthDate, currentJob, email, phone, id, CreditMapper.toDomain(creditOrder));
    }
}
