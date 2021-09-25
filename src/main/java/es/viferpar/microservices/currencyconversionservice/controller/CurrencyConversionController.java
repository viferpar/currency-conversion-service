package es.viferpar.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import es.viferpar.microservices.currencyconversionservice.bean.CurrencyConversion;
import es.viferpar.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrencyConversionController {

  private final CurrencyExchangeProxy proxy;

  @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
  public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
      @PathVariable String to, @PathVariable BigDecimal quantity) {

    final HashMap<String, String> uriVariables = new HashMap<>();
    uriVariables.put("from", from);
    uriVariables.put("to", to);

    final ResponseEntity<CurrencyConversion> conversionResponse = new RestTemplate().getForEntity(
        "http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
        uriVariables);

    final CurrencyConversion conversion = conversionResponse.getBody();

    return CurrencyConversion.builder().id(10001L).from(from).to(to).quantity(quantity)
        .conversionMultiple(conversion.getConversionMultiple())
        .totalCalculatedAmount(quantity.multiply(conversion.getConversionMultiple()))
        .enviroment(String.format("%s rest template", conversion.getEnviroment())).build();

  }

  @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
  public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,
      @PathVariable String to, @PathVariable BigDecimal quantity) {

    final CurrencyConversion exchange = proxy.retrieveExchangeValue(from, to);

    return CurrencyConversion.builder().id(10001L).from(from).to(to).quantity(quantity)
        .conversionMultiple(exchange.getConversionMultiple())
        .totalCalculatedAmount(quantity.multiply(exchange.getConversionMultiple()))
        .enviroment(String.format("%s feign", exchange.getEnviroment())).build();

  }

}
