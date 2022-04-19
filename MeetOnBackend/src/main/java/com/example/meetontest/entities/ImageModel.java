package com.example.meetontest.entities;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] pic;

    public ImageModel(String name, String type, byte[] pic) {
        this.name = name;
        this.type = type;
        this.pic = pic;
    }
}