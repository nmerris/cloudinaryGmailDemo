package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    ActorRepo actorRepo;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @Autowired
    MemeRepo memeRepo;




    @GetMapping("/")
    public String listActors(Model model) {

        model.addAttribute("meme", new Meme());
        return "memeform";
    }


    @PostMapping("/")
    public String processMeme(@RequestParam("selectedFilter") String filter,
                              @RequestParam("selectedBorder") String border,
                              @ModelAttribute Meme meme, Model model) {

        meme.setArtisticFilter(filter);
        meme.setBorderType(border);
//        System.out.println("meme data... url: " + meme.getImageUrl() + ", border: " + meme.getBorderType() + ", filter: " + meme.getArtisticFilter());


        Map uploadResult = cloudinaryConfig.upload(meme.getImageUrl(),
                    ObjectUtils.asMap("resourcetype", "auto"));

        System.out.println(uploadResult);

//        System.out.println("uploadResult.get (secure_url): " + uploadResult.get("secure_url"));
        model.addAttribute("cloudinaryUrl", uploadResult.get("secure_url"));
        model.addAttribute("originalUrl", meme.getImageUrl());
        return "showimage";
    }


    @GetMapping("/add")
    public String newActor(Model model) {
        model.addAttribute("actor", new Actor());
        return "actorform";
    }


    @PostMapping("/add")
    public String processActor(@ModelAttribute Actor actor,
                               @RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! in processActor, file was empty");
            return "redirect:/add";
        }

        try {
            Map uploadResult = cloudinaryConfig.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            actor.setHeadshot(uploadResult.get("url").toString());

            actorRepo.save(actor);
        } catch (IOException e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! in processActor, caught exception trying to upload");
            e.printStackTrace();
            return "redirect:/add";
        }

        return "redirect:/";

    }


}
