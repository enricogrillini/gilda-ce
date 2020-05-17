package it.itdistribuzione.gilda.controller.security;

import it.eg.sloth.framework.common.localization.Localization;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.bean.decode.Sec_dec_funzioniDecodeBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiRowBean;
import it.itdistribuzione.gilda.gen.controllerBaseLogic.sicurezza.ProfiloAbstractPage;

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
public class ProfiloPage extends ProfiloAbstractPage {
  @Override
  public String getFunctionName() {
    return Sec_dec_funzioniDecodeBean.LOGGED;
  }

  @Override
  protected String getJspName() {
    return Constant.Jsp.PROFILO;
  }

  @Override
  public void execInit() throws Exception {
    Sec_utentiRowBean utentiRowBean = new Sec_utentiRowBean(getUser().getId());

    getForm().getDetail().getLocalization().setDecodeMap(Localization.LOCALE_MAP);

    getForm().getDetail().setDataSource(utentiRowBean);
    getForm().getDetail().copyFromDataSource();
  }

  @Override
  public boolean execPostDetail(boolean validate) throws Exception {
    getForm().getDetail().post(getWebRequest());

    if (validate && getForm().getDetail().validate(getMessageList())) {
      getForm().getDetail().copyToDataSource();

      return true;
    } else {
      return !validate;
    }

  }

}
