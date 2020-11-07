package bbk.challenge.atm.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    private String userName;

    private Integer noBills;

    private Long amount;

    private Long timestamp;

    @Enumerated(EnumType.STRING) private TransactionType transactionType;

    public Transaction(String userName, Integer addingNoBills, Integer addingAmount, long currentTimeMillis,
            TransactionType transactionType) {
    }
}
