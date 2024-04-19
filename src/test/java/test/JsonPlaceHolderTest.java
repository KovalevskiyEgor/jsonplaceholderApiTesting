package test;

import models.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import spec.Specifications;
import util.RandomString;
import java.util.List;
import static io.restassured.RestAssured.given;

public class JsonPlaceHolderTest {
    public static String URL = "https://jsonplaceholder.typicode.com/";
    public RandomString randomString = new RandomString();

    @Test
    public void test(){
        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK200());

        List<Post> posts = given()
                .when()
                .get("/posts")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getList("", Post.class);

        Assert.assertEquals(posts.get(0).getId(), 1);
        Assert.assertEquals(posts.get(1).getId(), 2);
        Assert.assertEquals(posts.get(2).getId(), 3);

        Post post = given()
                .when()
                .get("/posts/99")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getObject("", Post.class);

        Assert.assertEquals(post.getId(),99);
        Assert.assertEquals(post.getUserId(),10);
        Assert.assertNotEquals(post.getTitle(),"");
        Assert.assertNotEquals(post.getBody(),"");

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecError404());

        Post post150 = given()
                .when()
                .get("/posts/150")
                .then()
                .assertThat().statusCode(404)
                .extract().body().jsonPath().getObject("", Post.class);

        Assert.assertEquals(post150.getBody(), null);

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK201());

        Post postToCreate = Post.builder().body(randomString.createRandomString()).title(randomString.createRandomString()).userId(1).build();
        Post createdPost = given()
                .body(postToCreate)
                .when()
                .post("/posts")
                .then()
                .assertThat().statusCode(201)
                .extract().as(Post.class);

        Assert.assertEquals(postToCreate.getBody(),createdPost.getBody());
        Assert.assertEquals(postToCreate.getTitle(),createdPost.getTitle());
        Assert.assertEquals(postToCreate.getUserId(),createdPost.getUserId());
        Assert.assertNotEquals(createdPost.getId(),null);

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK200());

        List<User> users = given()
                .when()
                .get("users")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getList("", User.class);

        User user5 = users.get(4);
        Assert.assertEquals(user5.getName(),"Chelsey Dietrich");
        Assert.assertEquals(user5.getUsername(),"Kamren");
        Assert.assertEquals(user5.getEmail(),"Lucio_Hettinger@annie.ca");
        Assert.assertEquals(user5.getAddress().getStreet(),"Skiles Walks");
        Assert.assertEquals(user5.getAddress().getSuite(),"Suite 351");
        Assert.assertEquals(user5.getAddress().getCity(),"Roscoeview");
        Assert.assertEquals(user5.getAddress().getZipcode(),"33263");
        Assert.assertEquals(user5.getAddress().getGeo().getLat(),"-31.8129");
        Assert.assertEquals(user5.getAddress().getGeo().getLng(),"62.5342");
        Assert.assertEquals(user5.getPhone(),"(254)954-1289");
        Assert.assertEquals(user5.getWebsite(),"demarco.info");
        Assert.assertEquals(user5.getCompany().getName(),"Keebler LLC");
        Assert.assertEquals(user5.getCompany().getCatchPhrase(),"User-centric fault-tolerant solution");
        Assert.assertEquals(user5.getCompany().getBs(),"revolutionize end-to-end systems");

        User user = given()
                .when()
                .get("users/5")
                .then()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().getObject("", User.class);

        Assert.assertEquals(user, user5); //проверяем поля пользователя с id=5 полученным из массива и пользователя которого получили напрямую с api с id=5
    }
}