package app.hungernet.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static app.hungernet.constants.ApplicationConstantsConfigs.USER_TABLE_NAME;

@Entity
@Table(name = USER_TABLE_NAME)
@Data
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private Boolean locked = false;
    private Boolean enabled = true;


    @OneToMany(cascade = CascadeType.ALL)
    @Transient
    private transient Set<Order> orderSet;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "roles_name")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
