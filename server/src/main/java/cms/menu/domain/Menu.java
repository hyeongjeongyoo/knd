package cms.menu.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "url")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('LINK','FOLDER','BOARD','CONTENT','PROGRAM')")
    private MenuType type;

    @Column(length = 255)
    private String url;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "display_position", nullable = false, length = 50)
    private String displayPosition;

    @Column(name = "visible")
    private Boolean visible;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Builder.Default
    private List<Menu> children = new ArrayList<>();

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "created_ip", length = 45)
    private String createdIp;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "updated_ip", length = 45)
    private String updatedIp;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(String name, MenuType type, String url, Long targetId,
            String displayPosition, Boolean visible, Integer sortOrder,
            Long parentId) {
        if (name != null)
            this.name = name;
        if (type != null)
            this.type = type;
        if (url != null)
            this.url = url;
        if (targetId != null)
            this.targetId = targetId;
        if (displayPosition != null)
            this.displayPosition = displayPosition;
        if (visible != null)
            this.visible = visible;
        if (sortOrder != null)
            this.sortOrder = sortOrder;
        if (parentId != null)
            this.parentId = parentId;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void updateTargetId(Long targetId) {
        this.targetId = targetId;
    }
}