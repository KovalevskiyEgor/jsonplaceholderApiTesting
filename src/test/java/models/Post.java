package models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post{
    public int userId;
    public int id;
    public String title;
    public String body;
}