package util.Requests;

import io.qameta.allure.Step;
import models.user.User;
import java.util.List;
import static io.restassured.RestAssured.given;

public class UserRequests extends Requests {
    @Override
    @Step("Get All Users")
    public List<User> getList(String uri, int statusCode) {
        return given()
                .when()
                .get(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().body().jsonPath().getList("", User.class);
    }

    @Override
    @Step("Get specific user")
    public User get(String uri, int statusCode) {
        return given()
                .when()
                .get(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().body().jsonPath().getObject("", User.class);
    }
}