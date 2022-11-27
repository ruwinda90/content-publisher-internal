package com.example.contentpub.internal.domain.db.projection;

import java.util.Date;

public interface CompactContentDbView {

    Integer getId();

    String getTitle();

    String getSummary();

    Date getCreatedAt();

    Date getUpdatedAt();

    Integer getWriterId();

    String getWriterName();

}
