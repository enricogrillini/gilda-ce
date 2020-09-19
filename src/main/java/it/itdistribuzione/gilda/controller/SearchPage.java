package it.itdistribuzione.gilda.controller;

import org.springframework.web.servlet.ModelAndView;

import it.eg.sloth.form.NavigationConst;
import it.eg.sloth.framework.common.base.BaseFunction;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.bean.decode.Sec_dec_funzioniDecodeBean;
import it.itdistribuzione.gilda.gen.controllerBaseLogic.SearchAbstractPage;
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
public class SearchPage extends SearchAbstractPage {

  @Override
  public String getFunctionName() {
    return Sec_dec_funzioniDecodeBean.LOGGED;
  }

  @Override
  protected String getJspName() {
    return Constant.Jsp.SEARCH;
  }

  public boolean defaultNavigation() throws Exception {
    if (super.defaultNavigation()) {
      return true;
    }

    String navigation[] = getWebRequest().getNavigation();

    if (navigation.length == 1) {
      if (NavigationConst.PREV_PAGE.equals(navigation[0])) {
        // getWebDesktopDto().getSearch().prevPage();
        return true;
      } else if (NavigationConst.NEXT_PAGE.equals(navigation[0])) {
        // getWebDesktopDto().getSearch().nextPage();
        return true;
      }
    }

    return false;
  }

  @Override
  public void execInit() throws Exception {
    String text = getWebRequest().getString("searchText");
    String value = getWebRequest().getString("searchValue");
    String data = getWebRequest().getString("searchData");

    log.info("text: {}, value {}, data {}", text, value, data);
    log.info("webRequest: {}", getWebRequest());

    if (!BaseFunction.isBlank(value)) {
      // Go to function
      setModelAndView(new ModelAndView("redirect:" + data));
    } else if (!BaseFunction.isBlank(text)) {
      getWebDesktopDto().getSearchManager().applySearch(text, 100);

      log.info("SuggestionList: {}", getWebDesktopDto().getSearchManager().getSuggestionList());
    }
  }

}
