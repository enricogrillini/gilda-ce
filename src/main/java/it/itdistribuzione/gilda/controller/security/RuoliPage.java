package it.itdistribuzione.gilda.controller.security;

import java.math.BigDecimal;

import it.eg.sloth.db.datasource.RowStatus;
import it.eg.sloth.form.grid.Grid;
import it.eg.sloth.framework.pageinfo.PageStatus;
import it.itdistribuzione.gilda.dao.SecurityDao;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.bean.decode.Sec_dec_funzioniDecodeBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_ruoliRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_ruoliTableBean;
import it.itdistribuzione.gilda.gen.controllerBaseLogic.sicurezza.RuoliAbstractPage;

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
public class RuoliPage extends RuoliAbstractPage {

  @Override
  public String getFunctionName() {
    return Sec_dec_funzioniDecodeBean.SYSADMIN;
  }

  @Override
  protected String getJspMasterName() {
    return Constant.Jsp.RUOLI;
  }

  @Override
  protected String getJspDetailName() {
    return Constant.Jsp.RUOLI_DETAIL;
  }

  @Override
  public void execReset() throws Exception {
  }

  @Override
  public void execInit() throws Exception {
    execLoad();
  }

  @Override
  public void execLoad() throws Exception {
    Sec_dec_ruoliTableBean sec_dec_ruoliTableBean = Sec_dec_ruoliTableBean.Factory.load(null);
    sec_dec_ruoliTableBean.addSortingRule(Sec_dec_ruoliRowBean.POSIZIONE);
    sec_dec_ruoliTableBean.applySort(false);

    getGrid().setDataSource(sec_dec_ruoliTableBean);
  }

  @Override
  public void execLoadDetail() throws Exception {
    if (getRowBean().getStatus() != RowStatus.INSERTED) {
      getForm().getDetail().copyFromDataSource(getGrid().getDataSource());

      getForm().getFunzioni().setDataSource(SecurityDao.getFunzioniRuoli(getRowBean().getCodruolo()));
      getForm().getMenu().setDataSource(SecurityDao.getMenuRuoli(getRowBean().getCodruolo()));
      getForm().getMenuUtente().setDataSource(SecurityDao.getMenuUtenteRuoli(getRowBean().getCodruolo()));
    } else {
      getForm().getDetail().clearData();

      getForm().getFunzioni().setDataSource(null);
      getForm().getMenu().setDataSource(null);
      getForm().getMenuUtente().setDataSource(null);
    }
  }

  @Override
  public void execLoadSubDetail(Grid<?> grid) throws Exception {
    if (grid == null || grid == getForm().getFunzioni()) {
      getForm().getFunzioni().copyFromDataSource();
    }

    if (grid == null || grid == getForm().getMenu()) {
      getForm().getMenu().copyFromDataSource();
    }

    if (grid == null || grid == getForm().getMenuUtente()) {
      getForm().getMenuUtente().copyFromDataSource();
    }
  }

  @Override
  public boolean execPostDetail(boolean validate) throws Exception {
    getForm().getDetail().post(getWebRequest());

    if (validate && getForm().getDetail().validate(getMessageList())) {
      getForm().getDetail().copyToDataSource(getForm().getMaster().getDataSource());

      return true;
    } else {
      return !validate;
    }
  }

  @Override
  public boolean execPostSubDetail(Grid<?> grid, boolean validate) throws Exception {
    boolean result = true;

    if (getForm().getTabs().getCurrentTab() == getForm().getTabs().getFunzioni() && (grid == null || grid == getForm().getFunzioni()) && getForm().getFunzioni().size() > 0) {
      getForm().getFunzioni().post(getWebRequest());

      if (validate && getForm().getFunzioni().validate(getMessageList())) {
        getForm().getFunzioni().copyToDataSource();
        getForm().getFunzioni().getDataSource().getRow().setCodruolo(getRowBean().getCodruolo());
      } else {
        result = !validate;
      }
    } else if (getForm().getTabs().getCurrentTab() == getForm().getTabs().getMenu() && (grid == null || grid == getForm().getMenu()) && getForm().getMenu().size() > 0) {
      getForm().getMenu().post(getWebRequest());

      if (validate && getForm().getMenu().validate(getMessageList())) {
        getForm().getMenu().copyToDataSource();
        getForm().getMenu().getDataSource().getRow().setCodruolo(getRowBean().getCodruolo());
      } else {
        result = !validate;
      }
    } else if (getForm().getTabs().getCurrentTab() == getForm().getTabs().getMenuUtente() && (grid == null || grid == getForm().getMenuUtente()) && getForm().getMenuUtente().size() > 0) {
      getForm().getMenuUtente().post(getWebRequest());

      if (validate && getForm().getMenuUtente().validate(getMessageList())) {
        getForm().getMenuUtente().copyToDataSource();
        getForm().getMenuUtente().getDataSource().getRow().setCodruolo(getRowBean().getCodruolo());
      } else {
        result = !validate;
      }
    }

    return result;
  }

  @Override
  public void normalizzaPressed() throws Exception {
    execLoad();

    int i = 0;
    for (Sec_dec_ruoliRowBean rowBean : getForm().getMaster().getDataSource()) {
      rowBean.setPosizione(new BigDecimal(i += 10));
    }

    getForm().getMaster().getDataSource().save();

    execLoad();
    getForm().getPageInfo().setPageStatus(PageStatus.MASTER);
  }

}
