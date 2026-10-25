package com.alooa.swish.model;

import com.alooa.swish.exception.InvalidShotsException;
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
    private Integer shotsTaken;

    @Column(nullable = false)
    private Integer shotsMade;

    @Column(length = 255)
    private String notes;

    private Instant timestamp = Instant.now();

    protected Shots() {}

    public Shots(Category category, Location location, Integer shotsTaken, Integer shotsMade, String notes, Instant timestamp) {
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

    public Integer getShotsTaken() {
        return shotsTaken;
    }

    public void setShotsTaken(Integer shotsTaken) {
        this.shotsTaken = shotsTaken;
    }

    public Integer getShotsMade() {
        return shotsMade;
    }

    public void setShotsMade(Integer shotsMade) {
        this.shotsMade = shotsMade;
    }

    public double getShootingPercentage() {
        if (shotsTaken == 0) {
            return 0.0;
        }
        return ((double) shotsMade / shotsTaken) * 100;
    }

    private record Thresholds(double excellent, double good) { }

    private Thresholds thresholds(Category category) {
        return switch (category) {
            case FREE_THROW, LAYUP -> new Thresholds(80, 70);
            case THREE -> new Thresholds(50, 35);
            case MIDDY, DUNK -> new Thresholds(60, 40);
        };
    }

    public String getAssessment() {
        Thresholds thresholds = thresholds(category);
        double percentage = getShootingPercentage();

        if (percentage >= thresholds.excellent()) {
            return "Excellent shooting performance!";
        } else if (percentage >= thresholds.good()) {
            return "Good shooting performance.";
        } else {
            return "Needs improvement, keep working.";
        }
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

    public void checkIfValid() {
        if (shotsTaken <= 0){
            throw new InvalidShotsException("Shots taken cannot be less than or equal to zero");
        }

        if (shotsMade < 0) {
            throw new InvalidShotsException("Shots made cannot be less than zero");
        }
        if (shotsMade > shotsTaken) {
            throw new InvalidShotsException("Shots made cannot be greater than shots taken");
        }

    }
}
