package es.viferpar.microservices.currencyconversionservice.bean;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CurrencyConversion {

  private Long id;

  private String from;

  private String to;

  private BigDecimal quantity;

  private BigDecimal conversionMultiple;

  private BigDecimal totalCalculatedAmount;

  private String enviroment;

}
