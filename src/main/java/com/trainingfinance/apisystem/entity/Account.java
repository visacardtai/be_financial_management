package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.config.Roles;
import com.trainingfinance.apisystem.entity.id.AccountRoleEntity;
import com.trainingfinance.apisystem.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "account",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
//    @JoinTable(name = "accounts_roles",
//            joinColumns = @JoinColumn(name = "account_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles;

//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
//    @JoinTable(
//            name = "accounts_roles",
//            joinColumns = @JoinColumn(name = "account_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    @ToString.Exclude
//    private List<Role> roles;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountRoleEntity> accountRoleEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Token> tokens;

//    @Enumerated(EnumType.STRING)
//    private Roles roles;
//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return accountRoleEntities.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getName()))
                .collect(Collectors.toList());
//        return roles.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
