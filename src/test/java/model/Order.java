package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Ingredients ingredients;
    private String _id;
    private String status;
    private Integer number;
    private String createdAt;
    private String updatedAt;
}
