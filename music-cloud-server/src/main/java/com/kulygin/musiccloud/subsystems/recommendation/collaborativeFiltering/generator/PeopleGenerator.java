package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.domain.UserDetails;
import com.kulygin.musiccloud.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
public class PeopleGenerator {

    private List<String> mans;
    private List<String> womans;
    private List<String> lastnames;

    @Autowired
    private UserService userService;

    private void readData() {
        mans = new ArrayList<>();
        womans = new ArrayList<>();
        lastnames = new ArrayList<>();

        try {
            mans.addAll(Files.readAllLines(Paths.get("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\additional\\rcsData\\generator\\people\\man-firstnames.txt")));
            womans.addAll(Files.readAllLines(Paths.get("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\additional\\rcsData\\generator\\people\\woman-firstnames.txt")));
            lastnames.addAll(Files.readAllLines(Paths.get("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\additional\\rcsData\\generator\\people\\lastnames.txt")));
        } catch (IOException e) {
            log.error("Error via file reading: ",e);
        }
    }

    public List<User> generateUsers(Integer userCount) {
        readData();
        List<User> users = new ArrayList<>();

        for (Integer i = 1; i <= userCount; i++) {

            String fname = "";
            String sname = "";

            int manWoman = GeneratorUtils.rnd(0,1);
            if (manWoman == 0) {
                fname = mans.get(GeneratorUtils.rnd(0,mans.size()-1));
                sname = lastnames.get(GeneratorUtils.rnd(0,lastnames.size()-1));
            } else {
                fname = womans.get(GeneratorUtils.rnd(0,womans.size()-1));
                sname = lastnames.get(GeneratorUtils.rnd(0,lastnames.size()-1)) + "а";
            }

            User user = User.builder()
                    .id(i.longValue())
                    .email(GeneratorUtils.toTranslit(fname) + "." + GeneratorUtils.toTranslit(sname) + "@gmail.com")
                    .password(String.valueOf(GeneratorUtils.rnd(1000,9999)))
                    .dateCreate(LocalDateTime.now())
                    .userDetails(UserDetails.builder()
                            .id(i.longValue())
                            .firstName(fname)
                            .lastName(sname)
                            .birthday(LocalDateTime.of(GeneratorUtils.rnd(1970,2005), GeneratorUtils.rnd(1,12), GeneratorUtils.rnd(1,28), 0, 0, 0))
                            .build())
                    .build();

            users.add(user);
        }

        return userService.saveAll(users);
    }
}
