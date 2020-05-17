package it.itdistribuzione.gilda.controller;

import org.springframework.web.servlet.ModelAndView;

import it.eg.sloth.framework.security.User;
import it.eg.sloth.webdesktop.controller.BasePage;
import it.itdistribuzione.gilda.gen.Constant;

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
public class LogoutPage extends BasePage {

  @Override
  public String getFunctionName() {
    return null;
  }

  @Override
  public ModelAndView service() throws Exception {
    getWebDesktopDto().setUser(new User());

    return new ModelAndView("redirect:" + Constant.Page.HOME_PUBBLICA_PAGE);
  }

}
