package com.example.demo.dto;

import lombok.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentDto implements StreamingResponseBody {
    private String name;
    private String mimeType;
    private ByteArrayOutputStream dataOutputStream;

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        dataOutputStream.writeTo(outputStream);
    }
}