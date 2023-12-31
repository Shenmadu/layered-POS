package entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Customer {
@Id
    private String id;
    private String name;
    private String address;
    private double salary;
}
