package com.suinfinity.user.init;

import com.suinfinity.user.model.Authority;
import com.suinfinity.user.model.Role;
import com.suinfinity.user.repository.AuthorityRepository;
import com.suinfinity.user.repository.RoleRepository;
import com.suinfinity.user.util.AuthorityEnum;
import com.suinfinity.user.util.RoleEnum;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@ConditionalOnProperty(name = "role.initializer.enabled", havingValue = "true")
public class RoleInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private final RoleRepository roleRepository;
  private final AuthorityRepository authorityRepository;

  public RoleInitializer(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
    this.authorityRepository = authorityRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Map<String, Authority> authorityMap = new HashMap<>();
    if (authorityRepository.count() == 0) {
      for (AuthorityEnum authorityEnum : AuthorityEnum.values()) {
        Authority authority = Authority.builder().authority(authorityEnum).build();
        authorityMap.put(authority.getAuthority(), authority);
      }
    } else {
      authorityMap =
          authorityRepository.findAll().stream()
              .collect(Collectors.toMap(Authority::getAuthority, authority -> authority));
    }
    if (roleRepository.findAll().isEmpty()) {
      for (RoleEnum roleEnum : RoleEnum.values()) {
        Role role = Role.builder().role(roleEnum).build();
        if (role.getRole() == RoleEnum.ADMIN) {
          Set<Authority> authorities = Set.of(authorityMap.get(AuthorityEnum.ALL.name()));
          role.setAuthorities(authorities);
        } else if (role.getRole() == RoleEnum.USER) {
          Set<Authority> authorities =
              Set.of(
                  authorityMap.get(AuthorityEnum.CREATE_USER.name()),
                  authorityMap.get(AuthorityEnum.UPDATE_USER.name()),
                  authorityMap.get(AuthorityEnum.CREATE_ORDER.name()),
                  authorityMap.get(AuthorityEnum.READ_ORDER.name()),
                  authorityMap.get(AuthorityEnum.UPDATE_ORDER.name()),
                  authorityMap.get(AuthorityEnum.DELETE_ORDER.name()));
          role.setAuthorities(authorities);
        }
        roleRepository.save(role);
      }
    }
  }
}
