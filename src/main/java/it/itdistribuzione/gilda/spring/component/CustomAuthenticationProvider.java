package it.itdistribuzione.gilda.spring.component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import it.eg.sloth.framework.common.base.BaseFunction;
import it.eg.sloth.framework.common.exception.FrameworkException;
import it.itdistribuzione.gilda.dao.SecurityDao;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiRowBean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    String username = auth.getName();
    String password = auth.getCredentials().toString();
    log.info("username {} {}", username, password);

    try {
      Sec_utentiRowBean utentiRowBean = SecurityDao.loadUser(username);
      if (BaseFunction.equals(password, utentiRowBean.getPassword())) {
        return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
      } else {
        return null;
      }
    } catch (SQLException | IOException | FrameworkException e) {
      throw new BadCredentialsException("System authentication failed");
    }
  }

  @Override
  public boolean supports(Class<?> auth) {
    return auth.equals(UsernamePasswordAuthenticationToken.class);
  }

}