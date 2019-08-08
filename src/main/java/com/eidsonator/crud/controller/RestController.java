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
    private Integer page;
    private Integer perPage;
    private String sortBy;
    private String sortDir;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Person>> get(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("perPage") Optional<Integer> perPage,
            @RequestParam("sortBy") Optional<String> sortBy,
            @RequestParam("sortDir") Optional<String> sortDir
    ) {
        this.page = page.orElse(1);
        this.perPage = perPage.orElse(10);
        this.sortBy = sortBy.orElse("lastName");
        this.sortDir = sortDir.orElse("ASC");

        HttpHeaders headers = new HttpHeaders();
        headers.set("links", getLinkHeader());
        headers.set("access-control-expose-headers", "links,x-page,x-last-page");
        headers.set("x-page", Integer.toString(this.page));
        headers.set("x-last-page", Integer.toString(getLastPage(this.perPage)));
        int offset = (this.page - 1) * this.perPage;
        RowBounds rowBounds = new RowBounds(offset, this.perPage);
        return new ResponseEntity<List<Person>>(personMapper.getPage(this.sortBy, this.sortDir, rowBounds), headers, HttpStatus.OK);
    }

    private String getLinkHeader() {
        int lastPage = getLastPage(perPage);

        StringBuilder links = new StringBuilder(String.format("</persons?page=1&perPage=%d&sortBy=%s&sortDir=%s>;rel=first,", perPage, sortBy, sortDir));
        links.append(String.format("</persons?page=%d&perPage=%d&sortBy=%s&sortDir=%s>;rel=prev,", page - 1, perPage, sortBy, sortDir));

        int displayPage = page;
        if (page + 10 >= lastPage) {
            displayPage = Math.max(1, lastPage - 10);
        }

        int displayPages = Math.min(lastPage, 10);

        for (; displayPage <= displayPages; displayPage++) {
            links.append(String.format("</persons?page=%d&perPage=%d&sortBy=%s&sortDir=%s>;rel=%s,",  displayPage, perPage, sortBy, sortDir, displayPage));
        }
        links.append(String.format("</persons?page=%d&perPage=%d&sortBy=%s&sortDir=%s>;rel=next,", page + 1, perPage, sortBy, sortDir));
        links.append(String.format("</persons?page=%d&perPage=%d&sortBy=%s&sortDir=%s>;rel=last", lastPage, perPage, sortBy, sortDir));

        return links.toString();
    }

    private int getLastPage(int perPage) {
        int count = personMapper.count();
        int lastPage = count / perPage;
        return lastPage * perPage + 1 > count  ? lastPage : lastPage + 1;
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
