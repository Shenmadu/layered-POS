package dto.tm;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrdersTm extends RecursiveTreeObject<OrdersTm> {
    private String orderId;
    private String date;
    private String custId;
}
