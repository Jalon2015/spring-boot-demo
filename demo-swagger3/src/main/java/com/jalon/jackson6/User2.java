package com.jalon.jackson6;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User2 {

    private String username;

//    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = CustomDateSerializer2.class)
    @JsonDeserialize(using = CustomDateDeserializer2.class)
    private LocalDate birth;

}
