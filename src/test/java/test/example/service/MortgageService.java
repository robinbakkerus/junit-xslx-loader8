package test.example.service;

import test.example.data.House;
import test.example.data.Mortgage;
import test.example.data.MortgageProductType;
import test.example.data.Person;

public interface MortgageService {

	Mortgage calculate(Person client, House house, MortgageProductType type);
}
