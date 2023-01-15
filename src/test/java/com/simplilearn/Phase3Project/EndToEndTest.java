package com.simplilearn.Phase3Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class EndToEndTest {
	Response response;
	String baseURI = "http://localhost:3000";
	RequestSpecification request = RestAssured.given();
	Map<String, Object> MapObj = new HashMap<String, Object>();

	@Test
	public void test1() {
		System.out.println("Get All Employees");
		response = GetAllEmployee();
		Assert.assertEquals(200, response.statusCode());

		System.out.println("The new Employee is");
		response = CreateEmployee("Ram", "10000");
		JsonPath jpath = response.jsonPath();
		int empId = jpath.get("id");
		Assert.assertEquals(201, response.getStatusCode());

		response = GetSingleEmployee(empId);
		JsonPath jpath2 = response.jsonPath();
		String name = jpath2.get("name");
		System.out.println("The name is :" + name);
		Assert.assertEquals(name, "Ram");
		Assert.assertEquals(200, response.getStatusCode());

		System.out.println("The update name is: ");
		response = UpdateEmployee(empId, 8000, "Sawan");
		System.out.println(response.getBody().asString());

		response = GetSingleEmployee(empId);
		JsonPath jPath3 = response.jsonPath();
		String names2 = jPath3.get("name");
		System.out.println("The name is " + names2);
		Assert.assertEquals(names2, "sawan");
		Assert.assertEquals(200, response.statusCode());

		response = DeleteEmployee(empId);
		Assert.assertEquals(200, response.statusCode());

		response = GetSingleEmployee(empId);
		Assert.assertEquals(404, response.statusCode());
		System.out.println("To get All the Employees");
		response = GetAllEmployee();
		JsonPath jPath4 = response.jsonPath();
		List<String> Ids = jPath4.get("id");
		Assert.assertFalse(Ids.contains(String.valueOf(empId)));

	}

	public Response GetAllEmployee() {
		RestAssured.baseURI = this.baseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("employees");
		System.out.println("here is the response " + response.getBody().asString());
		JsonPath jpath = response.jsonPath();
		List<String> names = jpath.get("name");
		for (int i = 0; i < names.size(); i++) {
			System.out.println("The name is :" + names.get(i));
		}
		return response;
	}

	public Response GetSingleEmployee(int empId) {
		RequestSpecification request = RestAssured.given();
		Response response = request.param("empId").get("employees");
		System.out.println(response.getBody().asString());
		return response;
	}

	public Response CreateEmployee(String empName, String empSalary) {
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		MapObj.put("name", empName);
		MapObj.put("salary", empSalary);
		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(MapObj).post("employees/create");
		return response;
	}

	public Response UpdateEmployee(int empId, int empSalary, String empName) {
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		MapObj.put("id", empId);
		MapObj.put("name", empName);
		MapObj.put("salary", empSalary);
		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).patch("employees/+empId");
		return response;

	}

	public Response DeleteEmployee(int empId) {
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		response = request.delete("employees/" + empId);
		return response;

	}
}
