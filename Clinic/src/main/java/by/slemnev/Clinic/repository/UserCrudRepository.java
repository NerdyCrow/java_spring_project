package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.User;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

import org.springframework.data.domain.*;

public interface UserCrudRepository extends CrudRepository<User,Long> {
    List<User> findAllByIdIsNot(Long id);
    Page<User> findAllByIdIsNot(Long id, Pageable pageable);

}
