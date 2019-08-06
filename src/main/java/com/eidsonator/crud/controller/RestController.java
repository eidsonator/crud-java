package com.eidsonator.crud.controller;

import com.eidsonator.crud.mapper.PersonMapper;
import com.eidsonator.crud.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:3000")
@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/persons")
public class RestController {

    @Autowired
    private PersonMapper personMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<Person> get() {
        return personMapper.findAll();
    }

    @CrossOrigin(origins = "http://127.0.0.1:3000")
    @RequestMapping(method = RequestMethod.OPTIONS)
    public Person save(@RequestBody Person person) {
        personMapper.insert(person);
        return new Person();
    }

    @CrossOrigin(origins = "http://127.0.0.1:3000")
    @RequestMapping(path = "/{person}", method = RequestMethod.PUT)
    public Person put(@PathVariable("person")long id, @RequestBody String body) {
        Person person = personMapper.findById(id);
        String sumpting = body;
        personMapper.update(person);
        return person;
    }

    @CrossOrigin(origins = "http://127.0.0.1:3000")
    @RequestMapping(path = "/{person}")
    public Person getPerson(@PathVariable("person")long person) {
        return personMapper.findById(person);
    }

}
