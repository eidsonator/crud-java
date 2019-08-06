package com.eidsonator.crud.controller;

import com.eidsonator.crud.mapper.PersonMapper;
import com.eidsonator.crud.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000", "https://crud-react-eidsonator.herokuapp.com/"})
@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/persons")
public class RestController {

    @Autowired
    private PersonMapper personMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<Person> get() {
        return personMapper.findAll();
    }

    @RequestMapping(path = "/{person}")
    public Person getPerson(@PathVariable("person")long person) {
        return personMapper.findById(person);
    }
    @RequestMapping(method = RequestMethod.POST)
    public Person post(@RequestBody String body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person =  objectMapper.readValue(body, Person.class);
        personMapper.insert(person);
        return person;
    }

    @RequestMapping(path = "/{person}", method = RequestMethod.PUT)
    public Person put(@PathVariable("person")long id, @RequestBody String body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person =  objectMapper.readValue(body, Person.class);
        personMapper.update(person);
        return person;
    }

    @RequestMapping(path="/{person}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("person")long id) {
        personMapper.delete(id);
        return "OK";
    }


}
