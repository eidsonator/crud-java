package com.eidsonator.crud.controller;

import com.eidsonator.crud.mapper.PersonMapper;
import com.eidsonator.crud.model.Person;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String create() {
        return "create";
    }

    @GetMapping("/details")
    public String details(@RequestParam(name="id") long id, Model model) {
        Person person = personMapper.findById(id);
        model.addAttribute("person", person);
        return "details";
    }
}
