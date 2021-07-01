package it.itdistribuzione.gilda.spring.component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import it.eg.sloth.webdesktop.common.SessionManager;
import it.eg.sloth.webdesktop.dto.WebDesktopDto;
import it.itdistribuzione.gilda.dao.SecurityDao;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationFilter implements Filter {

  @SneakyThrows
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    if (req.getRequestURI().contains("/html/") || req.getRequestURI().contains("/api/")) {
      // Access Log IN
      log.info("Access log IN - remoteHost: {}, method: {}, requestURI: {}, protocol {}", req.getRemoteHost(), req.getMethod(), req.getRequestURI(), req.getProtocol());

      if (!req.getRequestURI().startsWith("/html/HomePubblicaPage.html")) {
        WebDesktopDto webDesktopDto = SessionManager.getWebDesktopDto(req);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        SecurityDao.checkWebDesktop(webDesktopDto, auth.getName());
      }
    }

    chain.doFilter(request, response);

    if (req.getRequestURI().contains("/html/") || req.getRequestURI().contains("/api/")) {
      // Access Log OUT
      log.info("Access log OUT - remoteHost: {}, method: {}, requestURI: {}, protocol {}, status {}", req.getRemoteHost(), req.getMethod(), req.getRequestURI(), req.getProtocol(), res.getStatus());
    }
  }
}
