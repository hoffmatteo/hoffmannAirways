package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.entity.Customer;
import com.oth.sw.hoffmannairways.entity.Staff;
import com.oth.sw.hoffmannairways.entity.Userdata;
import com.oth.sw.hoffmannairways.repository.CustomerRepository;
import com.oth.sw.hoffmannairways.repository.StaffRepository;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserServiceIF {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StaffRepository staffRepository;

    @Transactional
    public Customer registerCustomer(Customer customer) {
        //TODO if...
        if (customerRepository.findCustomerByUsername(customer.getUserdata().getUsername()) == null) {
            return customerRepository.save(customer);

        } else {
            return null;
        }
    }

    public Customer loginCustomer(Userdata userdata) {
        //TODO
        return customerRepository.findCustomerByUserdata(userdata);
    }

    public Staff loginStaff(Userdata userdata) {
        //TODO
        return staffRepository.findStaffByUserdata(userdata);
    }
}
