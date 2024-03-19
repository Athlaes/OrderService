package fr.athlaes.services.ord.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Client extends AbstractPerson{
    UUID id;

    protected String currentJob;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CreditOrder> creditOrder = new ArrayList<>();

    public Client(String name, String firstname, String address, String city, int postalCode, LocalDate birthDate, String currentJob, String email, String phone, UUID id, List<CreditOrder> creditOrder) {
        super(name, firstname, address, city, postalCode, birthDate, email, phone);
        this.currentJob = currentJob;
        this.id = id;
        this.creditOrder = creditOrder;
    }
}
