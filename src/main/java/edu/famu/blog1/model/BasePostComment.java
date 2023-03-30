package edu.famu.blog1.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.protobuf.util.Timestamps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


import com.google.cloud.Timestamp;
import java.text.ParseException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePostComment {
    @DocumentId
    protected @Nullable String commentId;
    protected String content;
    protected @Nullable Timestamp createdAt;
    protected boolean published;
    protected @Nullable Timestamp publishedAt;
    protected String title;

    public boolean getPublished() {
        return published;
    }

    public void setCreatedAt(String createdAt) throws ParseException {
        this.createdAt = Timestamp.fromProto(Timestamps.parse(createdAt));
    }

    public void setPublishedAt(String publishedAt) throws ParseException {
        this.publishedAt = Timestamp.fromProto(Timestamps.parse(publishedAt));
    }
}
