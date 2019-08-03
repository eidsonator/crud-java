package com.eidsonator.crud.mapper;

import com.eidsonator.crud.model.Person;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PersonMapper {

    @Insert("INSERT INTO person (firstName, lastName) values(#{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Person person);

    @Select("SELECT id, firstName, lastName FROM person WHERE id = #{id}")
    Person findById(long id);

    @Select("Select id, firstName, lastName FROM person")
    List<Person> findAll();
}

