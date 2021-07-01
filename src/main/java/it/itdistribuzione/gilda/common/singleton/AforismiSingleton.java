package it.itdistribuzione.gilda.common.singleton;

import java.io.IOException;
import java.sql.SQLException;

import it.eg.sloth.framework.FrameComponent;
import it.eg.sloth.framework.common.exception.FrameworkException;
import it.itdistribuzione.gilda.gen.bean.tablebean.AforismiRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.AforismiTableBean;

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
public class AforismiSingleton extends FrameComponent {

  private AforismiTableBean aforismiTableBean;
  private int i;

  private static AforismiSingleton instance = null;

  private AforismiSingleton() throws SQLException, IOException, FrameworkException {
    aforismiTableBean = AforismiTableBean.Factory.load(null);
    i = 0;
  }

  public static AforismiSingleton getInstance() throws SQLException, IOException, FrameworkException {
    if (instance == null) {
      instance = new AforismiSingleton();
    }

    return instance;
  }

  /**
   * Ritorna un Aforisma
   *
   * @return
   */
  public AforismiRowBean getAforismiRowBean() {
    if (aforismiTableBean.size() > 0) {
      aforismiTableBean.setCurrentRow(i % aforismiTableBean.size());
      i++;
      return aforismiTableBean.getRow();
    } else {
      return null;
    }
  }

}
