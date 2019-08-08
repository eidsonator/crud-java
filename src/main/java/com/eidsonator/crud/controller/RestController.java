package com.eidsonator.crud.controller;

import com.eidsonator.crud.mapper.PersonMapper;
import com.eidsonator.crud.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000", "https://crud-react-eidsonator.herokuapp.com"})
@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/persons")
public class RestController {

    @Autowired
    private PersonMapper personMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Person>> get(@RequestParam("page") Optional<Integer> page, @RequestParam("perPage") Optional<Integer> perPage) {

        int _page = page.orElse(1);
        int _perPage = perPage.orElse(2);

//        Link: </products?page=5&perPage=20>;rel=self,</products?page=0&perPage=20>;rel=first,</products?page=4&perPage=20>;rel=previous,</products?page=6&perPage=20>;rel=next,</products?page=26&perPage=20>;rel=last

        HttpHeaders headers = new HttpHeaders();
        headers.set("links", getLinkHeader(_page, _perPage));
        headers.set("access-control-expose-headers", "links,x-page");
        headers.set("x-page", Integer.toString(_page));
        RowBounds rowBounds = new RowBounds( (_page - 1) * _perPage, _perPage);
        return new ResponseEntity<List<Person>>(personMapper.getPage(rowBounds), headers, HttpStatus.OK);
    }

    private String getLinkHeader(int page, int perPage) {
        int count = personMapper.count();
        int lastPage = count/perPage + 1;

        StringBuilder links = new StringBuilder(String.format("</persons?page=1&perPage=%d>;rel=first,", perPage));
        links.append(String.format("</persons?page=%d&perPage=%d>;rel=prev,", page - 1, perPage));

        int displayPage = 1;
        if (page > 3) {
            displayPage = page - 2;
        }


        int displayPages = Math.min(lastPage, 10) + displayPage - 1;

        for (; displayPage <= displayPages; displayPage++) {
            links.append(String.format("</persons?page=%d&perPage=%d>;rel=%s,",  displayPage, perPage, displayPage));
        }
        links.append(String.format("</persons?page=%d&perPage=%d>;rel=next,", page + 1, perPage));
        links.append(String.format("</persons?page=%d&perPage=%d>;rel=last", lastPage, perPage));

        return links.toString();

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
