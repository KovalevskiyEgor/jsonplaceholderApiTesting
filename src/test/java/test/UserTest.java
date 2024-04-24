package test;

import models.user.User;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import spec.Specifications;
import util.*;
import util.Requests.UserRequests;

import java.util.List;

public class UserTest {
    public PropertyReader propertyReader = new PropertyReader("config.properties");
    public String URL = propertyReader.getProperty("base.URL");
    public SoftAssert softAssert = new SoftAssert();
    public UserRequests userRequests = new UserRequests();

    @Test
    public void test(){
        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOK200());

        List<User> users = userRequests.getList(propertyReader.getProperty("endpoint.users"), 200);

        User user5 = users.get(4);
        softAssert.assertEquals(user5.getName(),"Chelsey Dietrich","name is correct");
        softAssert.assertEquals(user5.getUsername(),"Kamren","username is correct");
        softAssert.assertEquals(user5.getEmail(),"Lucio_Hettinger@annie.ca","email is correct");
        softAssert.assertEquals(user5.getAddress().getStreet(),"Skiles Walks","street is correct");
        softAssert.assertEquals(user5.getAddress().getSuite(),"Suite 351","suite is correct");
        softAssert.assertEquals(user5.getAddress().getCity(),"Roscoeview","city is correct");
        softAssert.assertEquals(user5.getAddress().getZipcode(),"33263", "zipcode is correct");
        softAssert.assertEquals(user5.getAddress().getGeo().getLat(),"-31.8129","geo lat is correct");
        softAssert.assertEquals(user5.getAddress().getGeo().getLng(),"62.5342", "geo lng is correct");
        softAssert.assertEquals(user5.getPhone(),"(254)954-1289", "phone is correct");
        softAssert.assertEquals(user5.getWebsite(),"demarco.info", "website is correct");
        softAssert.assertEquals(user5.getCompany().getName(),"Keebler LLC","company name is correct");
        softAssert.assertEquals(user5.getCompany().getCatchPhrase(),"User-centric fault-tolerant solution", "company catch phrase is correct");
        softAssert.assertEquals(user5.getCompany().getBs(),"revolutionize end-to-end systems","company bs is correct");

        User user = userRequests.get(propertyReader.getProperty("endpoint.users")+"/5", 200);

        softAssert.assertEquals(user, user5,"user is correct"); //проверяем поля пользователя с id=5 полученным из массива и пользователя которого получили напрямую с api с id=5
    }
}