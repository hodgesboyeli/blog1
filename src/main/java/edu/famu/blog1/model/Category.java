package edu.famu.blog1.model;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @DocumentId
    private @Nullable String categoryId;
    private String content;
    private String metaTitle;
    private String slug;
    private String title;


}
