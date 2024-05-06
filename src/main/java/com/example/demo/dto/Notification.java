package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class Notification {
    private String key;
    private List<String> emails;
    private String theme;
    private String message;
}
