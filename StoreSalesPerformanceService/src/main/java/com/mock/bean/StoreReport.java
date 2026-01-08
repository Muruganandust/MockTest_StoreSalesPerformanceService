package com.mock.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Class contains the store report details
 * @author Muruganand
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class StoreReport {
	int id;  
    String name; 
    BigDecimal revenue;
	String rating;
}
