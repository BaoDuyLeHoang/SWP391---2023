package com.example.projectswp.model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Blog {
    private int blogId;
    private int blogCategoryId;
    private int userId;
    private String blogContent;
    private String blogTitle;
    private String blogDescription;
    private Date blogDateCreate;
    private Date blogDateUpdate;
    private boolean blogStatus;
}
