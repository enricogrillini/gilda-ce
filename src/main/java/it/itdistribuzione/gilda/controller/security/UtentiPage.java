package it.itdistribuzione.gilda.controller.security;

import org.springframework.util.StreamUtils;

import it.eg.sloth.db.datasource.RowStatus;
import it.eg.sloth.form.grid.Grid;
import it.eg.sloth.framework.common.base.BaseFunction;
import it.eg.sloth.framework.common.base.PasswordUtil;
import it.eg.sloth.framework.common.localization.Localization;
import it.eg.sloth.framework.utility.FileType;
import it.itdistribuzione.gilda.dao.SecurityDao;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.bean.decode.Sec_dec_funzioniDecodeBean;
import it.itdistribuzione.gilda.gen.bean.decode.Sec_dec_ruoliDecodeBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_profiliRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_profiliTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiTableBean;
import it.itdistribuzione.gilda.gen.controllerBaseLogic.sicurezza.UtentiAbstractPage;
import it.itdistribuzione.gilda.gen.dao.SequencesDao;
import it.itdistribuzione.gilda.gen.form.sicurezza.UtentiForm.Tabs;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class UtentiPage extends UtentiAbstractPage {

  @Override
  public String getFunctionName() {
    return Sec_dec_funzioniDecodeBean.SYSADMIN;
  }

  @Override
  protected String getJspMasterName() {
    return Constant.Jsp.UTENTI;
  }

  @Override
  protected String getJspDetailName() {
    return Constant.Jsp.UTENTI_DETAIL;
  }

  @Override
  public void execReset() throws Exception {
    getForm().getFiltri().clearData();
  }

  @Override
  public void execInit() throws Exception {
    execLoad();

    getForm().getDetail().getLocalization().setDecodeMap(Localization.LOCALE_MAP);
    getForm().getProfili().getCodRuolo().setDecodeMap(Sec_dec_ruoliDecodeBean.Factory.getFromDb());
  }

  @Override
  public void execLoad() throws Exception {
    if (getForm().getFiltri().postAndValidate(getWebRequest(), getMessageList())) {
      String nominativo = getForm().getFiltri().getNominativo().getValue();

      Sec_utentiTableBean sec_utentiTableBean = SecurityDao.getTable(nominativo);

      getForm().getMaster().setDataSource(sec_utentiTableBean);
    }
  }

  @Override
  public void execLoadDetail() throws Exception {
    getForm().getDetail().copyFromDataSource(getForm().getMaster().getDataSource());

    if (getRowBean().getStatus() != RowStatus.INSERTED) {
      getForm().getDetail().copyFromDataSource(getForm().getMaster().getDataSource());

      Sec_profiliTableBean profiliTableBean = SecurityDao.loadProfili(getRowBean().getIdutente());
      getForm().getProfili().setDataSource(profiliTableBean);
    } else {
      getForm().getDetail().clearData();
      getForm().getProfili().setDataSource(null);
    }
  }

  @Override
  public void execLoadSubDetail(Grid<?> grid) throws Exception {
    if (getForm().getProfili().manageNav(grid)) {
      getForm().getProfili().copyFromDataSource();
    }
  }

  @Override
  public boolean execPostDetail(boolean validate) throws Exception {
    getForm().getDetail().post(getWebRequest());

    if (validate && getForm().getDetail().validate(getMessageList())) {
      getForm().getDetail().copyToDataSource(getForm().getMaster().getDataSource());

      log.info("getPartFileName - {}", BaseFunction.isBlank(getForm().getDetail().getFoto().getPartFileName()));
      if (!BaseFunction.isBlank(getForm().getDetail().getFoto().getPartFileName())) {
        log.info("setFoto");
        getRowBean().setFoto(getForm().getDetail().getFoto().getPartContent());
      }

      if (getRowBean().getIdutente() == null) {
        getRowBean().setIdutente(SequencesDao.Sec_Seq_Idutente());
      }
      return true;
    } else {
      return !validate;
    }
  }

  @Override
  public boolean execPostSubDetail(Grid<?> grid, boolean validate) throws Exception {
    boolean result = true;

    Tabs tabs = getForm().getTabs();
    if (tabs.currentTabEquals(Tabs._TAB_PROFILI) && getForm().getProfili().manageNav(grid)) {
      getForm().getProfili().post(getWebRequest());

      if (validate && getForm().getProfili().validate(getMessageList())) {
        Sec_profiliRowBean rowBean = getForm().getProfili().getDataSource().getRow();
        getForm().getProfili().copyToDataSource();

        rowBean.setIdutente(getRowBean().getIdutente());

        if (rowBean.getIdprofilo() == null) {
          rowBean.setIdprofilo(SequencesDao.Seq_Idprofilo());
        }

      } else {
        result = !validate;
      }
    }

    return result;
  }

  @Override
  public void resetPasswordPressed() throws Exception {
    if (getForm().getUtility().postAndValidate(getWebRequest(), getMessageList())) {

      // Verifico che la password sia valida
      if (!PasswordUtil.isPasswordValid(getForm().getUtility().getPassword().getValue())) {
        getMessageList().addBaseError("Password non valida");
        return;
      }

      // Verifico che la password sia uguale a "conferma password"
      if (!getForm().getUtility().getPassword().getValue().equals(getForm().getUtility().getConfermaPassword().getValue())) {
        getMessageList().addBaseError("Password e Conferma password non coincidono");
        return;
      }

      Sec_utentiRowBean utentiRowBean = new Sec_utentiRowBean(getRowBean().getIdutente());
      utentiRowBean.setPassword(PasswordUtil.hash(getForm().getUtility().getPassword().getValue()));
      utentiRowBean.save();

      getRowBean().copyFromDataSource(utentiRowBean);
      getRowBean().forceClean();

      execLoadDetail();

      getForm().getUtility().clearData();
      getMessageList().addBaseInfo("Password aggiornata correttamente!");
    }

  }

  @Override
  public void fotoPressed() throws Exception {
    setModelAndView("avatar.png", FileType.PNG);
    StreamUtils.copy(getRowBean().getFoto(), getResponse().getOutputStream());
  }

}
