package me.spiochu.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "E-mail is mandatory")
    @Email(message = "Invalid e-mail")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password is to short")
    private String password;
    @Transient
    @NotBlank(message = "Repeated password is mandatory")
    @Size(min = 6, message = "Repeated password is to short")
    private String password_confirm;
    private String registrationToken;
    private LocalDateTime register_date = LocalDateTime.now();
    private boolean activity = false;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password_confirm='" + password_confirm + '\'' +
                ", registrationToken='" + registrationToken + '\'' +
                ", register_date=" + register_date +
                ", activity=" + activity +
                ", roles=" + roles +
                '}';
    }
}