package com.suinfinity.user.model;

import com.suinfinity.user.dto.UserDTO.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "User")
@Setter
@Getter
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String name;

  private String password;

  @Email private String email;

  @ColumnDefault("true")
  private Boolean isEnable;

  @Embedded private Address address;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role_assignment",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"))
  private Set<Role> grantedRoles;

  @Transient private List<Authority> authorities;

  @Override
  public List<Authority> getAuthorities() {
    authorities =
        this.getGrantedRoles().stream().map(Role::getAuthorities).flatMap(Set::stream).toList();
    return authorities;
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
