package com.example.contentpub.internal.domain.db.projection;

import java.util.Date;

public interface ContentDbView {

    Integer getId();

    String getTitle();

    String getSummary();

    String getDetails();

    Date getCreatedAt();

    Date getUpdatedAt();

    Integer getWriterId();

    String getWriterName();

}
