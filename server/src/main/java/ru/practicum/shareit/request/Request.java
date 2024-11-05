package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "requests")
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Request {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String description;
    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime created;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /*@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private List<Item> items = new ArrayList<>();*/
}
