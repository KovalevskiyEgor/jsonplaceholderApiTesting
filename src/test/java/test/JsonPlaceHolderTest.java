package test;

import models.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import spec.Specifications;
import util.RandomString;
import java.util.List;
import static io.restassured.RestAssured.given;

public class JsonPlaceHolderTest {
    public static String URL = "https://jsonplaceholder.typicode.com/";
    public RandomString randomString = new RandomString();
    public SoftAssert softAssert = new SoftAssert();

    @Test
    public void test(){
        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK200());

        List<Post> posts = given()
                .when()
                .get("/posts")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getList("", Post.class);

        softAssert.assertEquals(posts.get(0).getId(), 1);
        softAssert.assertEquals(posts.get(1).getId(), 2);
        softAssert.assertEquals(posts.get(2).getId(), 3);

        Post post = given()
                .when()
                .get("/posts/99")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getObject("", Post.class);

        softAssert.assertEquals(post.getId(),99);
        softAssert.assertEquals(post.getUserId(),10);
        softAssert.assertNotEquals(post.getTitle(),"");
        softAssert.assertNotEquals(post.getBody(),"");

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecError404());

        Post post150 = given()
                .when()
                .get("/posts/150")
                .then()
                .assertThat().statusCode(404)
                .extract().body().jsonPath().getObject("", Post.class);

        softAssert.assertEquals(post150.getBody(), null);

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK201());

        Post postToCreate = Post.builder().body(randomString.createRandomString()).title(randomString.createRandomString()).userId(1).build();
        Post createdPost = given()
                .body(postToCreate)
                .when()
                .post("/posts")
                .then()
                .assertThat().statusCode(201)
                .extract().as(Post.class);

        softAssert.assertEquals(postToCreate.getBody(),createdPost.getBody());
        softAssert.assertEquals(postToCreate.getTitle(),createdPost.getTitle());
        softAssert.assertEquals(postToCreate.getUserId(),createdPost.getUserId());
        softAssert.assertNotEquals(createdPost.getId(),null);

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK200());

        List<User> users = given()
                .when()
                .get("users")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getList("", User.class);

        User user5 = users.get(4);
        softAssert.assertEquals(user5.getName(),"Chelsey Dietrich");
        softAssert.assertEquals(user5.getUsername(),"Kamren");
        softAssert.assertEquals(user5.getEmail(),"Lucio_Hettinger@annie.ca");
        softAssert.assertEquals(user5.getAddress().getStreet(),"Skiles Walks");
        softAssert.assertEquals(user5.getAddress().getSuite(),"Suite 351");
        softAssert.assertEquals(user5.getAddress().getCity(),"Roscoeview");
        softAssert.assertEquals(user5.getAddress().getZipcode(),"33263");
        softAssert.assertEquals(user5.getAddress().getGeo().getLat(),"-31.8129");
        softAssert.assertEquals(user5.getAddress().getGeo().getLng(),"62.5342");
        softAssert.assertEquals(user5.getPhone(),"(254)954-1289");
        softAssert.assertEquals(user5.getWebsite(),"demarco.info");
        softAssert.assertEquals(user5.getCompany().getName(),"Keebler LLC");
        softAssert.assertEquals(user5.getCompany().getCatchPhrase(),"User-centric fault-tolerant solution");
        softAssert.assertEquals(user5.getCompany().getBs(),"revolutionize end-to-end systems");

        User user = given()
                .when()
                .get("users/5")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getObject("", User.class);

        softAssert.assertEquals(user, user5); //проверяем поля пользователя с id=5 полученным из массива и пользователя которого получили напрямую с api с id=5

        softAssert.assertAll();
    }
}