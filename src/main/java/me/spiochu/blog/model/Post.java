package me.spiochu.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Type(type = "text")
    private String content;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated
    private CategoryEnum category;

    @JsonIgnore
    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<>();

    public Post(String title, String content, User user, CategoryEnum category) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.category = category;
    }

}
