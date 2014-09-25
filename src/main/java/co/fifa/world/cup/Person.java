////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2014, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////
// $Id$
// $Revision$
// $Date$
// $Author$
////////////////////////////////////////////////////////////////////////////////
package co.fifa.world.cup;

import java.io.Serializable;
import java.math.BigDecimal;


public class Person implements Serializable {
    
private static final long serialVersionUID = -5527566248002296042L;
    
  //  private String pid;
    private String name;
    private Double amount;
    private String country;
    private BigDecimal totalAmount;
    private BigDecimal winningAmount;
    
    
    public BigDecimal getWinningAmount() {
        return winningAmount;
    }
    
    public void setWinningAmount(BigDecimal winningAmount) {
        this.winningAmount = winningAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    

}
