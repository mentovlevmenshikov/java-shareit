package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@Entity
@Table(name = "Items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "is_available")
    private boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
