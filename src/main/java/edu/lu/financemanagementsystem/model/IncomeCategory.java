package edu.lu.financemanagementsystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "income_categories")
public class IncomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_id", updatable = false)
    private Long authorId; // NULL = predefined (build in) category
    @Column(nullable = false)
    private String title;
    @Column
    private String description;
    @Column
    private String color;
    @Column(name = "icon_name")
    private String iconName;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public enum CategoryColors {
        Uncategorized("#9f9f9f"),
        Salary("#37bf72"),
        Loan("#c8e645"),
        Refund("#81bcc9"),
        Interest("#1abd84"),
        Gift("#2aa7c9"),
        Transfer("#b596ab"),
        Other("#7f6261");

        private final String value;

        CategoryColors(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
