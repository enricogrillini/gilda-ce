package it.itdistribuzione.gilda.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.LocaleUtils;

import it.eg.sloth.db.datasource.DataRow;
import it.eg.sloth.db.datasource.DataTable;
import it.eg.sloth.db.query.filteredquery.FilteredQuery;
import it.eg.sloth.db.query.query.Query;
import it.eg.sloth.framework.common.base.BaseFunction;
import it.eg.sloth.framework.security.Menu;
import it.eg.sloth.framework.security.User;
import it.eg.sloth.framework.security.VoiceType;
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
public class SecurityDao {

  private static final String SQL_UTENTI = "Select *\n" +
                                           "from sec_utenti\n" +
                                           "/*W*/" +
                                           "Order By Cognome, Nome";

  private static final String SQL_FUNZIONI = "Select f.*,\n" +
                                             "       lib_sicurezza.GetruoliFunzione(f.codFunzione) Ruoli\n" +
                                             "from sec_dec_funzioni f\n" +
                                             "/*W*/\n" +
                                             "Order By Posizione";

  private static final String SQL_FUNZIONI_RUOLO = "select fr.*,\n" +
                                                   "       f.Descrizionelunga Descrizione\n" +
                                                   "from Sec_Dec_Funzioni f, Sec_Funzioniruoli fr\n" +
                                                   "where f.codfunzione = fr.codfunzione And\n" +
                                                   "      fr.codRuolo = ?\n" +
                                                   "Order By f.posizione";

  private static final String SQL_FUNZIONI_BY_UTENTE = "Select f.CODFUNZIONE\n" +
                                                       "From sec_profili p, sec_funzioniruoli fr, sec_dec_funzioni f\n" +
                                                       "Where p.CODRUOLO = fr.CODRUOLO And\n" +
                                                       "      fr.CODFUNZIONE = f.CODFUNZIONE And\n" +
                                                       "      trunc(sysdate) between p.dataInizio and nvl(p.dataFine, to_date('31/12/9999', 'dd/mm/yyyy')) And\n" +
                                                       "      fr.flagaccesso = 'S' And\n" +
                                                       "      f.flagvalido = 'S' And\n" +
                                                       "      p.idutente = ?";

  private static final String SQL_NORMALIZZA_FUNZIONI_RUOLI = "Insert Into Sec_Funzioniruoli\n" +
                                                              "     (Codfunzione,\n" +
                                                              "      Codruolo,\n" +
                                                              "      Flagaccesso)\n" +
                                                              "select f.Codfunzione, r.codruolo, 'N'\n" +
                                                              "from Sec_Dec_Funzioni f, Sec_dec_ruoli r\n" +
                                                              "Minus\n" +
                                                              "Select codFunzione, codruolo, 'N'\n" +
                                                              "from Sec_FunzioniRuoli";

  private static final String SQL_MENU = "Select m.*,\n" +
                                         "       lib_sicurezza.GetruoliMenu(m.codMenu) Ruoli\n" +
                                         "from Sec_Dec_Menu m\n" +
                                         "/*W*/" +
                                         "Order By Posizione";

  private static final String SQL_MENU_RUOLO = "select mur.*,\n" +
                                               "       rpad(' ', (livello - 1) * 3) || mu.Descrizionelunga Descrizione\n" +
                                               "from Sec_Dec_Menu mu, Sec_Menuruoli mur\n" +
                                               "where mu.codmenu = mur.codmenu And\n" +
                                               "      mur.codRuolo = ?\n" +
                                               "Order By mu.posizione";

  private static final String SQL_MENU_BY_UTENTE = "select mu.*\n" +
                                                   "from sec_profili p, sec_Menuruoli mur, sec_dec_Menu mu\n" +
                                                   "where p.CODRUOLO = mur.CODRUOLO And\n" +
                                                   "      mur.CODMenu = mu.CODMenu And\n" +
                                                   "      trunc(sysdate) between p.dataInizio and nvl(p.dataFine, to_date('31/12/9999', 'dd/mm/yyyy')) And\n" +
                                                   "      mur.flagaccesso = 'S' And\n" +
                                                   "      mu.flagvalido = 'S' And\n" +
                                                   "      p.idutente = ?\n" +
                                                   "Order by mu.posizione";

  private static final String SQL_NORMALIZZA_MENU_RUOLI = "Insert Into Sec_Menuruoli\n" +
                                                          "     (Codmenu,\n" +
                                                          "      Codruolo,\n" +
                                                          "      Flagaccesso)\n" +
                                                          "select mu.codMenu, r.codruolo, 'N'\n" +
                                                          "from Sec_Dec_Menu mu, Sec_dec_ruoli r\n" +
                                                          "Minus\n" +
                                                          "Select codMenu, codruolo, 'N'\n" +
                                                          "from Sec_Menuruoli";

  private static final String SQL_MENU_UTENTE = "Select m.*,\n" +
                                                "       lib_sicurezza.GetruoliMenuUtente(m.codMenuUtente) Ruoli\n" +
                                                "from Sec_Dec_Menuutente m\n" +
                                                "/*W*/\n" +
                                                "Order By Posizione";

  private static final String SQL_MENU_UTENTE_RUOLO = "select mur.*,\n" +
                                                      "       mu.Descrizionelunga Descrizione\n" +
                                                      "from Sec_Dec_Menuutente mu, Sec_Menuutenteruoli mur\n" +
                                                      "where mu.codmenuutente = mur.codmenuutente And\n" +
                                                      "      mur.codRuolo = ?\n" +
                                                      "Order By mu.posizione";

  private static final String SQL_MENU_UTENTE_BY_UTENTE = "select mu.*\n" +
                                                          "from sec_profili p, sec_MenuUtenteruoli mur, sec_dec_MenuUtente mu\n" +
                                                          "where p.CODRUOLO = mur.CODRUOLO And\n" +
                                                          "      mur.CODMENUUTENTE = mu.CODMENUUTENTE And\n" +
                                                          "      trunc(sysdate) between p.dataInizio and nvl(p.dataFine, to_date('31/12/9999', 'dd/mm/yyyy')) And\n" +
                                                          "      mur.flagaccesso = 'S' And\n" +
                                                          "      mu.flagvalido = 'S' And\n" +
                                                          "      p.idutente = ?\n" +
                                                          "Order by mu.posizione";

  private static final String SQL_NORMALIZZA_MENU_UTENTE_RUOLI = "Insert Into Sec_Menuutenteruoli\n" +
                                                                 "     (Codmenuutente,\n" +
                                                                 "      Codruolo,\n" +
                                                                 "      Flagaccesso)\n" +
                                                                 "select mu.codMenuUtente, r.codruolo, 'N'\n" +
                                                                 "from Sec_Dec_Menuutente mu, Sec_dec_ruoli r\n" +
                                                                 "Minus\n" +
                                                                 "Select codMenuUtente, codruolo, 'N'\n" +
                                                                 "from Sec_Menuutenteruoli";

  private static final String SQL_PROFILI = "Select *\n" +
                                            "From Sec_profili p\n" +
                                            "/*W*/\n" +
                                            "Order By CodRuolo ASC, DataInizio ASC";

  /**
   * Ritorna l'elenco utenti
   * 
   * @param nominativo
   * @return
   */
  public static final Sec_utentiTableBean getTable(String nominativo) {
    FilteredQuery query = new FilteredQuery(SQL_UTENTI);
    query.addFilter("upper(cognome) || upper(nome) like '%' || upper(?) || '%'", Types.VARCHAR, nominativo);

    return Sec_utentiTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Resituisce l'elenco delle funzioni
   * 
   * @param funzione
   * @return
   */
  public static Sec_dec_funzioniTableBean getFunzioni() {
    Query query = new Query(SQL_FUNZIONI);

    return Sec_dec_funzioniTableBean.Factory.loadFromQuery(query, 10);
  }

  /**
   * Ritorna l'elenco delle funzioni legate ad un ruolo
   * 
   * @param codRuolo
   * @param funzioneRoot
   * @return
   */
  public static final Sec_funzioniruoliTableBean getFunzioniRuoli(String codRuolo) {
    Query query = new Query(SQL_FUNZIONI_RUOLO);
    query.addParameter(Types.VARCHAR, codRuolo);

    return Sec_funzioniruoliTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Normalizza la tabella Sec_FunzioniRuoli
   * 
   * @throws SQLException
   */
  public static void normalizzaFunzioniRuoli() throws SQLException {
    Query query = new Query(SQL_NORMALIZZA_FUNZIONI_RUOLI);
    query.execute();
  }

  /**
   * Resituisce l'elenco delle voci del menu
   * 
   * @return
   */
  public static Sec_dec_menuTableBean getMenu() {
    Query query = new Query(SQL_MENU);

    return Sec_dec_menuTableBean.Factory.loadFromQuery(query, 10);
  }

  /**
   * Resituisce l'elenco delle voci del menu di un ruolo
   * 
   * @param codRuolo
   * @return
   */
  public static final Sec_menuruoliTableBean getMenuRuoli(String codRuolo) {
    Query query = new Query(SQL_MENU_RUOLO);

    query.addParameter(Types.VARCHAR, codRuolo);

    return Sec_menuruoliTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Normalizza la tabella Sec_MenuutenteRuoli
   * 
   * @throws SQLException
   */
  public static void normalizzaMenuRuoli() throws SQLException {
    Query query = new Query(SQL_NORMALIZZA_MENU_RUOLI);
    query.execute();
  }

  /**
   * Resituisce l'elenco delle voci del menu utente
   * 
   * @return
   */
  public static Sec_dec_menuutenteTableBean getMenuUtente() {
    Query query = new Query(SQL_MENU_UTENTE);

    return Sec_dec_menuutenteTableBean.Factory.loadFromQuery(query, 10);
  }

  /**
   * Normalizza la tabella Sec_Menuutenteruoli
   * 
   * @throws SQLException
   */
  public static void normalizzaMenuUtenteRuoli() throws SQLException {
    Query query = new Query(SQL_NORMALIZZA_MENU_UTENTE_RUOLI);
    query.execute();
  }

  /**
   * Resituisce l'elenco delle voci del menu utente di un ruolo
   * 
   * @param codRuolo
   * @return
   */
  public static final Sec_menuutenteruoliTableBean getMenuUtenteRuoli(String codRuolo) {
    Query query = new Query(SQL_MENU_UTENTE_RUOLO);

    query.addParameter(Types.VARCHAR, codRuolo);

    return Sec_menuutenteruoliTableBean.Factory.loadFromQuery(query);
  }

  public static final Sec_profiliTableBean loadProfili(BigDecimal idUtente) {
    FilteredQuery query = new FilteredQuery(SQL_PROFILI);
    query.addFilter("p.idUtente = ?", Types.INTEGER, idUtente);

    return Sec_profiliTableBean.Factory.loadFromQuery(query);
  }

  /**
   * Effettua la login e ritorna l'utente DB associato
   * 
   * @param userid
   * @param password
   * @return
   */
  private static final Sec_utentiRowBean select(String userid, String password) {
    FilteredQuery query = new FilteredQuery(SQL_UTENTI);
    query.addFilter("upper(userid) = upper(?)", Types.VARCHAR, BaseFunction.nvl(userid, " "));
    query.addFilter("upper(password) = upper(?)", Types.VARCHAR, BaseFunction.nvl(password, " "));

    return Sec_utentiRowBean.Factory.load(query);
  }

  public static final User login(String userid, String password) throws SQLException, IOException {
    User user = new User();

    Sec_utentiRowBean utentiRowBean = SecurityDao.select(userid, password);
    if (utentiRowBean != null && utentiRowBean.getIdutente() != null) {

      byte[] foto = utentiRowBean.getFoto();
      boolean hasAvatar = false;
      if (foto != null && foto.length != 0) {
        hasAvatar = true;
      }

      user = new User(utentiRowBean.getIdutente(),
          utentiRowBean.getUserid(),
          utentiRowBean.getCognome(),
          utentiRowBean.getNome(),
          utentiRowBean.getEmail(),
          utentiRowBean.getEmailpassword(),
          LocaleUtils.toLocale(utentiRowBean.getLocale()),
          hasAvatar,
          null);

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
  public static final void loadFunction(User user) throws SQLException, IOException {
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
   */
  public static final void loadMenu(User user) throws SQLException {
    // Aggiorno il menu
    Query query = new Query(SQL_MENU_BY_UTENTE);
    query.addParameter(Types.INTEGER, user.getId());

    Sec_dec_menuTableBean menuTableBean = Sec_dec_menuTableBean.Factory.loadFromQuery(query);

    Menu appMenu = new Menu();
    for (Sec_dec_menuRowBean row : menuTableBean) {
      if (row.getLivello().intValue() == 1) {
        appMenu = new Menu(row.getCodmenu(),
            VoiceType.valueOf(row.getCodtipovoce()),
            row.getDescrizionebreve(),
            row.getDescrizionelunga(),
            row.getImghtml(),
            row.getLink());

        user.getMenu().add(appMenu);
      } else {
        appMenu.addChild(
            row.getCodmenu(),
            VoiceType.valueOf(row.getCodtipovoce()),
            row.getDescrizionebreve(),
            row.getDescrizionelunga(),
            row.getImghtml(),
            row.getLink());
      }
    }
  }

  /**
   * Aggiorna il profilo dell'utente - Menu utente
   * 
   * @param user
   * @throws SQLException
   */
  public static final void loadMenuUtente(User user) throws SQLException {
    Query query = new Query(SQL_MENU_UTENTE_BY_UTENTE);
    query.addParameter(Types.INTEGER, user.getId());

    Sec_dec_menuutenteTableBean table = Sec_dec_menuutenteTableBean.Factory.loadFromQuery(query);
    for (Sec_dec_menuutenteRowBean row : table) {
      user.getUserMenu().addChild(
          row.getCodmenuutente(),
          VoiceType.valueOf(row.getCodtipovoce()),
          row.getDescrizionebreve(),
          row.getDescrizionelunga(),
          row.getImghtml(),
          row.getLink());
    }
  }

}
