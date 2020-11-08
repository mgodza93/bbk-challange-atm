package bbk.challenge.atm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "transaction")
@ApiObject(name = "Transaction", description = "Transaction entity used to store the adding of cash and the withdraws")
@Builder
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    @ApiObjectField
    private String userName;

    @ApiObjectField
    private Integer noBills;

    @ApiObjectField
    private Long amount;

    @ApiObjectField
    private Long timestamp;

    @ApiObjectField
    @Enumerated(EnumType.STRING) private TransactionType transactionType;
}
