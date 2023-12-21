package db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Integer id;

    private String name;

    private Integer priority;
}
