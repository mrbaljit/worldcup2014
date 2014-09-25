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

import java.util.List;


public class PersonJSONBean {
    
    private String totalAmount;

    
    public String getTotalAmount() {
        return totalAmount;
    }



    
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }



    private List<Person> bets;

    public List<Person> getBets() {
        return bets;
    }


    
    public void setBets(List<Person> bets) {
        this.bets = bets;
    }
    

}
