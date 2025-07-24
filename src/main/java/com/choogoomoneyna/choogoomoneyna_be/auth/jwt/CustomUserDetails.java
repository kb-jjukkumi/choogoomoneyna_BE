package com.choogoomoneyna.choogoomoneyna_be.auth.jwt;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final LoginType loginType;

    public CustomUserDetails(UserVO userVO) {
        this.id = userVO.getId();
        this.email = userVO.getEmail();
        this.password = userVO.getPassword();
        this.loginType = LoginType.valueOf(userVO.getLoginType());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
