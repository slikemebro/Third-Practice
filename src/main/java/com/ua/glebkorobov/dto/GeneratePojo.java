package com.ua.glebkorobov.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratePojo {

    public List<Pojo> generate(long count){
        return Stream.generate(Pojo::new).limit(count).collect(Collectors.toList());
    }
}
