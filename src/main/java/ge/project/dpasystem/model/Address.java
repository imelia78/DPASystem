package ge.project.dpasystem.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String district;
    private String street;
}
