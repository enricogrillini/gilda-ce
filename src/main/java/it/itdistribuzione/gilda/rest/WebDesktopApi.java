package it.itdistribuzione.gilda.rest;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eg.sloth.framework.common.exception.FrameworkException;
import it.eg.sloth.webdesktop.common.SessionManager;
import it.eg.sloth.webdesktop.dto.WebDesktopDto;
import it.eg.sloth.webdesktop.search.model.SimpleSuggestionList;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiRowBean;

/**
 * Project: gilda-ce
 * Copyright (C) 2019-2020 Enrico Grillini
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Enrico Grillini
 */
@RestController
@RequestMapping("/api/webdesktop")
@Api(value = "/api/webdesktop", produces = MediaType.APPLICATION_JSON_VALUE, tags = "WebDesktop API")
public class WebDesktopApi {

  @ApiOperation(value = "Ritorna l'avatar dell'utente conesso")
  @ApiResponses(value = {
                          @ApiResponse(code = 200, message = "Ok", response = byte[].class)
  })
  @GetMapping(path = "/avatar", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getAvatar(HttpServletRequest request) throws SQLException, FrameworkException, IOException {
    WebDesktopDto webDesktopDto = SessionManager.getWebDesktopDto(request);

    Sec_utentiRowBean utentiRowBean = new Sec_utentiRowBean(webDesktopDto.getUser().getId());

    byte[] foto = utentiRowBean.getFoto();

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_PNG)
        .header("Content-Disposition", "attachment; filename=avatar.png")
        .body(foto);
  }

  @ApiOperation(value = "Effettua una ricerca")
  @ApiResponses(value = {
                          @ApiResponse(code = 200, message = "Ok", response = SimpleSuggestionList.class)
  })
  @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleSuggestionList getCompanyLogo(@RequestParam @ApiParam(value = "Testo da ricercare", name = "query") String query, HttpServletRequest request) {
    WebDesktopDto webDesktopDto = SessionManager.getWebDesktopDto(request);

    return new SimpleSuggestionList(webDesktopDto.getSearchManager().simpleSearch(query, 10));
  }

}
