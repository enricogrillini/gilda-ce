package it.itdistribuzione.gilda.common.application;

import it.eg.sloth.webdesktop.dto.notificationcenter.impl.BaseMessage;

public class HistoryMessage extends BaseMessage {

  public HistoryMessage(String textSearch, String url) {
    this(textSearch, null, url);
  }

  public HistoryMessage(String textSearch, String description, String url) {
    super(textSearch, textSearch, null, description, false, url);
  }
}
