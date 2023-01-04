package com.example.ant.file.payload;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String name;
    private String userName;
    private String downloadUri;
    private String type;
    private long size;
}
