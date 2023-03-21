package com.ua.glebkorobov.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


public class Pojo {

    private String name;

    private long count;

    private LocalDateTime dateTime;


    public Pojo(String name, long count, LocalDateTime dateTime) {
        this.name = name;
        this.count = count;
        this.dateTime = dateTime;
    }

    public Pojo() {

    }

    @NotNull(message = "First name is compulsory")
    @Min(value = 10, message = "Count should be 10 or bigger")
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @NotNull(message = "First name is compulsory")
    @NotBlank(message = "First name is compulsory")
    @Size(min = 7, message = "Length of the name should be 7 or bigger")
    @Pattern(regexp = ".*a.*", message = "Name doesn't have character a")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }


    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
