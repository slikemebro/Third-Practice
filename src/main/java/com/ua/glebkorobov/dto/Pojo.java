package com.ua.glebkorobov.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvIgnore;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.Set;


public class Pojo {

    @CsvBindByName(column = "NAME")
    @CsvBindByPosition(position = 0)
    private String name;

    @CsvBindByName(column = "COUNT")
    @CsvBindByPosition(position = 1)
    private long count = 0;

    @JsonIgnore
    private static long countAll = 0;

    @CsvBindByName(column = "ERRORS")
    @CsvBindByPosition(position = 2)
    @JsonIgnore
    private Set<ConstraintViolation<Pojo>> errors;

    @CsvIgnore
    private LocalDateTime dateTime;

    public Pojo() {
        dateTime = LocalDateTime.now();
        count = countAll++;
        name = RandomStringUtils.randomAlphabetic(4, 15);
    }

    public Pojo(String name, long count, Set<ConstraintViolation<Pojo>> errors, LocalDateTime dateTime) {
        this.name = name;
        this.count = count;
        this.errors = errors;
        this.dateTime = dateTime;
    }

    @NotNull(message = "First name is compulsory")
    @Min(10)
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @NotNull(message = "First name is compulsory")
    @NotBlank(message = "First name is compulsory")
    @Size(min = 7)
    @Pattern(regexp = ".*a.*", message = "Name doesn't have character a")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PastOrPresent(message = "Date must be the past")
    @NotNull
    public LocalDateTime getDateTime() {
        return dateTime;
    }


    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<ConstraintViolation<Pojo>> getErrors() {
        return errors;
    }

    public void setErrors(Set<ConstraintViolation<Pojo>> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", dateTime=" + dateTime +
                '}';
    }
}
