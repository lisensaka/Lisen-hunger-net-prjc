package app.hungernet.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String uuid;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

//    @CreatedBy
//    @Column(updatable = false)
//    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @LastModifiedBy
//    private String updatedBy;
}
