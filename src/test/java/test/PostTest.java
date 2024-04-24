package test;

import models.page.Post;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import spec.Specifications;
import util.*;
import util.Requests.PostRequests;
import java.util.List;

public class PostTest {
    public PropertyReader propertyReader = new PropertyReader("config.properties");
    public String URL = propertyReader.getProperty("base.URL");
    public RandomString randomString = new RandomString();
    public SoftAssert softAssert = new SoftAssert();
    public PostRequests postRequests = new PostRequests();

    @Test
    public void test(){
        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK200());
        List<Post> posts = postRequests.getList(propertyReader.getProperty("endpoint.posts"), 200);
        softAssert.assertEquals(posts.get(0).getId(), 1,"the first post has id 1");
        softAssert.assertEquals(posts.get(1).getId(), 2,"the second post has id 2");
        softAssert.assertEquals(posts.get(2).getId(), 3,"the third post has id 3");

        Post post = postRequests.get(propertyReader.getProperty("endpoint.posts")+"/99", 200);
        softAssert.assertEquals(post.getId(),99,"id is correct");
        softAssert.assertEquals(post.getUserId(),10,"user id is correct");
        softAssert.assertNotEquals(post.getTitle(),"","title is not empty");
        softAssert.assertNotEquals(post.getBody(),"","body is not empty");

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecError404());
        Post post150 = postRequests.get(propertyReader.getProperty("endpoint.posts")+"/150", 404);
        softAssert.assertEquals(post150.getBody(), null,"body is empty");

        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK201());
        Post postToCreate = Post.builder().body(randomString.createRandomString()).title(randomString.createRandomString()).userId(1).build();
        Post createdPost = postRequests.post(propertyReader.getProperty("endpoint.posts"), postToCreate, 201);

        softAssert.assertEquals(postToCreate.getBody(), createdPost.getBody(),"body is correct");
        softAssert.assertEquals(postToCreate.getTitle(),createdPost.getTitle(),"title is correct");
        softAssert.assertEquals(postToCreate.getUserId(),createdPost.getUserId(),"user id is correct");
        softAssert.assertNotEquals(createdPost.getId(),null,"id is not null");

        softAssert.assertAll();
    }
}