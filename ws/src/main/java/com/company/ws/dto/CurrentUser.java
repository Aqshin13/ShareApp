package com.company.ws.dto;

import com.company.ws.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CurrentUser implements UserDetails {

    private String username;
    private String password;
    private long id;
    private boolean active;


    public CurrentUser(User user){
        this.username=user.getUsername();
        this.password=user.getPassword();
        this.id= user.getId();;
        this.active= user.isActive();

    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id=id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN","ROLE_USER");
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
