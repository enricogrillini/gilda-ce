package it.itdistribuzione.gilda.controller.security;

import java.math.BigDecimal;

import it.itdistribuzione.gilda.dao.SecurityDao;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.bean.decode.Sec_dec_funzioniDecodeBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_funzioniRowBean;
import it.itdistribuzione.gilda.gen.controllerBaseLogic.sicurezza.FunzioniAbstractPage;

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
public class FunzioniPage extends FunzioniAbstractPage {

  @Override
  public String getFunctionName() {
    return Sec_dec_funzioniDecodeBean.SYSADMIN;
  }

  @Override
  protected String getJspName() {
    return Constant.Jsp.FUNZIONI;
  }

  @Override
  public void execInit() throws Exception {
    execLoad();
  }

  @Override
  public void execReset() throws Exception {

  }

  @Override
  public void execLoad() throws Exception {
    getForm().getAnagrafica().setDataSource(SecurityDao.getFunzioni());
  }

  @Override
  public boolean execCommit() throws Exception {
    if (super.execCommit()) {
      SecurityDao.normalizzaFunzioniRuoli();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void normalizzaPressed() throws Exception {
    execLoad();

    int i = 0;
    for (Sec_dec_funzioniRowBean rowBean : getForm().getAnagrafica().getDataSource()) {
      rowBean.setPosizione(new BigDecimal(i += 10));
    }

    getForm().getAnagrafica().getDataSource().save();
    execLoad();
  }

  @Override
  public void clonaPressed() throws Exception {
    if (getForm().getAnagrafica().size() > 0) {
      Sec_dec_funzioniRowBean funzioniRowBean = getRowBean();

      onInsert();
      getRowBean().copyFromDataSource(funzioniRowBean);
      getRowBean().setCodfunzione(getRowBean().getCodfunzione() + "-COPY");
      getRowBean().setPosizione(getRowBean().getPosizione().add(new BigDecimal(1)));

      getForm().getAnagrafica().copyFromDataSource();
    }
  }

  // @Override
  // public void addRuoloPressed() throws Exception {
  // if (getForm().getAnagrafica().size() > 0 && getForm().getUtility().postAndValidate(getWebRequest(), getMessageList())) {
  // String codFunzione = getRowBean().getCodfunzione();
  // String codRuolo = getForm().getUtility().getCodRuolo().getValue();
  //
  // Sec_funzioniruoliRowBean rowBean = new Sec_funzioniruoliRowBean(codFunzione, codRuolo);
  // if (rowBean.getCodfunzione() == null) {
  // // Se il record non esiste lo creo e lo inserisco
  // rowBean = new Sec_funzioniruoliRowBean();
  // rowBean.setCodfunzione(codFunzione);
  // rowBean.setCodruolo(codRuolo);
  // }
  //
  // rowBean.setFlagaccesso("S");
  // rowBean.save();
  //
  // getRowBean().setString("ruoli", Lib_sicurezzaBean.Getruoli(rowBean.getCodfunzione()));
  // }
  // }

}
