package it.itdistribuzione.gilda.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.eg.sloth.db.datasource.DataRow;
import it.eg.sloth.db.datasource.DataTable;
import it.eg.sloth.db.query.filteredquery.FilteredQuery;
import it.eg.sloth.db.query.query.Query;
import it.eg.sloth.framework.common.base.BaseFunction;
import it.eg.sloth.framework.common.exception.FrameworkException;
import it.eg.sloth.framework.security.Menu;
import it.eg.sloth.framework.security.User;
import it.eg.sloth.framework.security.VoiceType;
import it.eg.sloth.webdesktop.dto.WebDesktopDto;
import it.eg.sloth.webdesktop.search.SearchRelevance;
import it.eg.sloth.webdesktop.search.impl.DataTableSearcher;
import it.eg.sloth.webdesktop.search.impl.MenuSearcher;
import it.itdistribuzione.gilda.gen.Constant;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_funzioniTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_menuRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_menuTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_menuutenteRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_dec_menuutenteTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_funzioniruoliTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_menuruoliTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_menuutenteruoliTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_profiliTableBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiRowBean;
import it.itdistribuzione.gilda.gen.bean.tablebean.Sec_utentiTableBean;
import lombok.extern.slf4j.Slf4j;

/**
 * Project: gilda-ce Copyright (C) 2019-2020 Enrico Grillini
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Enrico Grillini
 */
@Slf4j
public class SecurityDao {

  private static final String SQL_UTENTI = "Select *\n" +
                                           "from sec_utenti\n" +
                                           "/*W*/" +
                                           "Order By Cognome, Nome";

  private static final String SQL_FUNZIONI = "Select f.*,\n" +
                                             "       lib_sicurezza.GetruoliFunzione(f.codFunzione) Ruoli\n" + "from sec_dec_funzioni f\n" + "/*W*/\n" +
                                             "Order By Posizione";

  private static final String SQL_FUNZIONI_RUOLO = "select fr.*,\n" +
                                                   "       f.Descrizionelunga Descrizione\n" +
                                                   "from Sec_Dec_Funzioni f, Sec_Funzioniruoli fr\n" +
                                                   "where f.codfunzione = fr.codfunzione And\n" +
                                                   "      fr.codRuolo = ?\n" + "Order By f.posizione";

  private static final String SQL_FUNZIONI_BY_UTENTE = "Select f.CODFUNZIONE\n" +
                                                       "From sec_profili p, sec_funzioniruoli fr, sec_dec_funzioni f\n" +
                                                       "Where p.CODRUOLO = fr.CODRUOLO And\n" +
                                                       "      fr.CODFUNZIONE = f.CODFUNZIONE And\n" +
                                                       "      trunc(sysdate) between p.dataInizio and nvl(p.dataFine, to_date('31/12/9999', 'dd/mm/yyyy')) And\n" +
                                                       "      fr.flagaccesso = 'S' And\n" + "      f.flagvalido = 'S' And\n" + "      p.idutente = ?";

  private static final String SQL_MENU = "Select m.*,\n" +
                                         "       lib_sicurezza.GetruoliMenu(m.codMenu) Ruoli\n" +
                                         "from Sec_Dec_Menu m\n" +
                                         "/*W*/" +
                                         "Order By Posizione";

  private static final String SQL_MENU_RUOLO = "select mur.*,\n" +
                                               "       rpad(' ', (livello - 1) * 3) || mu.Descrizionelunga Descrizione\n" +
                                               "from Sec_Dec_Menu mu, Sec_Menuruoli mur\n" +
                                               "where mu.codmenu = mur.codmenu And\n" +
                                               "      mur.codRuolo = ?\n" + "Order By mu.posizione";

  private static final String SQL_MENU_BY_UTENTE = "select mu.*\n" +
                                                   "from sec_profili p, sec_Menuruoli mur, sec_dec_Menu mu\n" + "where p.CODRUOLO = mur.CODRUOLO And\n" +
                                                   "      mur.CODMenu = mu.CODMenu And\n" +
                                                   "      trunc(sysdate) between p.dataInizio and nvl(p.dataFine, to_date('31/12/9999', 'dd/mm/yyyy')) And\n" +
                                                   "      mur.flagaccesso = 'S' And\n" +
                                                   "      mu.flagvalido = 'S' And\n" +
                                                   "      p.idutente = ?\n" +
                                                   "Order by mu.posizione";

  private static final String SQL_MENU_UTENTE = "Select m.*,\n"
                                                + "       lib_sicurezza.GetruoliMenuUtente(m.codMenuUtente) Ruoli\n" + "from Sec_Dec_Menuutente m\n"
                                                + "/*W*/\n" + "Order By Posizione";

  private static final String SQL_MENU_UTENTE_RUOLO = "select mur.*,\n" + "       mu.Descrizionelunga Descrizione\n"
                                                      + "from Sec_Dec_Menuutente mu, Sec_Menuutenteruoli mur\n"
                                                      + "where mu.codmenuutente = mur.codmenuutente And\n" + "      mur.codRuolo = ?\n" + "Order By mu.posizione";

  private static final String SQL_MENU_UTENTE_BY_UTENTE = "select mu.*\n"
                                                          + "from sec_profili p, sec_MenuUtenteruoli mur, sec_dec_MenuUtente mu\n"
                                                          + "where p.CODRUOLO = mur.CODRUOLO And\n" + "      mur.CODMENUUTENTE = mu.CODMENUUTENTE And\n"
                                                          + "      trunc(sysdate) between p.dataInizio and nvl(p.dataFine, to_date('31/12/9999', 'dd/mm/yyyy')) And\n"
                                                          + "      mur.flagaccesso = 'S' And\n" + "      mu.flagvalido = 'S' And\n" + "      p.idutente = ?\n"
                                                          + "Order by mu.posizione";

  private static final String SQL_PROFILI = "Select *\n" +
                                            "From Sec_profili p\n" +
                                            "/*W*/\n" +
                                            "Order By CodRuolo ASC, DataInizio ASC";

  private static final String SQL_NORMALIZZA_FUNZIONI_RUOLI = "Insert Into Sec_Funzioniruoli\n" +
                                                              "     (Codfunzione,\n" +
                                                              "      Codruolo,\n" +
                                                              "      Flagaccesso)\n" +
                                                              "select f.Codfunzione, r.codruolo, 'N'\n" +
                                                              "from Sec_Dec_Funzioni f, Sec_dec_ruoli r\n" +
                                                              "Minus\n" +
                                                              "Select codFunzione, codruolo, 'N'\n" +
                                                              "from Sec_FunzioniRuoli";

  private static final String SQL_NORMALIZZA_MENU_RUOLI = "Insert Into Sec_Menuruoli\n" +
                                                          "     (Codmenu,\n" +
                                                          "      Codruolo,\n" +
                                                          "      Flagaccesso)\n" +
                                                          "select mu.codMenu, r.codruolo, 'N'\n" +
                                                          "from Sec_Dec_Menu mu, Sec_dec_ruoli r\n" +
                                                          "Minus\n" +
                                                          "Select codMenu, codruolo, 'N'\n" +
                                                          "from Sec_Menuruoli";

  private static final String SQL_NORMALIZZA_MENU_UTENTE_RUOLI = "Insert Into Sec_Menuutenteruoli\n" +
                                                                 "     (Codmenuutente,\n" +
                                                                 "      Codruolo,\n" +
                                                                 "      Flagaccesso)\n" +
                                                                 "select mu.codMenuUtente, r.codruolo, 'N'\n" +
                                                                 "from Sec_Dec_Menuutente mu, Sec_dec_ruoli r\n" +
                                                                 "Minus\n" +
                                                                 "Select codMenuUtente, codruolo, 'N'\n" +
                                                                 "from Sec_Menuutenteruoli";

  /**
   * Ritorna l'elenco utenti
   * 
   * @param nominativo
   * @return
   * @throws FrameworkException
   * @throws IOException
   * @throws SQLException
   */
  public static final Sec_utentiTableBean getTable(String nominativo) throws SQLException, IOException, FrameworkException {
    FilteredQuery query = new FilteredQuery(SQL_UTENTI);
    query.addFilter("upper(cognome) || upper(nome) like '%' || upper(?) || '%'", Types.VARCHAR, nominativo);

    return Sec_utentiTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Resituisce l'elenco delle funzioni
   * 
   * @return
   * @throws SQLException
   * @throws IOException
   * @throws FrameworkException
   */
  public static Sec_dec_funzioniTableBean getFunzioni() throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_FUNZIONI);

    return Sec_dec_funzioniTableBean.Factory.loadFromQuery(query, 10);
  }

  /**
   * Ritorna l'elenco delle funzioni legate ad un ruolo
   * 
   * @param codRuolo
   * @param funzioneRoot
   * @return
   * @throws FrameworkException
   * @throws IOException
   * @throws SQLException
   */
  public static final Sec_funzioniruoliTableBean getFunzioniRuoli(String codRuolo)
      throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_FUNZIONI_RUOLO);
    query.addParameter(Types.VARCHAR, codRuolo);

    return Sec_funzioniruoliTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Resituisce l'elenco delle voci del menu
   * 
   * @return
   * @throws FrameworkException
   * @throws IOException
   * @throws SQLException
   */
  public static Sec_dec_menuTableBean getMenu() throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_MENU);

    return Sec_dec_menuTableBean.Factory.loadFromQuery(query, 10);
  }

  /**
   * Resituisce l'elenco delle voci del menu di un ruolo
   * 
   * @param codRuolo
   * @return
   * @throws SQLException
   * @throws IOException
   * @throws FrameworkException
   */

  public static final Sec_menuruoliTableBean getMenuRuoli(String codRuolo)
      throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_MENU_RUOLO);

    query.addParameter(Types.VARCHAR, codRuolo);

    return Sec_menuruoliTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Resituisce l'elenco delle voci del menu utente
   * 
   * @return
   * @throws FrameworkException
   * @throws IOException
   * @throws SQLException
   */
  public static Sec_dec_menuutenteTableBean getMenuUtente() throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_MENU_UTENTE);

    return Sec_dec_menuutenteTableBean.Factory.loadFromQuery(query, 10);
  }

  /**
   * Resituisce l'elenco delle voci del menu utente di un ruolo
   * 
   * @param codRuolo
   * @return
   * @throws FrameworkException
   * @throws IOException
   * @throws SQLException
   */
  public static final Sec_menuutenteruoliTableBean getMenuUtenteRuoli(String codRuolo)
      throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_MENU_UTENTE_RUOLO);

    query.addParameter(Types.VARCHAR, codRuolo);

    return Sec_menuutenteruoliTableBean.Factory.loadFromQuery(query);
  }

  public static final Sec_profiliTableBean loadProfili(BigDecimal idUtente)
      throws SQLException, IOException, FrameworkException {
    FilteredQuery query = new FilteredQuery(SQL_PROFILI);
    query.addFilter("p.idUtente = ?", Types.INTEGER, idUtente);

    return Sec_profiliTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Seleziona l'utente passato
   * 
   * @param userid
   * @return
   * @throws FrameworkException
   * @throws IOException
   * @throws SQLException
   */
  public static final Sec_utentiRowBean loadUser(String userid) throws SQLException, IOException, FrameworkException {
    FilteredQuery query = new FilteredQuery(SQL_UTENTI);
    query.addFilter("upper(userid) = upper(?)", Types.VARCHAR, BaseFunction.nvl(userid, " "));

    return Sec_utentiRowBean.Factory.load(query);
  }

  public static void checkWebDesktop(WebDesktopDto webDesktopDto, String userName) throws SQLException, IOException, FrameworkException {
    if (webDesktopDto.getUser() != null && webDesktopDto.getUser().getId() != null) {
      return;
    }

    log.info("Caricamento Framework Session Context");

    // Utente Framework
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = profile(auth.getName());
    webDesktopDto.setUser(user);

    // Inizializzo le ricerche
    MenuSearcher menuSearcher = new MenuSearcher(user.getMenu());
    MenuSearcher userMenuSearcher = new MenuSearcher(user.getUserMenu());

    webDesktopDto.getSearchManager().clean();
    webDesktopDto.getSearchManager().addSearcher(menuSearcher, SearchRelevance.VERY_HIGH);
    webDesktopDto.getSearchManager().addSearcher(userMenuSearcher, SearchRelevance.VERY_HIGH);
  }

  public static final User profile(String userid) throws SQLException, IOException, FrameworkException {
    User user = new User();

    FilteredQuery query = new FilteredQuery(SQL_UTENTI);
    query.addFilter("upper(userid) = upper(?)", Types.VARCHAR, BaseFunction.nvl(userid, " "));
    Sec_utentiRowBean utentiRowBean = Sec_utentiRowBean.Factory.load(query);

    if (utentiRowBean != null && utentiRowBean.getIdutente() != null) {

      byte[] foto = utentiRowBean.getFoto();
      boolean hasAvatar = false;
      if (foto != null && foto.length != 0) {
        hasAvatar = true;
      }

      user = new User();
      user.setId(utentiRowBean.getIdutente());
      user.setUserid(utentiRowBean.getUserid());
      user.setSurname(utentiRowBean.getCognome());
      user.setName(utentiRowBean.getNome());
      user.setEmail(utentiRowBean.getEmail());
      user.setEmailPassword(utentiRowBean.getEmailpassword());
      user.setLocale(LocaleUtils.toLocale(utentiRowBean.getLocale()));
      user.setAvatar(hasAvatar);

      // Aggiorno le abilitazioni alle funzioni
      loadFunction(user);

      // Aggiorno il menu
      loadMenu(user);

      // Aggiorno il menu utente
      loadMenuUtente(user);
    }

    return user;
  }

  /**
   * Carica il profilo dell'utente - funzioni
   * 
   * @param user
   * @throws SQLException
   * @throws IOException
   */
  public static final void loadFunction(User user) throws SQLException, IOException, FrameworkException {
    // Aggiorno le abilitazioni alle funzioni
    Query query = new Query(SQL_FUNZIONI_BY_UTENTE);
    query.addParameter(Types.INTEGER, user.getId());

    DataTable<?> table = query.selectTable();
    for (DataRow row : table) {
      user.addFunction(row.getString("CODFUNZIONE"));
    }
  }

  /**
   * Aggiorna il profilo dell'utente - Menu
   * 
   * @param user
   * @throws SQLException
   * @throws FrameworkException
   * @throws IOException
   */
  public static final void loadMenu(User user) throws SQLException, IOException, FrameworkException {
    // Aggiorno il menu
    Query query = new Query(SQL_MENU_BY_UTENTE);
    query.addParameter(Types.INTEGER, user.getId());

    Sec_dec_menuTableBean menuTableBean = Sec_dec_menuTableBean.Factory.loadFromQuery(query);

    Menu appMenu = new Menu();
    for (Sec_dec_menuRowBean row : menuTableBean) {
      if (row.getLivello().intValue() == 1) {
        appMenu = new Menu(row.getCodmenu(), VoiceType.valueOf(row.getCodtipovoce()), row.getDescrizionebreve(),
            row.getDescrizionelunga(), row.getImghtml(), row.getLink());

        user.getMenu().add(appMenu);
      } else {
        appMenu.addChild(row.getCodmenu(), VoiceType.valueOf(row.getCodtipovoce()), row.getDescrizionebreve(),
            row.getDescrizionelunga(), row.getImghtml(), row.getLink());
      }
    }
  }

  /**
   * Aggiorna il profilo dell'utente - Menu utente
   * 
   * @param user
   * @throws SQLException
   * @throws FrameworkException
   * @throws IOException
   */
  public static final void loadMenuUtente(User user) throws SQLException, IOException, FrameworkException {
    Query query = new Query(SQL_MENU_UTENTE_BY_UTENTE);
    query.addParameter(Types.INTEGER, user.getId());

    Sec_dec_menuutenteTableBean table = Sec_dec_menuutenteTableBean.Factory.loadFromQuery(query);
    for (Sec_dec_menuutenteRowBean row : table) {
      user.getUserMenu().addChild(row.getCodmenuutente(), VoiceType.valueOf(row.getCodtipovoce()),
          row.getDescrizionebreve(), row.getDescrizionelunga(), row.getImghtml(), row.getLink());
    }
  }

  /**
   * Normalizza le tabelle di sicurezza
   *
   * @throws SQLException
   * @throws FrameworkException
   */
  public static void normalizzaSicurezza() throws SQLException, FrameworkException {
    Query query = new Query(SQL_NORMALIZZA_FUNZIONI_RUOLI);
    query.execute();

    query = new Query(SQL_NORMALIZZA_MENU_RUOLI);
    query.execute();

    query = new Query(SQL_NORMALIZZA_MENU_UTENTE_RUOLI);
    query.execute();
  }

}
