package util.Requests;

import io.qameta.allure.Step;
import models.page.Post;
import java.util.List;
import static io.restassured.RestAssured.given;

public class PostRequests extends Requests{
    @Override
    @Step("Get All Posts")
    public List<Post> getList(String uri, int statusCode) {
        return given()
                .when()
                .get(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().body().jsonPath().getList("", Post.class);
    }

    @Override
    @Step("Get specific post")
    public Post get(String uri, int statusCode) {
        return given()
                .when()
                .get(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().body().jsonPath().getObject("", Post.class);
    }

    @Override
    @Step("Add a post")
    public Post post(String uri, Object object, int statusCode) {
        Post post = (Post) object;
        return given()
                .body(post)
                .when()
                .post(uri)
                .then()
                .assertThat().statusCode(statusCode)
                .extract().as(Post.class);
    }
}