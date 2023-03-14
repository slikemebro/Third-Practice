package com.ua.glebkorobov.dto;

import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.util.Random;

@NoArgsConstructor
public class Pojo {
    String name = RandomStringUtils.randomAlphabetic(1, 15);

    long count1 = new Random().nextLong() & Integer.MAX_VALUE;
    long count = RandomUtils.nextLong(1, 10000);
    LocalDateTime dateTime = LocalDateTime.now();


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
    @Pattern(regexp = "[a-z-A-Z]*", message = "Name has invalid characters")
    @Pattern(regexp = "a", message = "Name doesn't have character a")
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

    @Override
    public String toString() {
        return "Pojo{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", dateTime=" + dateTime +
                '}';
    }
}
