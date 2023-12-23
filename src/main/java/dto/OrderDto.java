package dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto {
    private String orderId;
    private String date;
    private String custId;
    private List<OrderDetailsDto> list;
}
