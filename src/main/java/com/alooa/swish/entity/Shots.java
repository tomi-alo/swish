package com.alooa.swish.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "shots")
public class Shots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // automatically starts at 1 and auto increments per shots log
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(nullable = false)
    private int shotsTaken;

    @Column(nullable = false)
    private int shotsMade;

    @Column(length = 255)
    private String notes;

    private Instant timestamp = Instant.now();

    protected Shots() {}

    public Shots(Category category, Location location, int shotsTaken, int shotsMade, String notes, Instant timestamp) {
        this.category = category;
        this.location = location;
        this.shotsTaken = shotsTaken;
        this.shotsMade = shotsMade;
        this.timestamp = timestamp;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getShotsTaken() {
        return shotsTaken;
    }

    public void setShotsTaken(int shotsTaken) {
        this.shotsTaken = shotsTaken;
    }

    public int getShotsMade() {
        return shotsMade;
    }

    public void setShotsMade(int shotsMade) {
        this.shotsMade = shotsMade;
    }

    public double getShootingPercentage() {
        if (shotsTaken == 0) {
            return 0.0;
        }
        return ((double) shotsMade / shotsTaken) * 100;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
