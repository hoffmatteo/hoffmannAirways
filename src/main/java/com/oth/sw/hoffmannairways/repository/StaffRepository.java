package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Staff;
import com.oth.sw.hoffmannairways.entity.Userdata;
import org.springframework.data.repository.CrudRepository;

public interface StaffRepository extends CrudRepository<Staff, Integer> {

    Staff findStaffByUserdata(Userdata userdata);
}
