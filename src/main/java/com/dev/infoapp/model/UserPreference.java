package com.dev.infoapp.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_preferences")
public class UserPreference {
    @Id
    private Long id;

    @Column("user_id")  
    private Long userId; 
    
    private String preferredCategory;
    private String preferredSource;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
