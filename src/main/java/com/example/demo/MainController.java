package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    ActorRepo actorRepo;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @RequestMapping("/")
    public String listActors(Model model) {
        model.addAttribute("actors", actorRepo.findAll());
        return "list";
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
