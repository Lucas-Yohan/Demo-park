package com.lucasyohan.domain.Demo_Park.jwt;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private Usuarios usuario;

    public JwtUserDetails(Usuarios usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public Long getId(){
        return this.usuario.getId();
    }

    public String getRole(){
        return usuario.getRole().name();
    }

}
