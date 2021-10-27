package nl.leonw.supplierservice.impl;

// JPA cannot handle records so I will not use them here.
// I want to conform to the JPA specs for now.

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DBSupplier {
    Long id;
    String companyName;
    String marketingName;
    LocalDateTime lastChanged;
    String lastChangedBy;
}
