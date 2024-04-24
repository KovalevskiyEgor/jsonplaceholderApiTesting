package util.Requests;

import java.util.List;
import static io.restassured.RestAssured.given;

public abstract class Requests {
    public List<?> getList(String uri, int statusCode){
        List<Object> objects = given()
                .when()
                .get(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().body().jsonPath().getList("", Object.class);
        return objects;
    }
    public Object get(String uri, int statusCode){
        Object object = given()
                .when()
                .get(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().body().jsonPath().getObject("", Object.class);

        return object;
    }
    public Object post(String uri,Object object, int statusCode){
        Object createdObject = given()
                .body(object)
                .when()
                .post(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().as(Object.class);
        return createdObject;
    }
}