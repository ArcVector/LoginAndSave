package com.example.ant.file.model;

import com.example.ant.login.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name ="File")
@Data
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @NotBlank
    private String name;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "User")
    @OrderBy("userName")
    private User user;
    @NotBlank
    private String type;
    @NotBlank
    @Lob
    private byte[] data;


}
