/**
 * 
 */
package com.mock.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Muruganand
 * list of Stores input details
 */
@Getter
@Setter
@AllArgsConstructor
public class Stores {
	int id;  
    String name; 
    BigDecimal totalSales; 
    BigDecimal returns;
	}
