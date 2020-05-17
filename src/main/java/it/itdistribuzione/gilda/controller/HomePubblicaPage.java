package it.itdistribuzione.gilda.controller;

import it.eg.sloth.framework.security.User;
import it.eg.sloth.webdesktop.search.SearchRelevance;
import it.eg.sloth.webdesktop.search.impl.DataTableSearcher;
import it.eg.sloth.webdesktop.search.impl.MenuSearcher;
import it.itdistribuzione.gilda.common.application.HistoryApplication;
import it.itdistribuzione.gilda.dao.SecurityDao;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.controllerBaseLogic.HomePubblicaAbstractPage;

/**
 * Project: gilda-ce
 * Copyright (C) 2019-2020 Enrico Grillini
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Enrico Grillini
 */
public class HomePubblicaPage extends HomePubblicaAbstractPage {

  @Override
  public String getFunctionName() {
    return null;
  }

  @Override
  protected String getJspName() {
    return Constant.Jsp.HOME_PUBBLICA;
  }

  @Override
  public void execInit() throws Exception {
    // setModelAndView(new ModelAndView("redirect:" + "http://localhost:9080/oauth/authorize?"
    // + "scope=read_user&"
    // + "client_id=c51002e9b0fc755b0dc272ffc0798245a924f5af69f48c2d06283a1c48b5919b&"
    // + "redirect_uri=http://localhost:8080/intranet/html/HomePrivataPage.html&"
    // + "response_type=code&"
    // + "state=sadasdsadsad"));
  }

  @Override
  public void loginPressed() throws Exception {

    getForm().getLogin().post(getWebRequest());
    if (getForm().getLogin().getLogin().isPressed()) {
      if (getForm().getLogin().validate(getMessageList())) {

        User user = SecurityDao.login(getForm().getLogin().getUserid().getValue(), getForm().getLogin().getPassword().getValue());
        getWebDesktopDto().setUser(user);

        if (user.isLogged()) {
          // Inizializzo il Notification Center
          HistoryApplication historyApplication = new HistoryApplication();

          getWebDesktopDto().getNotificationCenter().clear();
          getWebDesktopDto().getNotificationCenter().addApplication(historyApplication);
          getWebDesktopDto().getNotificationCenter().close();

          // Inizializzo le ricerche
          MenuSearcher menuSearcher = new MenuSearcher(getUser().getMenu());
          MenuSearcher userMenuSearcher = new MenuSearcher(getUser().getUserMenu());

          getWebDesktopDto().getSearchManager().clean();
          getWebDesktopDto().getSearchManager().addSearcher(menuSearcher, SearchRelevance.VERY_HIGH);
          getWebDesktopDto().getSearchManager().addSearcher(userMenuSearcher, SearchRelevance.VERY_HIGH);

          setModelAndView("redirect:" + Constant.Page.HOME_PRIVATA_PAGE);
        } else {
          getMessageList().addBaseWarning("Userid/pasword errati");
        }
      } else {
        getMessageList().addBaseWarning("Impostare Userid/pasword");
      }
    }
  }

  @Override
  public void loginGitPressed() throws Exception {
    // TODO Auto-generated method stub

  }

}
