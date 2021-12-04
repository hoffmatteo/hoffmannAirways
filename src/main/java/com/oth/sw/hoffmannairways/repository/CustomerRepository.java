package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Customer;
import com.oth.sw.hoffmannairways.entity.Userdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c from Customer AS c WHERE c.userdata.username = ?1")
    Customer findCustomerByUsername(String username);

    Customer findCustomerByUserdata(Userdata userdata);


}
