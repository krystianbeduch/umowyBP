package pl.bpwesley.TourOperator.email.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "variables")
public class Variable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variableId;

    @Column(nullable = false, unique = true)
    private String name;
    private String value;

    public Variable(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
