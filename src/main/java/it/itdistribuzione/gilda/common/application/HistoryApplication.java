package it.itdistribuzione.gilda.common.application;

import it.eg.sloth.webdesktop.dto.notificationcenter.impl.BaseApplication;

public class HistoryApplication extends BaseApplication<HistoryMessage> {

  public static final String NAME = "CRONOLOGIA";

  public HistoryApplication() {
    super(NAME, "Cronologia", null, true);
  }

  public void addFirstMessage(String textSearch, String url) {
    addFirstMessage(textSearch, null, url);
  }

  public void addFirstMessage(String textSearch, String description, String url) {
    super.addFirstMessage(new HistoryMessage(textSearch, description, url));

    if (size() >= 10) {
      removeMessage(size() - 1);
    }
  }

}
