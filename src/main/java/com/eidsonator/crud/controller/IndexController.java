package com.eidsonator.crud.controller;

import com.eidsonator.crud.mapper.PersonMapper;
import com.eidsonator.crud.model.Person;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private PersonMapper personMapper;

    @GetMapping("/")
    public String index(Model model) {
//        PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);

        List<Person> people = personMapper.findAll();
        model.addAttribute("people", people);
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("person", new Person());
        return "create";
    }

    @PostMapping("/create")
    public String createSubmit(@ModelAttribute Person person) {
        if (person.getId() != null) {
            personMapper.update(person);
        } else {
            personMapper.insert(person);
        }
        return "result";
    }

    @GetMapping("/details")
    public String details(@RequestParam(name="id") long id, Model model) {
        Person person = personMapper.findById(id);
        model.addAttribute("person", person);
        return "details";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam(name="id") long id, Model model) {
        Person person = personMapper.findById(id);
        model.addAttribute("person", person);
        return "create";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(name="id") long id, Model model) {
        personMapper.delete(id);
        return index(model);
    }
}
