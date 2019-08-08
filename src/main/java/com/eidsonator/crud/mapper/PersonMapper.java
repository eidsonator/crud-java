package com.eidsonator.crud.mapper;

import com.eidsonator.crud.model.Person;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PersonMapper {

    @Insert("INSERT INTO person (firstName, lastName) values(#{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Person person);

    @Update("UPDATE person set firstName = #{firstName}, lastName = #{lastName} where id = #{id}")
    void update(Person person);

    @Select("SELECT id, firstName, lastName FROM person WHERE id = #{id}")
    Person findById(long id);

    @Select("Select id, firstName, lastName FROM person")
    List<Person> findAll();

    @Select("Select id, firstName, lastName FROM person")
    List<Person> getPage(RowBounds rowBounds);

    @Select("SELECT COUNT(*) FROM person")
    int count();

    @Delete("DELETE FROM person WHERE id = #{id}")
    void delete(long id);
}

