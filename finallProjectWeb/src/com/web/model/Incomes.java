package com.web.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "incomesList")
@XmlAccessorType (XmlAccessType.FIELD)
public class Incomes
{
    @XmlElement(name="income")
    private List<Income> incomesList;

	public List<Income> getIncomesList() {
		return incomesList;
	}

	public void setIncomesList(List<Income> incomesList) {
		this.incomesList = incomesList;
	}
 
   
}