package com.oth.sw.hoffmannairways.entity;

import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.entity.util.SingleIdEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class User extends SingleIdEntity<String> implements UserDetails, Serializable {
    @Id
    @Length(min = 4, max = 30)
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    @Length(min = 7, max = 30)
    private String name;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;
    private boolean sendNotification;

    public User() {
    }

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.accountType = AccountType.USER;
    }

    public User(String username, String password, String name, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.accountType = accountType;
    }

    public User(String username, String password, String name, AccountType accountType, boolean sendNotification) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.accountType = accountType;
        this.sendNotification = sendNotification;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return User.this.accountType.name();
            }
        });
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    @Override
    public String getID() {
        return this.username;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }
}
