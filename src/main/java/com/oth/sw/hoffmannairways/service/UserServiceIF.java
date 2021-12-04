package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Customer;
import com.oth.sw.hoffmannairways.entity.Staff;
import com.oth.sw.hoffmannairways.entity.Userdata;

public interface UserServiceIF {
    public Customer registerCustomer(Customer customer);

    public Customer loginCustomer(Userdata userdata);

    public Staff loginStaff(Userdata userdata);
}
