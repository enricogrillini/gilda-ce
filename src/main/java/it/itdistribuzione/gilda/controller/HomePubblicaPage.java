package it.itdistribuzione.gilda.controller;

import it.eg.sloth.framework.common.base.BaseFunction;
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
    getForm().getUtility().post(getWebRequest());

    if (!BaseFunction.isBlank( getForm().getUtility().getError().getValue())) {
      getMessageList().addBaseError("User/password errati!");
    }
  }


}
