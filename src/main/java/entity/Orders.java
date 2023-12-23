package entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Orders {
    private String orderId;
    private String date;
    private String customerId;
}
