package com.vinsguru.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinsguru.user.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>{

}
